package com.mvbbb.yim.common.protoc.http.request;

import com.mvbbb.yim.common.protoc.ws.SessionType;
import lombok.Data;

@Data
public class HistoryRequest {
    private SessionType sessionType;
    private int endMsgId; // 服务器消息id（不包含在查询结果中）
    private int limitCount; //本次拉取的消息条数上限
}
