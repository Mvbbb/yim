package com.mvbbb.yim.common.protoc.response;

import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.enums.SessionType;
import lombok.Data;

import java.util.List;

@Data
public class HistoryResponse {
    private String userId;
    private SessionType sessionType;
    private String sessionId;
    private List<MsgData> msgs;
}
