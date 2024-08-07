package com.HelpMe.Service.impl.core;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.HelpMe.dto.*;
import com.HelpMe.Entity.core.*;
import com.HelpMe.mapper.*;
import com.HelpMe.Service.CoreService.TUserService;
import com.HelpMe.utils.*;
import com.HelpMe.Service.CoreService.TasksService;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
* @author 艾莉希雅
* &#064;description  针对表【tasks】的数据库操作Service实现
* &#064;createDate  2023-08-20 10:37:17
 */
//TODO:每一个获取订单的高key需要加缓存
@Service
public class TasksServiceImpl extends ServiceImpl<TasksMapper, Task> implements TasksService{

    private final TUserService tUserService;
    private final StringRedisTemplate stringRedisTemplate;
    private final CacheClient cacheClient;
    private final PastimesMapper pastimesMapper;
    private final TasksMapper tasksMapper;
    private final TaskCategoriyMapper taskCategoriyMapper;
    private final TaskTimesMapper taskTimesMapper;

    private final OrdersMapper ordersMapper;
    private final RabbitTemplate rabbitTemplate;
    private final RedissonClient redissonClient;

    @Autowired
    public TasksServiceImpl(TUserService tUserService, StringRedisTemplate stringRedisTemplate, CacheClient cacheClient, PastimesMapper pastimesMapper, TaskCategoriyMapper taskCategoriyMapper, TasksMapper tasksMapper, TaskCategoriyMapper taskCategoriyMapper1, TaskTimesMapper taskTimesMapper, OrdersMapper ordersMapper, RabbitTemplate rabbitTemplate, RedissonClient redissonClient) {
        this.tUserService = tUserService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.cacheClient = cacheClient;
        this.pastimesMapper = pastimesMapper;
        this.tasksMapper = tasksMapper;
        this.taskCategoriyMapper = taskCategoriyMapper;
        this.taskTimesMapper = taskTimesMapper;
        this.ordersMapper = ordersMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.redissonClient = redissonClient;
    }

    //创建订单
    @Transactional
    @Override
    public HttpResult createTask(TaskForm taskForm) {
        var user = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong((String) user.getPrincipal());
        TUser tUser = tUserService.query().select("id","ach_url","nick_name").eq("login_name",userId).one();
        if(tUser == null){
            return HttpResult.fail("登录凭证错误，请重新登录");
        }
        var task = Task.builder()
                .initiatorId(tUser.getId())
                .name(taskForm.getName())
                .description(taskForm.getDescription())
                .x(taskForm.getX())
                .y(taskForm.getY())
                .status(TaskEnum.FALSE.state)
                .imageUrl(String.join(",",taskForm.getImageUrl()))
                .userId(tUser.getId())
                .userIcon(tUser.getAchUrl())
                .userName(tUser.getNickName())
                .price(taskForm.getPrice())
                .bigType(taskForm.getBigType())
                .build();
        //数据校验
        //设置创建日期
        task.setDate(new Timestamp(System.currentTimeMillis()));
        //在数据库中存储
        boolean flag = save(task);
        if(!flag){
            return HttpResult.fail("未知错误");
        }
        //先在redis中存储位置信息
        boolean isTrue = updateGEO(task);
        //创建日期
        int timeFlag = taskTimesMapper.insert(TaskTimes.builder()
                        .createTime(Timestamp.valueOf(LocalDateTime.now()))
                        .taskId(task.getId())
                .build());
        if(!isTrue && timeFlag < 0){
            return HttpResult.builder().code(ErrorCodeEnum.FAIL.code).msg("位置信息错误").build();
        }
        return HttpResult.builder().code(ErrorCodeEnum.SUCCESS.code).data(true).msg("发送成功").build();
    }

    //获取其他任务
    @Override
    public HttpResult getOtherTasksBySortKey(Long otherUserId, Integer sortKey, Integer pageNum) {
        if (otherUserId == null) {
            otherUserId = AuthenticationUtils.getId();
        }
        // 创建新的分页对象
        var page = new Page<Task>(pageNum, SystemConstants.MAX_PAGE_SIZE);
        List<Task> records = tasksMapper.queryByTypeWith(page,TaskEnum.FALSE.state,otherUserId).getRecords().stream().toList();
        setUserInfo(records);
        // 返回结果
        return HttpResult.success(records);
    }

