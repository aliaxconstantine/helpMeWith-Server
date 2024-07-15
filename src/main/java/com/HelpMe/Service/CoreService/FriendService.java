package com.HelpMe.Service.CoreService;

import com.HelpMe.dto.HttpResult;
import com.HelpMe.Entity.core.Friend;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 艾莉希雅
* @description 针对表【friend】的数据库操作Service
* @createDate 2023-10-13 15:24:54
*/
public interface FriendService extends IService<Friend> {

    boolean isFriend(String myId, String friendId);

    HttpResult getFriends();

    HttpResult addFriend(String otherUserId);

    HttpResult deFriend(String otherUserId);

}
