package com.mvbbb.yim.gateway.controller;

import com.mvbbb.yim.common.protoc.http.request.GenericRequest;
import com.mvbbb.yim.common.protoc.http.response.GenericResponse;
import com.mvbbb.yim.common.protoc.http.response.PullOfflineMsgResponse;
import com.mvbbb.yim.logic.service.MsgService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
public class MsgController {

    @DubboReference
    MsgService msgService;

    // TODO
    @RequestMapping(path = "/message/history",method = RequestMethod.GET)
    public void getHistoryMsg(){

    }

    @RequestMapping(path = "/message/offline", method = RequestMethod.GET)
    public GenericResponse<PullOfflineMsgResponse> getOfflineMsg(@RequestBody GenericRequest<String> request){
        String userId = request.getData();
        PullOfflineMsgResponse offlineMsg = msgService.getOfflineMsg(userId);
        return GenericResponse.success(offlineMsg);
    }
}
