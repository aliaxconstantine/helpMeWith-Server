package com.man.controller;
import com.man.dto.*;
import com.man.entity.core.TUser;
import com.man.entity.core.TUserInfo;
import com.man.service.CoreService.*;
import com.man.service.impl.core.TUserServiceImpl;
import com.man.utils.AuthenticationUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@ResponseBody
@RestController
@RequestMapping(value = "/user",produces = {"application/json;charset=utf-8"})
public class UserController {

    private final TUserService userService;
    private final TasksService tasksService;
    private final FriendService friendService;
    private final SysUserService sysUserService;
    private final SysUserRuleService sysUserRuleService;
    private final TUserInfoService tUserInfoService;

    private final CommunicationsService communicationsService;

    @Autowired
    public UserController(TUserServiceImpl userService, TasksService tasksService, FriendService friendService, SysUserService sysUserService, SysUserRuleService sysUserRuleService, TUserInfoService tUserInfoService, CommunicationsService communicationsService) {
        this.userService = userService;
        this.tasksService = tasksService;
        this.friendService = friendService;
        this.sysUserService = sysUserService;
        this.sysUserRuleService = sysUserRuleService;
        this.tUserInfoService = tUserInfoService;
        this.communicationsService = communicationsService;
    }
    //发送验证码
    @PostMapping("/code")
    public HttpResult sendCode(@RequestParam("phone") String phone, HttpSession session){
        return userService.sendCode(phone,session);
    }

    @PostMapping("/pwdCode")
    public HttpResult sendCode(@RequestParam("name") String nickName){
        return userService.sendCode(nickName);
    }

    //获取其他用户资料：
    @GetMapping("/other")
    public HttpResult getOtherUser(@RequestParam(name = "otherId") String otherId)
    {
        TUser user = userService.getById(otherId);
        if(user == null){
            return HttpResult.fail("id错误");
        }
        TUserInfo userInfo = tUserInfoService.query().eq("user_id",user.getId()).one();
        OtherUserFrom userFrom = OtherUserFrom.builder()
                .id(user.getId())
                .achUrl(user.getAchUrl())
                .nickname(user.getNickName())
                .tUserInfo(userInfo)
                .build();

        return HttpResult.success(userFrom);
    }
    //用户注册
    @PostMapping("/register")
    public HttpResult registerUser(@Validated @RequestBody TUser tUser){
        return userService.register(tUser);
    }

    // 修改用户资料
    @PostMapping("/update")
    public HttpResult updateUser(@RequestBody UserFrom userFrom){
        return userService.updateUser(userFrom);
    }
    //获取自己资料
    @GetMapping("/me")
    public HttpResult me(){
        return userService.getMe(false);
    }

    //用户获取对应状态的任务，发布时间，按照属性排序
    @GetMapping("/tasks")
    public HttpResult getTasksByType(@RequestParam(name = "sortKey") Integer sortKey,
                                     @RequestParam(name = "pageNum") Integer pageNum) {
        return tasksService.getOtherTasksBySortKey(null,pageNum,sortKey);
    }

    //获取我的信息
    @GetMapping("/me/info")
    public HttpResult meInfo(){
        return userService.getMe(true);
    }

    //查看我的评价
    @GetMapping("/me/star")
    public HttpResult meStar(@RequestParam(name = "pageNum") Integer pageNum){
        return userService.getStar(pageNum);
    }


    //修改账户密码
    @PostMapping("/me/password")
    public HttpResult rePassword(@RequestParam(name="pwd") String password){
        return userService.rePassword(password);
    }

    //更新用户资料
    @PostMapping("/me/infoUpdate")
    public HttpResult upInfo(@RequestBody TUserInfo userInfo){
        boolean flag = tUserInfoService.update().update(userInfo);
        if(!flag){
            return HttpResult.fail("服务器错误");
        }
        return HttpResult.success("修改成功");
    }


    @GetMapping("/other/star")
    public HttpResult otherStar(@RequestParam(name = "otherId") Long otherId,
                                @RequestParam(name= "pageNum") Integer pageNum){
        return userService.getOtherStar(otherId,pageNum);
    }
    //获取用户
    @GetMapping("/other/tasks")
    public HttpResult otherStar(@RequestParam(name = "otherId") Long otherId,
                                @RequestParam(name= "pageNum") Integer pageNum,
                                @RequestParam(name="sortKey") Integer sortKey){
        return tasksService.getOtherTasksBySortKey(otherId,pageNum,sortKey);
    }

    //注销用户
    @DeleteMapping("/me")
    public HttpResult deleteUser() {
        //实现注销用户的逻辑
        Long userId = AuthenticationUtils.getId();
        boolean isTrue = userService.removeById(userId);
        if (!isTrue){
            return HttpResult.builder().code(ErrorCodeEnum.FAIL.code).msg("删除失败").build();
        }
        return HttpResult.builder().code(ErrorCodeEnum.SUCCESS.code).msg("删除成功").build();
    }

    // 获取用户好友列表
    @GetMapping("/friends")
    public HttpResult getUserFriends() {
        //  根据 userId 获取用户好友列表
        return friendService.getFriends();
    }

    //添加好友
    @PostMapping("/friends")
    public HttpResult addFriend(@RequestParam("otherUserId") String otherUserId){
        HttpResult addFlag = friendService.addFriend(otherUserId);
        if(addFlag.getCode().equals(1)){
            return HttpResult.fail(addFlag.getMsg());
        }
        communicationsService.sendChatMessage("添加好友啦，快来聊天吧！",otherUserId);
        return HttpResult.success("添加成功");
    }

    //删除好友
    @PostMapping("/defriend")
    public HttpResult deFriend(@RequestParam("otherUserId") String otherUserId){
        HttpResult deFlag = friendService.deFriend(otherUserId);
        if(deFlag.getCode().equals(1)){
            return HttpResult.fail("删除失败");
        }
        communicationsService.sendChatMessage("对方已经不是您的好友",otherUserId);
        return HttpResult.success("删除成功");
    }

    // 检测用户凭证资质
    @GetMapping("/checkCredential")
    public HttpResult checkCredential() {
        // TODO: 实现逻辑
        return HttpResult.builder().msg("暂未开通业务").build();
    }

    // 用户获取已接受任务
    @GetMapping("/getAcceptedTasks")
    public HttpResult getAcceptedTasks(@RequestParam Integer pageNum) {
        return tasksService.getOtherTasksBySortKey(null,TaskEnum.UNFINISH.state,pageNum);
    }
    //修改头像
    @PostMapping("/avatar")
    public HttpResult setAvatar(@RequestParam("url") String url){
        return userService.updateAavatar(url);
    }

}