    private void setUserInfo(List<Task> tasks) {
        tasks.forEach(task -> {
            Integer sortKey = task.getStatus();
            var user = (sortKey.equals(TaskEnum.TRUE.state) || sortKey.equals(TaskEnum.FINISH.state) || sortKey.equals(TaskEnum.UNFINISH.state))
                    ? tUserService.getById(task.getInitiatorId())
                    : tUserService.getById(task.getAssigneeId());
            if(user != null){
                task.setUserName(user.getNickName());
                task.setUserId(user.getId());
                task.setUserIcon(user.getAchUrl());
            }
        });
    }

    //获取订单时间
    @Override
    public HttpResult getTaskTime(Long taskId) {
        QueryWrapper<TaskTimes> wrapper = new QueryWrapper<>();
        wrapper.eq("task_id",taskId);
        TaskTimes taskTimes = pastimesMapper.selectOne(wrapper);
        if(taskTimes == null){
            return HttpResult.fail("未查到该订单时间信息");
        }
        return HttpResult.success(taskTimes);
    }

    //通过任务类别获取任务
    @Override
    public HttpResult getTypeTasks(String type, Integer pageNum) {
        //进行缓存
        if("all".equals(type)){
            var page = new Page<Task>(pageNum, SystemConstants.MAX_PAGE_SIZE);
            List<Task> tasks = query().select(  "id", "image_url", "name", "status", "description",
                    "initiator_id", "assignee_id", "date", "comments",
                    "type", "big_type", "x", "y", "progress",
                    "city", "price").eq("status",2).orderByAsc("date").page(page).getRecords();
            setUserInfo(tasks);
            return HttpResult.success(tasks);
        }
        var off = (pageNum - 1) * 20;
        if(off < 0){
            return HttpResult.fail("错误页数");
        }
        var taskList = tasksMapper.getAllByTypeTasks(type,off);
        if(taskList == null){
            return HttpResult.fail("服务器异常");
        }
        setUserInfo(taskList);
        return HttpResult.success(taskList);
    }
    //通过关键词搜索任务
    @Override
    public HttpResult getByKey(String enKey, Integer pageNum) {
        var off = (pageNum - 1) * 20;
        if(off < 0){
            return HttpResult.fail("错误页数");
        }
        var taskList = tasksMapper.getByKeyTasks(enKey,off);
        if(taskList == null){
            return HttpResult.fail("服务器异常");
        }
        setUserInfo(taskList);
        return HttpResult.success(taskList);
    }

    @Override
    public HttpResult getAllTask() {
        return HttpResult.success(tasksMapper.selectList(null));
    }

    @Override
    public HttpResult delectTask(String taskId) {
        return HttpResult.success(tasksMapper.deleteById(taskId));
    }

    //获取订单详情页
    @Override
    public HttpResult queryTaskById(Long id) {
        Task task = getTask(id);
        if(task == null){
            return HttpResult.builder().code(ErrorCodeEnum.FAIL.code).msg("不存在的id").build();
        }
        queryTaskUser(task);
        return HttpResult.builder().code(ErrorCodeEnum.SUCCESS.code).data(task).build();
    }

    private void queryTaskUser(Task task) {
        long userId = task.getInitiatorId();
        TUser user = tUserService.getById(userId);
        task.setUserId(user.getId());
        task.setUserName(user.getNickName());
        task.setUserIcon(user.getAchUrl());
    }

