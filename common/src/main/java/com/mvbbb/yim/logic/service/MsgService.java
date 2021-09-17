package com.mvbbb.yim.logic.service;

import com.mvbbb.yim.common.protoc.http.response.PullOfflineMsgResponse;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.vo.MsgVO;

import java.util.List;

public interface MsgService {
    List<MsgVO> getHistoryMsg(String userId, String sessionId, SessionType sessionType, int from, int limit);

    PullOfflineMsgResponse getOfflineMsg(String userId);
}
