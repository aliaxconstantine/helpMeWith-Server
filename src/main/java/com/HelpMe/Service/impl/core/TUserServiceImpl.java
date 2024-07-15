package com.HelpMe.Service.impl.core;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.HelpMe.dto.*;
import com.HelpMe.Entity.Security.SysUser;
import com.HelpMe.Entity.Security.SysUserRule;
import com.HelpMe.Entity.core.TStar;
import com.HelpMe.Entity.core.TUser;
import com.HelpMe.Entity.core.TUserInfo;
import com.HelpMe.Entity.core.Task;
import com.HelpMe.mapper.*;
import com.HelpMe.Service.CoreService.TUserService;
import com.HelpMe.utils.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
//TODO:登录接口转移到 Security里
/**
* @author 艾莉希雅
* &#064;description  针对表【t_user】的数据库操作Service实现
* &#064;createDate  2023-08-20 10:37:10
 */
@Service
public class TUserServiceImpl extends ServiceImpl<UserMapper, TUser> implements TUserService {

    private final StringRedisTemplate stringRedisTemplate;
    private final UserMapper userMapper;
    private final SysUserMapper sysUserMapper;
    private final SysUserRuleMapper sysUserRuleMapper;
    private final TUserInfoMapper userInfoMapper;
    private final TStarMapper starMapper;
    private final TasksMapper tasksMapper;
    @Autowired
    public TUserServiceImpl(StringRedisTemplate stringRedisTemplate, UserMapper userMapper, SysUserMapper sysUserMapper, SysUserRuleMapper sysUserRuleMapper, TUserInfoMapper userInfoMapper, TStarMapper starMapper, TasksMapper tasksMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.userMapper = userMapper;
        this.sysUserMapper = sysUserMapper;
        this.sysUserRuleMapper = sysUserRuleMapper;
        this.userInfoMapper = userInfoMapper;
        this.starMapper = starMapper;
        this.tasksMapper = tasksMapper;
    }

