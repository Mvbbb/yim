package com.mvbbb.yim.common.protoc;

import com.mvbbb.yim.common.protoc.enums.ResCode;
import com.mvbbb.yim.common.protoc.enums.SessionType;
import lombok.Data;

import java.util.Date;

@Data
public class MsgDataAck {
    private String fromUserId;
    private String toSessionId;
    private String clientMsgId;
    private ResCode resCode;
    private SessionType sessionType;
    private String errMsg;
    private Date timestamp;
}
