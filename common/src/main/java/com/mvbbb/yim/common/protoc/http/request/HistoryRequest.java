package com.mvbbb.yim.common.protoc.http.request;

import com.mvbbb.yim.common.protoc.ws.SessionType;
import lombok.Data;

@Data
public class HistoryRequest {

    private SessionType sessionType;
    private String sessionId;
    private int from;
    private int limit; //本次拉取的消息条数上限
}
