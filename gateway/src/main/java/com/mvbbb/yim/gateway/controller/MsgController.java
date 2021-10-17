package com.mvbbb.yim.gateway.controller;

import com.mvbbb.yim.common.protoc.http.response.GenericResponse;
import com.mvbbb.yim.common.protoc.http.response.RecentChatResponse;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.vo.MsgVO;
import com.mvbbb.yim.gateway.CheckAuth;
import com.mvbbb.yim.logic.service.MsgService;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CheckAuth
public class MsgController {

    @DubboReference(check = false, retries = 0)
    MsgService msgService;

    @ApiOperation("获取历史消息")
    @RequestMapping(path = "/message/history", method = RequestMethod.GET)
    public GenericResponse<List<MsgVO>> getHistoryMsg(@RequestHeader String userId,
                                                      @RequestHeader String token,
                                                      @RequestParam SessionType sessionType,
                                                      @RequestParam String sessionId,
                                                      @RequestParam int from,
                                                      @RequestParam int limit) {

        List<MsgVO> historyMsg = msgService.getHistoryMsg(userId,
                sessionId,
                sessionType,
                from,
                limit);
        return GenericResponse.success(historyMsg);
    }

    @ApiOperation("获取最近聊天列表 (登录之后，拉取一次，用于获取会话列表及其未读消息)")
    @RequestMapping(path = "/recent/chat", method = RequestMethod.GET)
    public GenericResponse<RecentChatResponse> getRecentOfflineMsg(@RequestHeader String userId, @RequestHeader String token) {
        RecentChatResponse recentChatResponse = msgService.getRecentOfflineMsg(userId);
        return GenericResponse.success(recentChatResponse);
    }

    @ApiOperation("获取未读消息的条目（非web端api，不适用于web端）")
    @RequestMapping(path = "/unread/count/all", method = RequestMethod.GET)
    public GenericResponse<String> getUnreadMsgCount(@RequestHeader String userId, @RequestHeader String token) {
        long count = msgService.getAllUnreadCount(userId);
        return GenericResponse.success(String.valueOf(count));
    }

    @ApiOperation("清空一个会话的所有未读消息条数（非web端api，不适用于web端）")
    @RequestMapping(path = "/unread/count/session/clear", method = RequestMethod.POST)
    public GenericResponse allUnreadReaded(@RequestHeader String userId, @RequestHeader String token, @RequestHeader SessionType sessionType, @RequestHeader String sessionId) {
        msgService.clearUnreadReaded(userId, sessionType, sessionId);
        return GenericResponse.success();
    }

}
