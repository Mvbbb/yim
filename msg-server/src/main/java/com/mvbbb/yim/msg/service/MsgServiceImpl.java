package com.mvbbb.yim.msg.service;

import com.mvbbb.yim.common.entity.MsgSend;
import com.mvbbb.yim.common.mapper.MsgSendMapper;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.util.BeanConvertor;
import com.mvbbb.yim.msg.MsgHandler;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class MsgServiceImpl implements MsgService {

    @Resource
    MsgHandler msgHandler;
    @Resource
    MsgSendMapper msgSendMapper;

    @Override
    public void handlerMsgData(MsgData msgData) {
        // 持久化消息
        MsgSend msgSend = BeanConvertor.msgDataToMsgSend(msgData);
        msgSendMapper.insert(msgSend);
        // 发送消息
        switch (msgData.getSessionType()){
            case SINGLE:
                msgData.setRecvUserId(msgData.getToSessionId());
                msgHandler.sendSingleMsg(msgData);
                break;
            case GROUP:
                msgHandler.sendGroupMsg(msgData);
                break;
            default:
                break;
        }
    }

    public void failedMsg(MsgData msgData){

    }
}