    @Override
    @Transactional
    public HttpResult update(Task task){
        Long id = task.getId();
        if(id == null){
            return HttpResult.builder().code(ErrorCodeEnum.FAIL.code).data(false).msg("id为空").build();
        }
        //更新数据库
        boolean isSu = updateById(task);
        //删除缓存
        if(isSu){
            stringRedisTemplate.delete(RedisConstants.CACHE_TASK_KEY + id);
            //存储布隆过滤器
            redissonClient.getBloomFilter("task").add(id);
            return HttpResult.builder().code(ErrorCodeEnum.SUCCESS.code).data(true).build();
        }
        else {
            return HttpResult.builder().code(ErrorCodeEnum.FAIL.code).data(false).msg("未知错误").build();
        }
    }
    @Override
    @Transactional
    public boolean updateGEO(Task task){
        String key = RedisConstants.TASK_GEO_KEY + task.getStatus();
        Long number = stringRedisTemplate.opsForGeo().add(key,new Point(task.getX(),task.getY()),task.getId().toString());
        if(number == null) {
            return false;
        }
        return number == 1;
    }
    @Transactional
    @Override
    //用户承接任务
    public HttpResult acceptTask(Long taskId) {
        Long userId = AuthenticationUtils.getId();
        //判断任务是否被承接
        synchronized (userId.toString().lines()) {
            Task task = getById(taskId);
            if (!task.getStatus().equals(TaskEnum.FALSE.state)) {
                return HttpResult.builder().data(false).code(ErrorCodeEnum.FAIL.code).msg("该任务已被承接").build();
            }
            //修改任务状态
            task.setStatus(TaskEnum.UNFINISH.state);
            //添加用户任务清单
            task.setAssigneeId(userId);
            update(task);
        }
        //订单承接成功后向发起方发送消息
        rabbitTemplate.convertAndSend(RabbitMessage.EXCHANGE_NAME, RabbitMessage.SYSTEM_INFO_ROUTING_KEY,
                JSONUtil.toJsonStr(
                        SystemMessageForm.builder().message( "您的订单已被id为"+userId+"的用户承接")
                                .userId(userId.toString())
                ));
        return HttpResult.builder().code(ErrorCodeEnum.SUCCESS.code).data(true).build();
    }

    //给任务添加评论数
    @Transactional
    @Override
    public HttpResult addMessageCount(Long taskId){
        Task task = getById(taskId);
        //添加评论数
        task.setComments(task.getComments()+1);
        return update(task);
    }
    //获取状态不同，类型不同的task列表
    @Override
    public HttpResult getTasksBySortKey(Integer pageNum, Integer status, Double x, Double y) {
        Page<Task> page =null;
        //从缓存获取数据
        int form = (pageNum-1) * SystemConstants.DEFAULT_PAGE_SIZE;
        int end = pageNum * SystemConstants.DEFAULT_PAGE_SIZE;

        //获取GEO 数据 圆心，距离，半径
        String key = RedisConstants.TASK_GEO_KEY + status;
        return getTasksHttpResult(x, y, form, end, key);
    }
    //获取默认的主页任务
    @Override
    public HttpResult getTasksByTime(Integer pageNum) {
        var data = query().eq("status",TaskEnum.FALSE.state)
                .page(new Page<>(pageNum, SystemConstants.DEFAULT_PAGE_SIZE)).getRecords();
        if(data == null){
            return HttpResult.builder().code(ErrorCodeEnum.FAIL.code).msg("获取失败").data(null).build();
        }
        data.forEach( task -> {
            Long initiatorId = task.getInitiatorId();
            TUser user = tUserService.getById(initiatorId);
            if(user == null){
                throw new RuntimeException("错误参数，角色id");
            }
            task.setUserName(user.getNickName());
            task.setUserIcon(user.getAchUrl());
            task.setUserId(user.getId());
        });
        return HttpResult.builder().msg("获取成功").code(ErrorCodeEnum.SUCCESS.code).data(data).build();
    }


    //确认任务完成但是未付款
    @Override
    @Transactional
    public HttpResult confirmTaskCompletion(Long taskId) {
        Task task = getTask(taskId);
        //创建订单
        TOrder orders = TOrder.builder()
                .orderData(Timestamp.valueOf(LocalDateTime.now()))
                .status(OrderEnum.OUT.status)
                .productId(taskId)
                .customerId(task.getUserId())
                .totalAmount(task.getPrice())
                .build();
        return HttpResult.builder().msg("创建订单成功").data(orders).code(ErrorCodeEnum.SUCCESS.code).build();
    }

