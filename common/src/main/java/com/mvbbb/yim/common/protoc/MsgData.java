package com.mvbbb.yim.common.protoc;

import com.mvbbb.yim.common.protoc.ws.MsgType;
import lombok.Data;
import com.mvbbb.yim.common.protoc.ws.SessionType;

import java.io.Serializable;
import java.util.Date;

@Data
public class MsgData implements Serializable {
    private String clientMsgId;
    private long serverMsgId;
    private String fromUserId;
    private SessionType sessionType;
    private String toSessionId;
    private String recvUserId;
    private MsgType msgType;
    private String data;
    private Date timestamp;
}
