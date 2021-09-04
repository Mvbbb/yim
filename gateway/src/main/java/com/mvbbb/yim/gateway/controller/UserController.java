package com.mvbbb.yim.gateway.controller;

import com.mvbbb.yim.auth.service.AuthService;
import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.common.protoc.AuthEnum;
import com.mvbbb.yim.common.protoc.http.ResCode;
import com.mvbbb.yim.common.protoc.http.request.GenericRequest;
import com.mvbbb.yim.common.protoc.http.response.GenericResponse;
import com.mvbbb.yim.common.protoc.http.request.LoginRequest;
import com.mvbbb.yim.common.protoc.http.request.RegisterRequest;
import com.mvbbb.yim.common.protoc.http.response.AuthResponse;
import com.mvbbb.yim.common.protoc.http.response.RegisterResponse;
import com.mvbbb.yim.logic.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class UserController {

    @DubboReference(check = false)
    AuthService authService;
    @DubboReference(check = false)
    UserService userService;

    // todo 提高粒度
    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public GenericResponse<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        String token = authService.checkLogin(loginRequest.getUserId(), loginRequest.getPassword());
        if(token==null){
            return GenericResponse.failed(ResCode.FAILED);
        }
        AuthResponse authResponse = new AuthResponse();
        authResponse.setUserId(loginRequest.getUserId());
        authResponse.setToken(token);
        return GenericResponse.success(authResponse);
    }

    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public GenericResponse<RegisterResponse> register(@RequestBody RegisterRequest registerRequest){
        User user = authService.register(registerRequest.getUsername(), registerRequest.getPassword());
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUserId(user.getUserId());
        registerResponse.setAvatar(user.getAvatar());
        registerResponse.setUsername(user.getUsername());
        return GenericResponse.success(registerResponse);
    }


    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public GenericResponse<Object> logout(@RequestParam GenericRequest<Object> request) {
        AuthEnum authRes = authService.logout(request.getUserId(), request.getToken());
        return authRes==AuthEnum.SUCCESS?GenericResponse.success():GenericResponse.failed(ResCode.FAILED);
    }


    @RequestMapping(path = "/user/all",method = RequestMethod.GET)
    public GenericResponse<List<User>> allUser(){
        List<User> users = userService.listAllUser();
        return GenericResponse.success(users);
    }


    @RequestMapping(path = "/user/info",method = RequestMethod.GET)
    public GenericResponse<User> userInfo(@RequestBody GenericRequest<String > request){
        String userId = request.getData();
        User userInfo = userService.getUserInfo(userId);
        return GenericResponse.success(userInfo);
    }

    @RequestMapping(path = "/user/avatar/update",method = RequestMethod.POST)
    public GenericResponse<Object> userAvatarUpdate(@RequestBody GenericRequest<String> request){
        String userId = request.getUserId();
        String avatar = request.getData();
        userService.updateUserAvatar(userId,avatar);
        return GenericResponse.success();
    }

    @RequestMapping(path = "/user/name/update",method = RequestMethod.POST)
    public GenericResponse<Object> userNameUpdate(@RequestBody GenericRequest<String> request){
        String userId = request.getUserId();
        String username = request.getData();
        userService.updateUserName(userId,username);
        return GenericResponse.success();
    }
}
