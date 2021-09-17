package com.mvbbb.yim.ws.service;

import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.util.SnowflakeIdWorker;
import com.mvbbb.yim.msg.service.MsgService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 将消息传递到 msg server 模块
 */
@Component
public class MsgTransfer {

    @DubboReference(check = false)
    MsgService msgService;
    @Resource
    MsgSendService sendDataToUserHandler;

    public void sendMsg(MsgData msgData) {
        msgData.setServerMsgId(SnowflakeIdWorker.generateId());
        sendDataToUserHandler.sendAckToUser(msgData.getFromUserId(), "Ws Server receive msg.");
        msgService.handlerMsgData(msgData);
    }
}
