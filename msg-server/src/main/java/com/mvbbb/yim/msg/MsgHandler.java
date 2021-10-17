package com.mvbbb.yim.msg;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mvbbb.yim.common.service.UnreadService;
import com.mvbbb.yim.common.WsServerRoute;
import com.mvbbb.yim.common.entity.*;
import com.mvbbb.yim.common.mapper.*;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.util.BeanConvertor;
import com.mvbbb.yim.msg.mq.MqSender;
import com.mvbbb.yim.msg.service.RouteService;
import com.mvbbb.yim.msg.service.UserStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MsgHandler {

    @Resource
    UserStatusService userStatusService;
    @Resource
    UserGroupRelationMapper userGroupRelationMapper;
    @Resource
    MsgRecvMapper msgRecvMapper;
    @Resource
    RouteService routeService;
    @Resource
    MsgRecentMapper msgRecentMapper;
    @Resource
    GroupMapper groupMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    MqSender mqSender;
    @Resource
    UnreadService unreadService;
    private Logger logger = LoggerFactory.getLogger(MsgHandler.class);

    public void sendSingleMsg(MsgData msgData, boolean fromGroup) {

        String toUserId = msgData.getToUserId();
        if (!fromGroup) {
            unreadService.updateUnreadCount(toUserId,SessionType.SINGLE,msgData.getFromUserId(),1);
            updateRecentMsg(msgData);
        }
        if (!userStatusService.isUserOnline(toUserId)) {
            // 写入离线消息
            logger.error("用户 [{}] 不在线, 保存消息到离线表", toUserId);
            userStatusService.offlineMsgOccur(toUserId);
            saveMsg(msgData);
        } else {
            // 写入消息投递队列
            WsServerRoute userWsServer = routeService.getUserWsServer(toUserId);
            logger.info("投递消息到接收用户所在的 ws 的消息队列。ToUserId:{},WsServerRoute:{}", toUserId, userWsServer);
            mqSender.produce(userWsServer, msgData);
        }
    }

    /**
     * 采用写扩散的方式发送群聊消息
     *
     * @param msgData 某个用户发送的群聊消息
     */
    public void sendGroupMsg(MsgData msgData) {
        String groupId = msgData.getGroupId();
        List<String> members = userGroupRelationMapper.selectList(new LambdaQueryWrapper<UserGroupRelation>().eq(UserGroupRelation::getGroupId, groupId))
                .stream().map(UserGroupRelation::getUserId).collect(Collectors.toList());
        members.forEach((memberId) -> {
            if (memberId != null) {
                if (!memberId.equals(msgData.getFromUserId())) {
                    logger.info("发送消息给群成员。UserId:{}", memberId);
                    msgData.setToUserId(memberId);
                    updateRecentMsg(msgData);
                    // 原子化更新未读消息数
                    unreadService.updateUnreadCount(memberId,SessionType.GROUP,groupId,1);
                    sendSingleMsg(msgData, true);
                } else {
                    msgData.setToUserId(msgData.getFromUserId());
                    updateRecentMsg(msgData);
                }
            }
        });
    }

    public void saveMsg(MsgData msgData) {
        logger.info("持久化消息到 msg_recv 表中。msgData:{}", msgData);
        MsgRecv msgRecv = BeanConvertor.msgDataToMsgRecv(msgData);
        msgRecvMapper.insert(msgRecv);
    }

    public void updateRecentMsg(MsgData msgData) {

        MsgRecent msgRecent;
        switch (msgData.getSessionType()) {
            case SINGLE:
                msgRecent = msgRecentMapper.selectUserSingleRecentMsg(msgData.getToUserId(), msgData.getFromUserId());
                if (msgRecent == null) {
                    //insert
                    MsgRecent msgRecent1 = new MsgRecent();
                    msgRecent1.setUserId(msgData.getFromUserId());
                    msgRecent1.setSessionUid(msgData.getToUserId());
                    msgRecent1.setSessionType(SessionType.getInt(SessionType.SINGLE));
                    User user1 = userMapper.selectById(msgData.getToUserId());
                    msgRecent1.setName(user1.getUsername());
                    msgRecent1.setAvatar(user1.getAvatar());
                    msgRecent1.setContent(msgData.getData());
                    msgRecent1.setTimestamp(msgData.getTimestamp());
                    msgRecentMapper.insert(msgRecent1);

                    MsgRecent msgRecent2 = new MsgRecent();
                    msgRecent2.setUserId(msgData.getToUserId());
                    msgRecent2.setSessionUid(msgData.getFromUserId());
                    msgRecent2.setSessionType(SessionType.getInt(SessionType.SINGLE));
                    User user2 = userMapper.selectById(msgData.getFromUserId());
                    msgRecent2.setName(user2.getUsername());
                    msgRecent2.setAvatar(user2.getAvatar());
                    msgRecent2.setContent(msgData.getData());
                    msgRecent2.setTimestamp(msgData.getTimestamp());
                    msgRecentMapper.insert(msgRecent2);

                } else {
                    //update
                    msgRecentMapper.updateSingleChatContent(msgData.getFromUserId(), msgData.getToUserId(), msgData.getData());
                }

                break;
            case GROUP:
                msgRecent = msgRecentMapper.selectUserGroupRecentMsg(msgData.getToUserId(), msgData.getGroupId());
                if (msgRecent == null) {
                    MsgRecent msgRecent3 = new MsgRecent();
                    msgRecent3.setUserId(msgData.getToUserId());
                    msgRecent3.setSessionGroupId(msgData.getGroupId());
                    msgRecent3.setSessionType(SessionType.getInt(SessionType.GROUP));
                    Group group = groupMapper.selectById(msgData.getGroupId());
                    msgRecent3.setName(group.getGroupName());
                    msgRecent3.setAvatar(group.getAvatar());
                    msgRecent3.setTimestamp(msgData.getTimestamp());
                    msgRecent3.setContent(msgData.getData());
                    msgRecentMapper.insert(msgRecent3);

                } else if (!msgRecent.getContent().equals(msgData.getData())) {
                    msgRecentMapper.updateGroupChantContent(msgData.getGroupId(), msgData.getData());
                }

                break;
            default:
                break;
        }
    }
}
