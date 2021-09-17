package com.mvbbb.yim.common.util;

import com.mvbbb.yim.common.entity.MsgRecv;
import com.mvbbb.yim.common.entity.MsgSend;
import com.mvbbb.yim.common.protoc.Ack;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.Protobuf;
import com.mvbbb.yim.common.protoc.ws.MsgType;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.protoc.ws.request.ByeRequest;
import com.mvbbb.yim.common.protoc.ws.request.GreetRequest;
import com.mvbbb.yim.common.vo.MsgVO;

import java.util.Date;

public final class BeanConvertor {

    @Deprecated
    public static MsgData msgSendToMsgData(String userId, MsgSend msgSend, SessionType sessionType) {
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

    public static MsgVO msgSendToMsgVO(MsgSend msgSend) {
        MsgVO msgVO = new MsgVO();
        msgVO.setClientMsgId(msgSend.getClientMsgId());
        msgVO.setServerMsgId(String.valueOf(msgSend.getServerMsgId()));
        msgVO.setFromUid(msgSend.getFromUid());
        msgVO.setGroupId(msgSend.getGroupId());
        msgVO.setSessionType(SessionType.getType(msgSend.getSessionType()));
        msgVO.setMsgType(MsgType.getType(msgSend.getMsgType()));
        msgVO.setMsgData(msgSend.getMsgData());
        msgVO.setTimestamp(msgSend.getTimestamp());
        return msgVO;
    }

    public static MsgVO msgRecvToMsgVO(MsgRecv msgRecv) {
        MsgVO msgVO = new MsgVO();
        msgVO.setClientMsgId(msgRecv.getClientMsgId());
        msgVO.setServerMsgId(String.valueOf(msgRecv.getServerMsgId()));
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
        msgSend.setToUid(msgData.getSessionType() == SessionType.SINGLE ? msgData.getToSessionId() : null);
        msgSend.setGroupId(msgData.getSessionType() == SessionType.GROUP ? msgData.getToSessionId() : null);
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
        msgRecv.setGroupId(msgData.getSessionType() == SessionType.GROUP ? msgData.getToSessionId() : null);
        msgRecv.setMsgType(msgData.getMsgType() == MsgType.TEXT ? 0 : 1);
        msgRecv.setTimestamp(msgData.getTimestamp());
        msgRecv.setMsgData(msgData.getData());
        msgRecv.setDeleted(false);
        return msgRecv;
    }

    public static GreetRequest protocToGreetRequest(Protobuf.Greet greet) {
        GreetRequest greetRequest = new GreetRequest();
        greetRequest.setUserId(greet.getUserId());
        greetRequest.setToken(greet.getToken());
        ;
        return greetRequest;
    }

    public static MsgData protocToMsgData(Protobuf.MsgData dataPacket) {
        MsgData msgData = new MsgData();
        msgData.setClientMsgId(dataPacket.getClientMsgId());
        msgData.setServerMsgId(dataPacket.getServerMsgId());
        msgData.setFromUserId(dataPacket.getFromUserId());
        Protobuf.SessionType sessionType = dataPacket.getSessionType();
        switch (sessionType) {
            case SINGLE:
                msgData.setSessionType(SessionType.SINGLE);
                break;
            case GROUP:
                msgData.setSessionType(SessionType.GROUP);
        }
        msgData.setToSessionId(dataPacket.getToSessionId());
        msgData.setRecvUserId(dataPacket.getRecvUserId());
        Protobuf.MsgType msgType = dataPacket.getMsgType();
        switch (msgType) {
            case TEXT:
                msgData.setMsgType(MsgType.TEXT);
                break;
            case FILE:
                msgData.setMsgType(MsgType.FILE);
                break;
        }
        msgData.setData(dataPacket.getData());
        msgData.setTimestamp(new Date(dataPacket.getTimestamp()));
        return msgData;
    }


    public static ByeRequest protocToBye(Protobuf.Bye bye) {
        return new ByeRequest();
    }

    public static Protobuf.Ack protocFromAck(Ack ack) {
        Protobuf.Ack.Builder ackBuilder = Protobuf.Ack.newBuilder();
        ackBuilder.setClientMsgId(ack.getClientMsgId());
        ackBuilder.setServerMsgId(ack.getServerMsgId());
        ackBuilder.setMsg(ack.getMsg());
        return ackBuilder.build();
    }

    public static Protobuf.MsgVO protoFromMsgData(MsgData msgData) {
        Protobuf.MsgVO.Builder msgVoBuilder = Protobuf.MsgVO.newBuilder();
        msgVoBuilder.setClientMsgId(msgData.getClientMsgId());
        msgVoBuilder.setServerMsgId(msgData.getServerMsgId());
        msgVoBuilder.setFromUid(msgData.getFromUserId());
        if (msgData.getSessionType() == SessionType.GROUP) {
            msgVoBuilder.setGroupId(msgData.getToSessionId());
        }
        switch (msgData.getSessionType()) {
            case GROUP:
                msgVoBuilder.setSessionType(Protobuf.SessionType.GROUP);
                msgVoBuilder.setGroupId(msgData.getToSessionId());
                break;
            case SINGLE:
                msgVoBuilder.setSessionType(Protobuf.SessionType.SINGLE);
                break;
            default:
                break;
        }
        msgVoBuilder.setData(msgData.getData());
        msgVoBuilder.setTimestamp(msgData.getTimestamp().getTime());
        return msgVoBuilder.build();
    }

    public static MsgVO msgDataToMsgVO(MsgData msgData) {
        MsgVO msgVO = new MsgVO();
        msgVO.setClientMsgId(msgData.getClientMsgId());
        msgVO.setServerMsgId(String.valueOf((msgData.getServerMsgId())));
        msgVO.setFromUid(msgData.getFromUserId());
        switch (msgData.getSessionType()) {
            case GROUP:
                msgVO.setSessionType(SessionType.GROUP);
                msgVO.setGroupId(msgData.getToSessionId());
                break;
            case SINGLE:
                msgVO.setSessionType(SessionType.SINGLE);
                break;
            default:
                break;
        }
        msgVO.setMsgType(msgData.getMsgType());
        msgVO.setMsgData(msgData.getData());
        msgVO.setTimestamp(msgData.getTimestamp());
        return msgVO;
    }
}
