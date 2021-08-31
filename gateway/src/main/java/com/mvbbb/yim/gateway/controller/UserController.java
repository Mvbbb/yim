package com.mvbbb.yim.gateway.controller;

import com.mvbbb.yim.auth.AuthService;
import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.common.protoc.DataPacket;
import com.mvbbb.yim.common.protoc.enums.AuthResponseEnum;
import com.mvbbb.yim.common.protoc.request.LoginRequest;
import com.mvbbb.yim.common.protoc.request.RegisterRequest;
import com.mvbbb.yim.common.protoc.response.AuthResponse;
import com.mvbbb.yim.common.protoc.response.RegisterResponse;
import com.mvbbb.yim.logic.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class UserController {

    @DubboReference
    AuthService authService;
    @DubboReference
    UserService userService;

    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public DataPacket<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        String token = authService.checkLogin(loginRequest.getUserId(), loginRequest.getPassword());
        DataPacket<AuthResponse> authResponseDataPacket = new DataPacket<>();
        AuthResponse authResponse = new AuthResponse();
        if(token==null){
            authResponse.setStatus(AuthResponseEnum.ERROR);
            authResponse.setErrMsg("用户名不存在或者密码错误");
        }else{
            authResponse.setUserId(loginRequest.getUserId());
            authResponse.setToken(token);
        }
        authResponseDataPacket.setData(authResponse);
        return authResponseDataPacket;
    }

    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public DataPacket<RegisterResponse> register(@RequestBody RegisterRequest registerRequest){
        User user = authService.register(registerRequest.getUsername(), registerRequest.getPassword());
        DataPacket<RegisterResponse> dataPacket = new DataPacket<>();
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUserId(user.getUserId());
        registerResponse.setAvatar(user.getAvatar());
        registerResponse.setUsername(user.getUsername());
        dataPacket.setData(registerResponse);
        return dataPacket;
    }


    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public void logout(@RequestParam String userId) {

    }


    @RequestMapping(path = "/user/all",method = RequestMethod.GET)
    public void allUser(){

        List<User> users = userService.listAllUser();

    }


    @RequestMapping(path = "/user/info",method = RequestMethod.GET)
    public void userInfo(){
        String userId = null;
        User userInfo = userService.getUserInfo(userId);

    }

    @RequestMapping(path = "/user/avatar/update",method = RequestMethod.POST)
    public void userAvatarUpdate(){
        String userId = null;
        String avatar = null;
        userService.updateUserAvatar(userId,avatar);
    }

    @RequestMapping(path = "/user/name/update",method = RequestMethod.POST)
    public void userNameUpdate(){
        String userId = null;
        String username = null;
        userService.updateUserName(userId,username);
    }
}
