package com.mvbbb.yim.logic.service;

import com.mvbbb.yim.common.WsServerRoute;
import com.mvbbb.yim.common.entity.User;

import java.util.List;

public interface UserService {

    List<User> listAllUser();

    User getUserInfo(String userId);

    User updateUserAvatar(String userId,String avatar);

    User updateUserName(String userId,String username);
}
