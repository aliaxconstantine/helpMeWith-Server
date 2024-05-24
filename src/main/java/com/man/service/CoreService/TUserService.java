package com.man.service.CoreService;

import com.baomidou.mybatisplus.extension.service.IService;
import com.man.dto.HttpResult;
import com.man.dto.LoginForm;
import com.man.dto.UserFrom;
import com.man.entity.core.TUser;
import jakarta.servlet.http.HttpSession;

/**
* @author 艾莉希雅
* @description 针对表【t_user】的数据库操作Service
* @createDate 2023-08-20 10:37:10
*/
public interface TUserService extends IService<TUser> {
    HttpResult sendCode(String phone, HttpSession session);


    HttpResult sendCode(String nickName);

    boolean Login(LoginForm loginForm);


    TUser getByPhone(String phone);

    HttpResult register(TUser tUser);

    TUser createTUserWithPhone(String phone);

    HttpResult getOtherById(String otherId);

    HttpResult getMe(Boolean ifFrom);

    HttpResult updateUser(UserFrom userFrom);

    HttpResult getStar(Integer pageNum);

    HttpResult getOtherStar(Long otherId, Integer pageNum);

    HttpResult rePassword(String password);

    HttpResult updateAavatar(String url);

    HttpResult getAllUser();

    HttpResult delectUser(String userId);
}
