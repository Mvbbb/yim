package com.mvbbb.yim.common.protoc.http;

import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class OfflineSessionMsg implements Serializable {
    private String sessionId;
    private SessionType sessionType;
    private int cnt;
    private List<MsgData> msgs;
    private Date timestamp; // 当前会话的最后一条消息的时间戳
}
