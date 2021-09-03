package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.msg.service.MsgService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MsgHandler {

    @DubboReference
    MsgService msgService;
    @Resource
    SendDataToUserHandler sendDataToUserHandler;

    public void sendMsg(MsgData msgData){
        sendDataToUserHandler.sendAckToUser(msgData.getFromUserId(),"Ws Server receive msg");
        msgService.handlerMsgData(msgData);
    }
}
