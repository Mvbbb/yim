package com.mvbbb.yim.gateway.controller;

import com.mvbbb.yim.auth.AuthService;
import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.logic.service.MsgService;
import com.mvbbb.yim.logic.service.RelationService;
import com.mvbbb.yim.logic.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @DubboReference
    AuthService authService;

    @DubboReference
    UserService userService;

    @DubboReference
    MsgService msgService;

    @DubboReference
    RelationService relationService;

    @RequestMapping(path = "/auth/echo",method = RequestMethod.GET)
    public String echoAuthServer(){
        return authService.echo();
    }

    @RequestMapping(path = "/logic/echo",method = RequestMethod.GET)
    public String echoLogicServer(){
        User userInfo = userService.getUserInfo("1");
        return userInfo.toString();
    }
}
