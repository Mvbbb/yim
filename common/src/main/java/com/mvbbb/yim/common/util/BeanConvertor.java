package com.mvbbb.yim.common.util;

import com.mvbbb.yim.common.entity.MsgRecv;
import com.mvbbb.yim.common.entity.MsgSend;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.MsgType;
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

    public static MsgData msgRecvToMsgData(MsgRecv msgRecv) {
        MsgData msgData = new MsgData();
        msgData.setServerMsgId(msgRecv.getMsgId());
        msgData.setFromUserId(msgRecv.getMsgFrom());
        msgData.setSessionType(msgRecv.getGroupId()==null?SessionType.SINGLE:SessionType.GROUP);
        msgData.setToSessionId(msgRecv.getMsgTo());
        msgData.setRecvUserId(msgRecv.getMsgTo());
        msgData.setMsgType(msgRecv.getMsgType()==0? MsgType.TEXT:MsgType.FILE);
        msgData.setData(msgRecv.getMsgData());
        msgData.setTimestamp(msgRecv.getTimestamp());
        return msgData;
    }

    public static MsgSend msgDataToMsgSend(MsgData msgData) {
        MsgSend msgSend = new MsgSend();
        msgSend.setMsgId(msgData.getServerMsgId());
        msgSend.setClientMsgId(msgData.getClientMsgId());
        msgSend.setFromUid(msgData.getFromUserId());
        msgSend.setToUid(msgData.getSessionType()== SessionType.SINGLE?msgData.getToSessionId():null);
        msgSend.setGroupId(msgData.getSessionType()==SessionType.GROUP?msgData.getToSessionId():null);
        msgSend.setMsgType(msgSend.getMsgType());
        msgSend.setMsgData(msgData.getData());
        msgSend.setTimestamp(msgData.getTimestamp());
        return msgSend;
    }

    public static MsgRecv msgDataToMsgRecv(MsgData msgData) {

        MsgRecv msgRecv = new MsgRecv();
        msgRecv.setMsgId(msgData.getServerMsgId());
        msgRecv.setMsgFrom(msgData.getFromUserId());
        msgRecv.setMsgTo(msgData.getRecvUserId());
        msgRecv.setGroupId(msgData.getSessionType()==SessionType.GROUP?msgData.getToSessionId():null);
        msgRecv.setMsgType(msgData.getMsgType()== MsgType.TEXT?0:1);
        msgRecv.setTimestamp(msgData.getTimestamp());
        msgRecv.setMsgData(msgData.getData());
        msgRecv.setDeleted(false);
        return msgRecv;
    }
}
