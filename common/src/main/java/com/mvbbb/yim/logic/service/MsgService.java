package com.mvbbb.yim.logic.service;

import com.mvbbb.yim.common.protoc.http.response.RecentChatResponse;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.vo.MsgVO;

import java.util.List;

public interface MsgService {
    List<MsgVO> getHistoryMsg(String userId, String sessionId, SessionType sessionType, int from, int limit);

//    PullOfflineMsgResponse getOfflineMsg(String userId);

    RecentChatResponse getRecentOfflineMsg(String userId);

    long getAllUnreadCount(String userId);

    void clearUnreadReaded(String userId, SessionType sessionType, String sessionId);
}
