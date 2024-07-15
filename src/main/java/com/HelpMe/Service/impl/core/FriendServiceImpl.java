package com.HelpMe.Service.impl.core;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.HelpMe.dto.ErrorCodeEnum;
import com.HelpMe.dto.FriendEnum;
import com.HelpMe.dto.HttpResult;
import com.HelpMe.Entity.core.Friend;
import com.HelpMe.Entity.core.TUser;
import com.HelpMe.mapper.CommunicationsMapper;
import com.HelpMe.mapper.FriendMapper;
import com.HelpMe.mapper.UserMapper;
import com.HelpMe.Service.CoreService.FriendService;
import com.HelpMe.utils.AuthenticationUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;

/**
* @author 艾莉希雅
* @description 针对表【friend】的数据库操作Service实现
* @createDate 2023-10-13 15:24:54
*/
@Service
@Log4j2
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {
    private final FriendMapper friendMapper;
    private final UserMapper userMapper;
    private final CommunicationsMapper communicationsMapper;
    @Autowired
    public FriendServiceImpl(FriendMapper friendMapper, UserMapper userMapper, CommunicationsMapper communicationsMapper) {
        this.friendMapper = friendMapper;
        this.userMapper = userMapper;
        this.communicationsMapper = communicationsMapper;
    }

    //添加好友
    @Transactional
    @Override
    public HttpResult addFriend(String otherUserId){
        if (ifUserId(otherUserId)) return HttpResult.fail("请输入正确的id");
        //如果已经是好友
        if(isFriend(AuthenticationUtils.getId().toString(),otherUserId)){
           return HttpResult.fail("已经添加为好友");
        }
        //添加好友
        Long id = Long.parseLong(otherUserId);
        Friend friend = Friend.builder()
                .friendId(id)
                .userId(AuthenticationUtils.getId())
                .status(FriendEnum.FRIEND.state)
                .createTime(new Timestamp(System.currentTimeMillis()))
                .build();
        boolean flag = save(friend);
        //保证数据一致性
        if(!flag){
            return HttpResult.builder().code(ErrorCodeEnum.FAIL.code).msg("服务器异常").build();
        }
        //如果是单向好友
        if(isFriend(otherUserId,AuthenticationUtils.getId().toString())){
            return HttpResult.success("添加成功");
        }
        //如果不是单向好友
        //对方添加好友
        Friend rfriend = Friend.builder()
                .friendId(AuthenticationUtils.getId())
                .userId(id)
                .status(FriendEnum.FRIEND.state)
                .createTime(new Timestamp(System.currentTimeMillis()))
                .build();
        boolean rFlag = save(rfriend);
        return (rFlag)?HttpResult.success("添加成功"):HttpResult.fail("服务器异常");
    }

    private boolean ifUserId(String otherUserId) {
        //检查该id是否为userid
        QueryWrapper<TUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", otherUserId);
        TUser user = userMapper.selectOne(userQueryWrapper);
        return ObjectUtils.isEmpty(user);
    }

    @Override
    public HttpResult deFriend(String otherUserId) {
        if (ifUserId(otherUserId)) {
            return HttpResult.fail("请输入正确的id");
        }
        Friend friend = query().eq("user_id", AuthenticationUtils.getId()).eq("friend_id", otherUserId).one();
        if(friend == null){
            return HttpResult.fail("你还不是对方好友");
        }
        boolean flag = (boolean) removeById(friend);
        return (flag)?HttpResult.success("删除成功"):HttpResult.fail("服务器异常");
    }

    @Override
    public boolean isFriend(String myId, String friendId) {
        Friend friend = query().eq("user_id",myId).eq("friend_id",friendId).one();
        return friend != null;
    }

    @Override
    public HttpResult getFriends() {
        //获取好友列表，按照时间顺序
        var friendList = friendMapper.getByFriendIdFriendList(AuthenticationUtils.getId());
        //将所有好友返回
        friendList.forEach(f ->{
            QueryWrapper<TUser> wrapper = new QueryWrapper<>();
            wrapper.eq("id",f.getFriendId());
            var friend = userMapper.selectOne(wrapper);
            f.setUserName(friend.getNickName());
            f.setUserIcon(friend.getAchUrl());
        });
        return HttpResult.builder().data(friendList).msg("获取成功").code(ErrorCodeEnum.SUCCESS.code).build();
    }

}




