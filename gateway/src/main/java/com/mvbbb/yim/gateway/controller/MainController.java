package com.mvbbb.yim.gateway.controller;

import com.mvbbb.yim.auth.service.AuthService;
import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.logic.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MainController {

    @DubboReference(check = false)
    AuthService authService;

    @DubboReference(check = false)
    UserService userService;

    @RequestMapping(path = "/auth/echo",method = RequestMethod.GET)
    @ApiOperation("测试 auth 服务是否联通")
    public String echoAuthServer(){
        return authService.echo();
    }

    @RequestMapping(path = "/logic/echo",method = RequestMethod.GET)
    @ApiOperation("测试 logic 服务是否联通")
    public String echoLogicServer(){
        User userInfo = userService.getUserInfo("1");
        return userInfo.toString();
    }
}
