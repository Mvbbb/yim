package com.mvbbb.yim.common.vo;

import com.mvbbb.yim.common.protoc.ws.MsgType;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务器向用户推送的消息
 */
@Data
public class MsgVO implements Serializable {
    private String clientMsgId;
    private long serverMsgId;
    private String fromUid;
    private String groupId;
    private SessionType sessionType;
    private MsgType msgType;
    private String msgData;
    private Date timestamp;
}