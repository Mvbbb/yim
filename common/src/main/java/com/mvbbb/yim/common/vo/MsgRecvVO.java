package com.mvbbb.yim.common.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MsgRecvVO {
    private long id;
    private long msgId;
    private String msgFrom;
    private String msgTo;
    private String groupId;
    private int msgType;
    private String msgData;
    private Date timestamp;
}
