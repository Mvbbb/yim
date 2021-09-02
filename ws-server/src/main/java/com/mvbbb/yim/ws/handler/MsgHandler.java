package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.msg.service.MsgService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

@Component
public class MsgHandler {

    @DubboReference
    MsgService msgService;

    public void sendMsg(MsgData msgData){
        msgService.handlerMsgData(msgData);
    }
}
