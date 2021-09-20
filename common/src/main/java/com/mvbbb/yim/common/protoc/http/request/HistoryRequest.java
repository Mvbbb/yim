package com.mvbbb.yim.common.protoc.http.request;

import com.mvbbb.yim.common.protoc.ws.SessionType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Deprecated
@Data
public class HistoryRequest {
    @NotNull
    private SessionType sessionType;
    @NotNull
    private String sessionId;
    @NotNull
    private int from;
    @NotNull
    private int limit; //本次拉取的消息条数上限
}
