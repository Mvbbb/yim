package com.mvbbb.yim.common.protoc;

import com.mvbbb.yim.common.protoc.enums.MsgType;
import lombok.Data;
import com.mvbbb.yim.common.protoc.enums.SessionType;

import java.util.Date;

@Data
public class MsgData {
    private String clientMsgId;
    private String serverMsgId;
    private String fromUserId;
    private SessionType sessionType;
    private String toSessionId;
    private MsgType msgType;
    private String data;
    private Date timestamp;
}
