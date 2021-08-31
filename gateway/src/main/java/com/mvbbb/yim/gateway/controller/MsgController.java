package com.mvbbb.yim.gateway.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class MsgController {
    @RequestMapping(path = "/message/history",method = RequestMethod.GET)
    public void getHistoryMsg(){

    }

    @RequestMapping(path = "/message/group-history",method = RequestMethod.GET)
    public void getGroupHistroyMsg(){

    }

    @RequestMapping(path = "/message/offline", method = RequestMethod.GET)
    public void getOfflineMsg(){

    }
}
