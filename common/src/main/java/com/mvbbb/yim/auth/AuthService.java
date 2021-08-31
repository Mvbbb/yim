package com.mvbbb.yim.auth;

import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.common.protoc.request.LoginRequest;

public interface AuthService {
    String genToken(String userId);
    boolean checkToken(String userId,String token);
    String checkLogin(String userId,String password);
    boolean changePassword(String username,String oldPassword,String newPassword);
    User register(String username, String password);

    String echo();
}