    @Override
    public HttpResult sendCode(String phone, HttpSession httpSession) {
        //校验手机号
        if(RegexUtils.isPhoneInvalid(phone)){
            //如果不符合，返回错误信息
            return HttpResult.builder().code(ErrorCodeEnum.FAIL.code).msg("手机号格式错误").build();
        }
        //符合，生成验证码
        String code = RandomUtil.randomNumbers(6);
        //保存验证码到Redis
        stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY+phone,code, RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);
        //发送验证码
        return HttpResult.builder().code(ErrorCodeEnum.SUCCESS.code).data(code).msg("获取成功").build();
    }
    @Override
    public HttpResult sendCode(String nickName) {
        //生成验证码
        String code = RandomUtil.randomNumbers(6);
        //保存验证码到Redis
        stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY+nickName,code, RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);
        //发送验证码
        return HttpResult.builder().data(code).code(ErrorCodeEnum.SUCCESS.code).msg("获取成功").build();
    }

    @Override
    public boolean Login(LoginForm loginFrom){
        String phone = loginFrom.getPhone();
        //1：从redis获取并校验验证码
        String cacheCode = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY+phone);
        String code = loginFrom.getCode();
        if(RegexUtils.isCodeInvalid(loginFrom.getCode()) || cacheCode == null || !cacheCode.equals(code)){
            return false;
        }
        //根据手机号查找用户
        TUser user = query().eq("phone",phone).one();
        if(user == null){
            user = createTUserWithPhone(phone);
        }
        //2：保存用户信息到redis中
        //将user对象转换为Hash存储
        UserForm userFrom = BeanUtil.copyProperties(user, UserForm.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(userFrom,new HashMap<>(),
                CopyOptions.create().
                        setIgnoreNullValue(true).
                        setFieldValueEditor((key,value) -> value.toString())
        );
        var userid = RedisConstants.LOGIN_USER_KEY+user.getId();
        //存储
        stringRedisTemplate.opsForHash().putAll(userid,userMap);
        stringRedisTemplate.expire(userid, RedisConstants.LOGIN_USER_TTL,TimeUnit.MINUTES);
        return true;
    }


    @Override
    public TUser getByPhone(String phone) {
       return userMapper.selectUserByPhone(phone);
    }

    @Override
    @Transactional
    public HttpResult register(TUser tUser) {
        //检查是否重复注册
        Long phone = tUser.getPhone();
        TUser oldUser = query().eq("phone",phone).one();
        if(oldUser != null){
            return HttpResult.fail("该手机号已经注册过");
        }
        //用户名查询是否重复
        TUser nickOld = query().eq("nick_name",tUser.getNickName()).one();
        if(nickOld != null){
            return HttpResult.fail("该用户名已被使用");
        }
        tUser.setNickName(IdUtils.generate());
        String password = new BCryptPasswordEncoder().encode(tUser.getPassword());
        SysUser sysUser = SysUser.builder()
                .id(IdUtils.snowflake.nextId())
                .username(tUser.getLoginName())
                .password(password)
                .enabled(1)
                .accountNoExpired(1)
                .accountNoLocked(1)
                .credentialsNoExpired(1)
                .build();
        //加入安全角色
        sysUserMapper.insert(sysUser);
        sysUserRuleMapper.insert(SysUserRule.builder().userId(sysUser.getId()).menuId(1).build());
        //注册用户
        tUser.setSysUser(sysUser.getId());
        log.error(tUser.toString());
        boolean flag = save(tUser);
        if(!flag){
            return HttpResult.fail("服务器错误");
        }
        //创建详细信息
        TUserInfo userInfo = TUserInfo.builder()
                .userId(tUser.getId())
                .createTime(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        userInfoMapper.insert(userInfo);
        return HttpResult.success("注册成功");
    }
    @Override
    @Transactional
    public TUser createTUserWithPhone(String phone){
        TUser user = new TUser();
        user.setLoginName(phone);
        user.setNickName(SystemConstants.USER_NICK_NAME_PREFIX+RandomUtil.randomString(10));
        save(user);
        return user;
    }

    @Override
    public HttpResult getOtherById(String otherId) {
        return null;
    }

    @Override
    public HttpResult getMe(Boolean ifFrom) {
        String name = AuthenticationUtils.getName();
        TUser tUser = query().select("id","nick_name","password","ach_url").eq("login_name",name).one();
        if(tUser == null){
            return HttpResult.fail("登录凭证错误，请尝试重新登录");
        }
        TUserInfo userInfo = null;
        if(ifFrom){
            QueryWrapper<TUserInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id",tUser.getId());
            userInfo = userInfoMapper.selectOne(wrapper);
            if(userInfo == null){
                //创建默认userInfo
                userInfo = TUserInfo.builder()
                        .workAddress("空")
                        .userId(AuthenticationUtils.getId())
                        .workType("空")
                        .createTime(new Timestamp(System.currentTimeMillis()))
                        .build();
            }
            return HttpResult.success(userInfo);
        }
        UserForm userFrom = UserForm.builder()
                .userId(tUser.getId())
                .nickName(tUser.getNickName())
                .icon(tUser.getAchUrl())
                .userInfo(userInfo)
                .build();

        return HttpResult.builder().code(ErrorCodeEnum.SUCCESS.code).data(userFrom).msg("获取成功").build();
    }

    @Override
    @Transactional
    public HttpResult updateUser(UserForm userFrom) {
        //更新用户
        TUser tUser = new TUser();
        tUser.setId(AuthenticationUtils.getId());
        tUser.setNickName(userFrom.getNickName());
        tUser.setAchUrl(userFrom.getIcon());
        updateById(tUser);

        //更新详细信息
        TUserInfo oldUserInfo = userFrom.getUserInfo();
        oldUserInfo.setUserId(AuthenticationUtils.getId());
        if(oldUserInfo == null){
            return HttpResult.success("修改失败，请重新登录");
        }
        userInfoMapper.updateById(oldUserInfo);
        return HttpResult.success("修改成功");
    }


    private HttpResult getStars(Integer pageNum, Long userId) {
        QueryWrapper<TStar> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        var tStar =  starMapper.selectPage(new Page<>(pageNum,SystemConstants.MAX_PAGE_SIZE),wrapper).getRecords();
        tStar.forEach( s ->{
            QueryWrapper<TUser> u_wrapper = new QueryWrapper<>();
            u_wrapper.select("nick_name","ach_url");
            u_wrapper.eq("id", s.getUserId());
            var user = userMapper.selectOne(u_wrapper);
            if(user == null){
                throw new RuntimeException("数据参数错误");
            }
            s.setNickName(user.getNickName());
            s.setAchUrl(user.getAchUrl());

            QueryWrapper<Task> t_wrapper = new QueryWrapper<>();
            u_wrapper.select("image_url","name");
            u_wrapper.eq("id", s.getTaskId());
            var task = tasksMapper.selectOne(t_wrapper);
            if(task == null){
                throw new RuntimeException("数据参数错误");
            }
            s.setTaskName(task.getName());
            //只获取第一张图
            var string = task.getImageUrl();
            var imageUrl = task.getImageUrl().substring(0, task.getImageUrl().indexOf(','));
            s.setTaskUrl(imageUrl);
        });
        return HttpResult.success(tStar);
    }
    @Override
    public HttpResult getStar(Integer pageNum){
        Long userId = AuthenticationUtils.getId();
        return getStars(pageNum,userId);
    }

    @Override
    public HttpResult getOtherStar(Long otherId, Integer pageNum) {
        return getStars(pageNum,otherId);
    }

    @Override
    @Transactional
    public HttpResult rePassword(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 修改账户密码
        TUser user = new TUser();
        user.setId(AuthenticationUtils.getId());
        user.setPassword(password);
        boolean flag = update().update(user);
        // 修改系统账户密码
        SysUser sysUser = SysUser.builder()
                .password(passwordEncoder.encode(password))
                .build();
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username",AuthenticationUtils.getName());
        int flag1 = sysUserMapper.update(sysUser,wrapper);
        // 返回修改结果
        if (flag && flag1 > 0) {
            return HttpResult.success(true);
        } else {
            return HttpResult.fail("修改失败");
        }
    }

    @Override
    public HttpResult updateAavatar(String url) {
        TUser tUser = new TUser();
        tUser.setAchUrl(url);
        tUser.setId(AuthenticationUtils.getId());
        boolean flag = updateById(tUser);
        if(!flag){
            return HttpResult.fail("上传错误");
        }
        return HttpResult.success(true);
    }

    @Override
    public HttpResult getAllUser() {
        return HttpResult.success(userMapper.selectList(null));
    }

    @Override
    public HttpResult delectUser(String userId) {
        return HttpResult.success(userMapper.deleteById(userId));
    }
}




