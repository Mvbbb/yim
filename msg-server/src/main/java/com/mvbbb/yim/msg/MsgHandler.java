package com.mvbbb.yim.msg;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mvbbb.yim.common.WsServerRoute;
import com.mvbbb.yim.common.entity.*;
import com.mvbbb.yim.common.mapper.*;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.MsgType;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.util.BeanConvertor;
import com.mvbbb.yim.mq.RedisStreamManager;
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
    RedisStreamManager redisStreamManager;
    private Logger logger = LoggerFactory.getLogger(MsgHandler.class);

    public void sendSingleMsg(MsgData msgData,boolean fromGroup) {

        String toUserId = msgData.getToUserId();
        if(!fromGroup){
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
            logger.info("投递消息到接收用户所在的 ws 的消息队列。ToUserId:{},WsServerRoute:{}", toUserId,userWsServer);
            redisStreamManager.produce(userWsServer, msgData);
        }
    }

    /**
     * 采用写扩散的方式发送群聊消息
     * @param msgData 某个用户发送的群聊消息
     */
    public void sendGroupMsg(MsgData msgData) {
        String groupId = msgData.getGroupId();
        List<String> members = userGroupRelationMapper.selectList(new LambdaQueryWrapper<UserGroupRelation>().eq(UserGroupRelation::getGroupId, groupId))
                .stream().map(UserGroupRelation::getUserId).collect(Collectors.toList());
        members.forEach((memberId) -> {
            if (memberId != null && !memberId.equals(msgData.getFromUserId())) {
                logger.info("发送消息给群成员。UserId:{}",memberId);
                msgData.setToUserId(memberId);
                updateRecentMsg(msgData);
                sendSingleMsg(msgData,true);
            }
        });
    }

    public void saveMsg(MsgData msgData) {
        logger.info("持久化消息到 msg_recv 表中。msgData:{}",msgData);
        MsgRecv msgRecv = BeanConvertor.msgDataToMsgRecv(msgData);
        msgRecvMapper.insert(msgRecv);
    }

    public void updateRecentMsg(MsgData msgData){

        //msgRecent 只保留最新的一条消息记录
        msgRecentMapper.deleteSessionOldMsg(msgData.getFromUserId(),msgData.getToUserId(),msgData.getGroupId());
        MsgRecent msgRecent = new MsgRecent();
        String name = null;
        String avatar = null;
        switch (msgData.getSessionType()){
            case SINGLE:
                User user = userMapper.selectById(msgData.getToUserId());
                if(user==null){
                    logger.error("发生错误");
                    return;
                }
                name = user.getUsername();
                avatar = user.getAvatar();
                break;
            case GROUP:
                Group group = groupMapper.selectById(msgData.getGroupId());
                if(group==null) {
                    logger.error("发生错误");
                    return;
                }
                name = group.getGroupName() ;
                avatar = group.getAvatar();
                break;
            default:break;
        }
        msgRecent.setFromUid(msgData.getFromUserId());
        msgRecent.setToUid(msgData.getToUserId());
        msgRecent.setGroupId(msgData.getGroupId());
        msgRecent.setSessionType(SessionType.getInt(msgData.getSessionType()));
        msgRecent.setContent(msgData.getData());
        msgRecent.setName(name);
        msgRecent.setAvatar(avatar);
        msgRecent.setTimestamp(msgData.getTimestamp());
        msgRecentMapper.insert(msgRecent);
    }
}
