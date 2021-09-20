package com.mvbbb.yim.gateway.controller;

import com.mvbbb.yim.auth.service.AuthService;
import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.common.protoc.AuthEnum;
import com.mvbbb.yim.common.protoc.http.ResCode;
import com.mvbbb.yim.common.protoc.http.request.AuthRequest;
import com.mvbbb.yim.common.protoc.http.request.GenericRequest;
import com.mvbbb.yim.common.protoc.http.request.LoginRequest;
import com.mvbbb.yim.common.protoc.http.request.RegisterRequest;
import com.mvbbb.yim.common.protoc.http.response.AuthWsInfoResponse;
import com.mvbbb.yim.common.protoc.http.response.GenericResponse;
import com.mvbbb.yim.common.protoc.http.response.RegisterResponse;
import com.mvbbb.yim.common.vo.UserVO;
import com.mvbbb.yim.gateway.CheckAuth;
import com.mvbbb.yim.gateway.service.RegisterService;
import com.mvbbb.yim.logic.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 使用注解对 接口进行权限校验
 */
@RestController
@CheckAuth
public class UserController {

    @DubboReference(check = false)
    AuthService authService;
    @DubboReference(check = false)
    UserService userService;
    @Resource
    RegisterService zkService;

    @ApiOperation("登录")
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @CheckAuth(check = false)
    public GenericResponse<AuthWsInfoResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.checkLogin(loginRequest.getUserId(), loginRequest.getPassword());
        if (token == null) {
            return GenericResponse.failed(ResCode.FAILED);
        }
        String wsUrl = zkService.getWsForUser();
        AuthWsInfoResponse authWsInfoResponse = new AuthWsInfoResponse();
        authWsInfoResponse.setUserId(loginRequest.getUserId());
        authWsInfoResponse.setToken(token);
        authWsInfoResponse.setWsUrl(wsUrl);
        return GenericResponse.success(authWsInfoResponse);
    }

    @ApiOperation("重新获取 ws 服务器地址")
    @RequestMapping(path = "/reconnect", method = RequestMethod.POST)
    public GenericResponse<String> reconnect(@RequestHeader String userId,@RequestHeader String token) {
        String wsUrl = zkService.getWsForUser();
        return GenericResponse.success(wsUrl);
    }

    @ApiOperation("注册")
    @CheckAuth(check = false)
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public GenericResponse<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        User user = authService.register(registerRequest.getUsername(), registerRequest.getPassword());
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUserId(user.getUserId());
        registerResponse.setAvatar(user.getAvatar());
        registerResponse.setUsername(user.getUsername());
        return GenericResponse.success(registerResponse);
    }


    @ApiOperation("退出登录")
    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public GenericResponse<Object> logout(@RequestHeader String userId,@RequestHeader String token) {
        AuthEnum authRes = authService.logout(userId,token);
        return authRes == AuthEnum.SUCCESS ? GenericResponse.success() : GenericResponse.failed(ResCode.FAILED);
    }

    @ApiOperation("查看所有 user")
    @RequestMapping(path = "/user/all", method = RequestMethod.GET)
    public GenericResponse<List<UserVO>> allUser(@RequestHeader String userId,@RequestHeader String token) {
        List<UserVO> users = userService.listAllUser();
        return GenericResponse.success(users);
    }


    @ApiOperation("获取 user 信息")
    @RequestMapping(path = "/user/info", method = RequestMethod.GET)
    public GenericResponse<UserVO> userInfo(@RequestHeader String userId, @RequestHeader String token, @RequestParam String queryUid) {
        UserVO userInfo = userService.getUserInfo(queryUid);
        return GenericResponse.success(userInfo);
    }

    @ApiOperation("更新头像")
    @RequestMapping(path = "/user/avatar/update", method = RequestMethod.POST)
    public GenericResponse<Object> userAvatarUpdate(@RequestHeader String userId,@RequestHeader String token,  @RequestBody GenericRequest<String> request) {
        String avatar = request.getData();
        userService.updateUserAvatar(userId, avatar);
        return GenericResponse.success();
    }

    @ApiOperation("更新昵称")
    @RequestMapping(path = "/user/name/update", method = RequestMethod.POST)
    public GenericResponse<Object> userNameUpdate(@RequestHeader String userId, @RequestHeader String token, @RequestBody GenericRequest<String> request) {
        String username = request.getData();
        userService.updateUserName(userId, username);
        return GenericResponse.success();
    }
}
