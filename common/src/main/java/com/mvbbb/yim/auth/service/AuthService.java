package com.mvbbb.yim.auth.service;

import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.common.protoc.AuthEnum;

public interface AuthService {
    AuthEnum checkToken(String userId, String token);
    String checkLogin(String userId,String password);
    AuthEnum changePassword(String userId,String oldPassword,String newPassword);
    User register(String username, String password);
    AuthEnum logout(String userId,String token);
    String echo();
}
