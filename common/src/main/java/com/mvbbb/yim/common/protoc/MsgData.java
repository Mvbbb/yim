package com.mvbbb.yim.common.protoc;

import com.mvbbb.yim.common.protoc.ws.MsgType;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class MsgData implements Serializable {
    private String clientMsgId;
    private long serverMsgId;
    private String fromUserId;
    private SessionType sessionType;
    private String groupId;
    private String toUserId;
    private MsgType msgType;
    private String data;
    private Date timestamp;
}