    //任务完成
    @Override
    @Transactional
    public HttpResult successTask(String taskId){
        Task task = getTask(taskId);
        if(task == null){
            return HttpResult.builder().code(ErrorCodeEnum.FAIL.code).msg("不存在的id").build();
        }
        //修改任务状态
        task.setStatus(TaskEnum.UNFINISH.state);
        //修改任务截止时间
        TaskTimes taskTimes = taskTimesMapper.selectOne(new QueryWrapper<TaskTimes>().eq("task_id",taskId));
        taskTimes.setFinishTime(Timestamp.valueOf(LocalDateTime.now()));
        taskTimesMapper.update(taskTimes,new QueryWrapper<TaskTimes>().eq("id",taskTimes.getId()));
        //任务完成后向承接方发送消息
        Long initiatorId = task.getInitiatorId();
        rabbitTemplate.convertAndSend(RabbitMessage.EXCHANGE_NAME, RabbitMessage.SYSTEM_INFO_ROUTING_KEY,
                JSONUtil.toJsonStr(SystemMessageForm.builder()
                        .message( "您承接的任务"+task.getName()+"已经被确认完成").userId(initiatorId.toString())));
        HttpResult httpResult = update(task);
        if(httpResult.getCode().equals(ErrorCodeEnum.FAIL.code)){
            return httpResult;
        }
        //创建订单
        TOrder orders = TOrder.builder()
                .id(IdUtils.snowflake.nextId())
                .unitPrice(task.getPrice())
                .status(OrderEnum.OUT.status)
                .productId(task.getInitiatorId())
                .customerId(task.getId())
                .totalAmount(task.getPrice())
                .orderData(new Timestamp(System.currentTimeMillis()))
                .build();
        ordersMapper.insert(orders);
        //返回订单数据
        return HttpResult.success(orders);
    }
    public Task getTask(Serializable taskId){
        return cacheClient.queryWithPassThrough(RedisConstants.CACHE_TASK_KEY,taskId,Task.class, id ->getById(taskId), RedisConstants.CACHE_TASK_TTL,TimeUnit.MINUTES);
    }

    //主页获取附近任务列表
    @Override
    public HttpResult getTasksByLocation(Integer pageNum, Double x, Double y) {
        Page<Task> page = null;
        //通过用户位置获取最近任务
        int form = (pageNum-1) * SystemConstants.DEFAULT_PAGE_SIZE;
        int end = pageNum * SystemConstants.DEFAULT_PAGE_SIZE;
        //获取GEO 数据 圆心，距离，半径
        //主页展示任务key
        String key = RedisConstants.TASK_GEO_KEY + 5;
        return getTasksHttpResult(x, y, form, end, key);
    }

    private HttpResult getTasksHttpResult(Double x, Double y, int form, int end, String key) {
        GeoResults<RedisGeoCommands.GeoLocation<String>> search = stringRedisTemplate.opsForGeo().search(
                key, //查询需要的key
                GeoReference.fromCoordinate(x, y),//查询圆心
                new Distance(500000000),//查询坐标
                RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs().includeDistance().limit(end)
        );
        //获取id并进行分页处理
        if(search == null ){
            return HttpResult.builder().code(ErrorCodeEnum.FAIL.code).msg("查询为空").build();
        }
        var result = search.getContent();
        if(result.size() <= form){
            return HttpResult.builder().data(Collections.emptyList()).code(ErrorCodeEnum.FAIL.code).msg("查询为空").build();
        }
        //根据id获取
        List<Long> ids = new ArrayList<>(result.size());
        Map<String,Distance> distanceMap = new HashMap<>(result.size());
        //截取
        result.stream().skip(form).forEach(re ->{
                    var name = re.getContent().getName();
                    ids.add(Long.valueOf(name));
                    var dis = re.getDistance();
                    distanceMap.put(name,dis);
                }
        );
        String idStr = StrUtil.join("",ids);
        //根据id查询Task
        List<Task> idList = query().select("id", "image_url", "name", "status", "description",
                "initiator_id", "assignee_id", "date", "comments",
                "type", "big_type", "x", "y", "progress",
                "city", "price").in("id", ids).eq("status",5).last("ORDER BY FIELD(id," + idStr + ")").list();
        for(Task task:idList){
            task.setDistance(distanceMap.get(task.getId().toString()).getValue());
        }
        return HttpResult.builder().code(ErrorCodeEnum.SUCCESS.code).data(idList).msg("查询成功").build();
    }


}




