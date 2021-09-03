package com.mvbbb.yim.common.util;

import com.mvbbb.yim.common.entity.MsgSend;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.SessionType;

public final class BeanConvertor {

    public static MsgData msgSendToMsgData(String userId,MsgSend msgSend, SessionType sessionType){
        MsgData msgData = new MsgData();
        msgData.setClientMsgId(msgSend.getClientMsgId());
        msgData.setServerMsgId(msgSend.getMsgId());
        msgData.setFromUserId(msgSend.getFromUid());
        msgData.setSessionType(sessionType);
        msgData.setToSessionId(userId);
        msgData.setRecvUserId(userId);
        msgData.setSessionType(sessionType);
        msgData.setData(msgSend.getMsgData());
        msgData.setTimestamp(msgSend.getTimestamp());
        return msgData;
    }
}
