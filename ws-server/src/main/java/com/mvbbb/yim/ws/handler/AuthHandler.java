package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.auth.AuthService;
import com.mvbbb.yim.common.protoc.request.AuthRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AuthHandler {

    @DubboReference
    AuthService authService;

    public boolean auth(AuthRequest data) {
        String userId = data.getUserId();
        String token = data.getToken();
        boolean tokenValid = authService.checkToken(userId, token);
        return tokenValid;
    }
}
