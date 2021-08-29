package com.mvbbb.yim.common.protoc;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OfflineSessionMsg {
    private String sessionId;
    private int cnt;
    private List<MsgData> msgs;
    private Date timestamp; // 当前会话的最后一条消息的时间戳
}
