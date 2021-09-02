package com.mvbbb.yim.logic.service;

import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.protoc.http.response.PullOfflineMsgResponse;

import java.util.List;

public interface MsgService {
    List<MsgData> getHistoryMsg(String userId, String sessionId, SessionType sessionType);
    PullOfflineMsgResponse getOfflineMsg(String userId);
}
