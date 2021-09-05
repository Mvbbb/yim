package com.mvbbb.yim.common.util;

import com.mvbbb.yim.common.entity.MsgRecv;
import com.mvbbb.yim.common.entity.MsgSend;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.MsgType;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.vo.MsgVO;

public final class BeanConvertor {

    @Deprecated
    public static MsgData msgSendToMsgData(String userId,MsgSend msgSend, SessionType sessionType){
        MsgData msgData = new MsgData();
        msgData.setClientMsgId(msgSend.getClientMsgId());
        msgData.setServerMsgId(msgSend.getServerMsgId());
        msgData.setFromUserId(msgSend.getFromUid());
        msgData.setSessionType(sessionType);
        msgData.setToSessionId(userId);
        msgData.setRecvUserId(userId);
        msgData.setSessionType(sessionType);
        msgData.setData(msgSend.getMsgData());
        msgData.setTimestamp(msgSend.getTimestamp());
        return msgData;
    }

    public static MsgVO msgSendToMsgVO(MsgSend msgSend){
        MsgVO msgVO = new MsgVO();
        msgVO.setClientMsgId(msgSend.getClientMsgId());
        msgVO.setServerMsgId(msgSend.getServerMsgId());
        msgVO.setFromUid(msgSend.getFromUid());
        msgVO.setGroupId(msgSend.getGroupId());
        msgVO.setSessionType(SessionType.getType(msgSend.getSessionType()));
        msgVO.setMsgType(MsgType.getType(msgSend.getMsgType()));
        msgVO.setMsgData(msgSend.getMsgData());
        msgVO.setTimestamp(msgSend.getTimestamp());
        return msgVO;
    }

    public static MsgVO msgRecvToMsgVO(MsgRecv msgRecv){
        MsgVO msgVO = new MsgVO();
        msgVO.setClientMsgId(msgRecv.getClientMsgId());
        msgVO.setServerMsgId(msgRecv.getServerMsgId());
        msgVO.setFromUid(msgRecv.getFromUid());
        msgVO.setGroupId(msgRecv.getGroupId());
        msgVO.setSessionType(SessionType.getType(msgRecv.getSessionType()));
        msgVO.setMsgType(MsgType.getType(msgRecv.getMsgType()));
        msgVO.setMsgData(msgRecv.getMsgData());
        msgVO.setTimestamp(msgRecv.getTimestamp());
        return msgVO;
    }


    public static MsgSend msgDataToMsgSend(MsgData msgData) {
        MsgSend msgSend = new MsgSend();
        msgSend.setServerMsgId(msgData.getServerMsgId());
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
        msgRecv.setClientMsgId(msgData.getClientMsgId());
        msgRecv.setServerMsgId(msgData.getServerMsgId());
        msgRecv.setFromUid(msgData.getFromUserId());
        msgRecv.setToUid(msgData.getRecvUserId());
        msgRecv.setGroupId(msgData.getSessionType()==SessionType.GROUP?msgData.getToSessionId():null);
        msgRecv.setMsgType(msgData.getMsgType()== MsgType.TEXT?0:1);
        msgRecv.setTimestamp(msgData.getTimestamp());
        msgRecv.setMsgData(msgData.getData());
        msgRecv.setDeleted(false);
        return msgRecv;
    }
}
