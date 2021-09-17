package com.mvbbb.yim.logic.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mvbbb.yim.common.entity.MsgRecv;
import com.mvbbb.yim.common.entity.MsgSend;
import com.mvbbb.yim.common.mapper.MsgRecvMapper;
import com.mvbbb.yim.common.mapper.MsgSendMapper;
import com.mvbbb.yim.common.protoc.http.OfflineSessionMsg;
import com.mvbbb.yim.common.protoc.http.response.PullOfflineMsgResponse;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.util.BeanConvertor;
import com.mvbbb.yim.common.vo.MsgVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@DubboService
public class MsgServiceImpl implements MsgService {

    private static final Logger logger = LoggerFactory.getLogger(MsgServiceImpl.class);

    @Resource
    MsgRecvMapper msgRecvMapper;
    @Resource
    MsgSendMapper msgSendMapper;
    @DubboReference(check = false)
    UserStatusService userStatusService;

    /**
     * 拉取一个会话的历史消息
     *
     * @param userId      用户 id
     * @param sessionId   群聊或者好友 id
     * @param sessionType 群聊/私聊
     * @param from        群聊 id/好友id
     * @param limit       限制拉取条数
     * @return 本会话的历史消息
     */
    @Override
    public List<MsgVO> getHistoryMsg(String userId, String sessionId, SessionType sessionType, int from, int limit) {
        List<MsgVO> res = null;
        switch (sessionType) {
            case SINGLE:
                List<MsgSend> singleMsgSends = msgSendMapper.selectHistorySingleMsg(userId, sessionId, from, limit);
                res = singleMsgSends.stream().map((BeanConvertor::msgSendToMsgVO)).collect(Collectors.toList());
                break;
            case GROUP:
                List<MsgSend> groupMsgSends = msgSendMapper.selectHistoryGroupMsg(sessionId, from, limit);
                res = groupMsgSends.stream().map((BeanConvertor::msgSendToMsgVO)).collect(Collectors.toList());
                break;
            default:
                break;
        }
        return res;
    }

    /**
     * 拉取离线消息
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public PullOfflineMsgResponse getOfflineMsg(String userId) {
        List<MsgRecv> msgRecvs = msgRecvMapper.selectList(new LambdaQueryWrapper<MsgRecv>().eq(MsgRecv::getToUid, userId).eq(MsgRecv::isDelivered, false));
        msgRecvMapper.delivered(userId);

        PullOfflineMsgResponse pullOfflineMsgResponse = new PullOfflineMsgResponse();
        pullOfflineMsgResponse.setCnt(msgRecvs.size());
        // 群聊消息
        Set<String> groupIds = msgRecvs.stream().map(MsgRecv::getGroupId).collect(Collectors.toSet());
        groupIds.forEach((groupId) -> {
            if (groupId != null) {
                OfflineSessionMsg offlineSessionMsg = new OfflineSessionMsg();
                offlineSessionMsg.setSessionId(groupId);

                List<MsgVO> groupMsgData = msgRecvs.stream().filter((msgRecv) -> {
                    if (msgRecv.getGroupId() != null && msgRecv.getGroupId().equals(groupId)) {
                        return true;
                    } else {
                        return false;
                    }
                }).collect(Collectors.toList()).stream().map((BeanConvertor::msgRecvToMsgVO)).collect(Collectors.toList());
                offlineSessionMsg.setSessionType(SessionType.GROUP);
                offlineSessionMsg.setMsgs(groupMsgData);
                offlineSessionMsg.setCnt(groupMsgData.size());
                offlineSessionMsg.setTimestamp(groupMsgData.get(groupMsgData.size() - 1).getTimestamp());
                pullOfflineMsgResponse.getSessionsMsgs().add(offlineSessionMsg);
            }
        });

        // 私聊消息
        Set<String> friendIds = msgRecvs.stream().filter((msgRecv) -> {
            return msgRecv.getGroupId() == null;
        }).map(MsgRecv::getFromUid).collect(Collectors.toSet());
        friendIds.forEach((fromId) -> {
            if (fromId != null) {
                OfflineSessionMsg friendSessionMsgs = new OfflineSessionMsg();
                friendSessionMsgs.setSessionId(fromId);
                friendSessionMsgs.setSessionType(SessionType.SINGLE);
                List<MsgVO> friendMsgs = msgRecvs.stream().filter((msgRecv -> {
                    return msgRecv.getGroupId() == null && msgRecv.getFromUid().equals(fromId);
                })).collect(Collectors.toList()).stream().map(BeanConvertor::msgRecvToMsgVO).collect(Collectors.toList());
                friendSessionMsgs.setMsgs(friendMsgs);
                friendSessionMsgs.setCnt(friendMsgs.size());
                friendSessionMsgs.setTimestamp(friendMsgs.get(friendMsgs.size() - 1).getTimestamp());
                pullOfflineMsgResponse.getSessionsMsgs().add(friendSessionMsgs);
            }
        });
        userStatusService.offlineMsgPoolOver(userId);
        logger.info("{}", pullOfflineMsgResponse);
        return pullOfflineMsgResponse;
    }
}
