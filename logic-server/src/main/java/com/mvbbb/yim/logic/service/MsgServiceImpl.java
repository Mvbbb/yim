package com.mvbbb.yim.logic.service;

import com.mvbbb.yim.common.entity.MsgRecent;
import com.mvbbb.yim.common.entity.MsgRecv;
import com.mvbbb.yim.common.entity.MsgSend;
import com.mvbbb.yim.common.mapper.MsgRecentMapper;
import com.mvbbb.yim.common.mapper.MsgRecvMapper;
import com.mvbbb.yim.common.mapper.MsgSendMapper;
import com.mvbbb.yim.common.protoc.http.RecentChatItem;
import com.mvbbb.yim.common.protoc.http.response.RecentChatResponse;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.service.UnreadService;
import com.mvbbb.yim.common.util.BeanConvertor;
import com.mvbbb.yim.common.vo.MsgVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@DubboService
public class MsgServiceImpl implements MsgService {

    private static final Logger logger = LoggerFactory.getLogger(MsgServiceImpl.class);

    @Resource
    MsgRecvMapper msgRecvMapper;
    @Resource
    MsgSendMapper msgSendMapper;
    @Resource
    MsgRecentMapper msgRecentMapper;
    @Resource(name = "jsonRedisTemplate")
    RedisTemplate<Object, Object> redisTemplate;
    @Resource
    UnreadService unreadService;
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
//    @Override
//    public PullOfflineMsgResponse getOfflineMsg(String userId) {
//        List<MsgRecv> msgRecvs = msgRecvMapper.selectList(new LambdaQueryWrapper<MsgRecv>().eq(MsgRecv::getToUid, userId).eq(MsgRecv::isDelivered, false));
//        msgRecvMapper.delivered(userId);
//
//        PullOfflineMsgResponse pullOfflineMsgResponse = new PullOfflineMsgResponse();
//        pullOfflineMsgResponse.setCnt(msgRecvs.size());
//        // 群聊消息
//        Set<String> groupIds = msgRecvs.stream().map(MsgRecv::getGroupId).collect(Collectors.toSet());
//        groupIds.forEach((groupId) -> {
//            if (groupId != null) {
//                OfflineSessionMsg offlineSessionMsg = new OfflineSessionMsg();
//                offlineSessionMsg.setSessionId(groupId);
//
//                List<MsgVO> groupMsgData = msgRecvs.stream().filter((msgRecv) -> {
//                    if (msgRecv.getGroupId() != null && msgRecv.getGroupId().equals(groupId)) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }).collect(Collectors.toList()).stream().map((BeanConvertor::msgRecvToMsgVO)).collect(Collectors.toList());
//                offlineSessionMsg.setSessionType(SessionType.GROUP);
//                offlineSessionMsg.setMsgs(groupMsgData);
//                offlineSessionMsg.setCnt(groupMsgData.size());
//                offlineSessionMsg.setTimestamp(groupMsgData.get(groupMsgData.size() - 1).getTimestamp());
//                pullOfflineMsgResponse.getSessionsMsgs().add(offlineSessionMsg);
//            }
//        });
//
//        // 私聊消息
//        Set<String> friendIds = msgRecvs.stream().filter((msgRecv) -> {
//            return msgRecv.getGroupId() == null;
//        }).map(MsgRecv::getFromUid).collect(Collectors.toSet());
//        friendIds.forEach((fromId) -> {
//            if (fromId != null) {
//                OfflineSessionMsg friendSessionMsgs = new OfflineSessionMsg();
//                friendSessionMsgs.setSessionId(fromId);
//                friendSessionMsgs.setSessionType(SessionType.SINGLE);
//                List<MsgVO> friendMsgs = msgRecvs.stream().filter((msgRecv -> {
//                    return msgRecv.getGroupId() == null && msgRecv.getFromUid().equals(fromId);
//                })).collect(Collectors.toList()).stream().map(BeanConvertor::msgRecvToMsgVO).collect(Collectors.toList());
//                friendSessionMsgs.setMsgs(friendMsgs);
//                friendSessionMsgs.setCnt(friendMsgs.size());
//                friendSessionMsgs.setTimestamp(friendMsgs.get(friendMsgs.size() - 1).getTimestamp());
//                pullOfflineMsgResponse.getSessionsMsgs().add(friendSessionMsgs);
//            }
//        });
//        userStatusService.offlineMsgPoolOver(userId);
//        logger.info("{}", pullOfflineMsgResponse);
//        return pullOfflineMsgResponse;
//    }
    @Override
    public RecentChatResponse getRecentOfflineMsg(String userId) {

        List<MsgRecv> msgOffline = msgRecvMapper.selectOfflineMsgs(userId);
        List<MsgRecent> msgRecents = msgRecentMapper.selectUserRecentMsg(userId);

        Set<RecentChatItem> recentChatItems = new HashSet<>();
        for (MsgRecent msgRecent : msgRecents) {
            RecentChatItem recentChatItem = new RecentChatItem();
            recentChatItem.setName(msgRecent.getName());
            recentChatItem.setUserId(msgRecent.getSessionUid());
            recentChatItem.setGroupId(msgRecent.getSessionGroupId());
            recentChatItem.setSessionType(SessionType.getType(msgRecent.getSessionType()));
            recentChatItem.setAvatar(msgRecent.getAvatar());
            recentChatItem.setLatestMsgContent(msgRecent.getContent());
            recentChatItem.setLatestMsgDate(msgRecent.getTimestamp());
            recentChatItems.add(recentChatItem);
        }

        // 将所有的离线消息分组
        Map<String, List<MsgVO>> friendOfflineMsgs = new HashMap<>();
        Map<String, List<MsgVO>> groupOfflineMsgs = new HashMap<>();
        Map<String, Integer> friendUnread = new HashMap<>();
        Map<String, Integer> groupUnread = new HashMap<>();
        // 离线消息拼接
        msgOffline.forEach((msgRecv -> {

            MsgVO msgVO = BeanConvertor.msgRecvToMsgVO(msgRecv);
            int unread;
            switch (SessionType.getType(msgRecv.getSessionType())) {
                case SINGLE:
                    String friendUid = msgRecv.getFromUid();
                    List<MsgVO> msgVOS1 = friendOfflineMsgs.get(friendUid);
                    if (msgVOS1 == null) {
                        ArrayList<MsgVO> msgVOS = new ArrayList<>();
                        msgVOS.add(msgVO);
                        friendOfflineMsgs.put(friendUid, msgVOS);
                    } else {
                        msgVOS1.add(msgVO);
                    }
                    unread = friendUnread.getOrDefault(msgRecv.getToUid(), 0) + 1;
                    friendUnread.put(msgRecv.getToUid(), unread);
                    break;

                case GROUP:
                    String groupId = msgRecv.getGroupId();
                    List<MsgVO> msgVOS = groupOfflineMsgs.get(groupId);
                    if (msgVOS == null) {
                        ArrayList<MsgVO> msgVOS2 = new ArrayList<>();
                        msgVOS2.add(msgVO);
                        groupOfflineMsgs.put(groupId, msgVOS2);
                    } else {
                        msgVOS.add(msgVO);
                    }
                    unread = groupUnread.getOrDefault(msgRecv.getGroupId(), 0) + 1;
                    groupUnread.put(msgRecv.getGroupId(), unread);
                    break;

                default:
                    break;
            }
        }));

        // 对离线消息就行排序
        friendOfflineMsgs.forEach((key, value) -> {
            value.sort((msgVo1, msgVo2) -> {
                return msgVo2.getTimestamp().compareTo(msgVo1.getTimestamp());
            });
        });
        groupOfflineMsgs.forEach((key, value) -> {
            value.sort((msgVo1, msgVo2) -> {
                return msgVo2.getTimestamp().compareTo(msgVo1.getTimestamp());
            });
        });

        for (RecentChatItem recentChatItem : recentChatItems) {
            List<MsgVO> msgs = null;
            switch (recentChatItem.getSessionType()) {
                case SINGLE:
                    String friendId = recentChatItem.getUserId();
                    msgs = friendOfflineMsgs.get(friendId);
                    recentChatItem.setUnread(friendUnread.get(friendId));
                    break;
                case GROUP:
                    String groupId = recentChatItem.getGroupId();
                    msgs = groupOfflineMsgs.get(groupId);
                    recentChatItem.setUnread(groupUnread.get(groupId));
                    break;
                default:
                    break;
            }

            if (msgs == null) {
                recentChatItem.setUnread(0);
                recentChatItem.setMsgs(null);
            } else {
                recentChatItem.setUnread(msgs.size());
                recentChatItem.setMsgs(msgs);
            }
        }

        RecentChatResponse recentChatResponse = new RecentChatResponse();
        recentChatResponse.setRecentChatItems(new ArrayList<>(recentChatItems));

        msgRecvMapper.delivered(userId);
        userStatusService.offlineMsgPoolOver(userId);
        return recentChatResponse;
    }

    @Override
    public long getAllUnreadCount(String userId) {
        return unreadService.getAllUnreadCount(userId);
    }

    @Override
    public void clearUnreadReaded(String userId, SessionType sessionType, String sessionId) {
        unreadService.clearSessionUnreadCount(userId, sessionType, sessionId);
    }
}
