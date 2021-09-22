package com.mvbbb.yim.msg;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mvbbb.yim.common.WsServerRoute;
import com.mvbbb.yim.common.entity.MsgRecv;
import com.mvbbb.yim.common.entity.UserGroupRelation;
import com.mvbbb.yim.common.mapper.MsgRecvMapper;
import com.mvbbb.yim.common.mapper.UserGroupRelationMapper;
import com.mvbbb.yim.common.protoc.MsgData;
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
    RedisStreamManager redisStreamManager;
    private Logger logger = LoggerFactory.getLogger(MsgHandler.class);

    public void sendSingleMsg(MsgData msgData) {

        String toUserId = msgData.getRecvUserId();

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

    // 扩散写
    public void sendGroupMsg(MsgData msgData) {
        String groupId = msgData.getToSessionId();
        List<String> members = userGroupRelationMapper.selectList(new LambdaQueryWrapper<UserGroupRelation>().eq(UserGroupRelation::getGroupId, groupId))
                .stream().map(UserGroupRelation::getUserId).collect(Collectors.toList());
        members.forEach((memberId) -> {
            if (memberId != null && !memberId.equals(msgData.getFromUserId())) {
                logger.info("发送消息给群成员。UserId:{}",memberId);
                msgData.setRecvUserId(memberId);
                sendSingleMsg(msgData);
            }
        });
    }

    public void saveMsg(MsgData msgData) {
        logger.info("持久化消息到 msg_recv 表中。msgData:{}",msgData);
        MsgRecv msgRecv = BeanConvertor.msgDataToMsgRecv(msgData);
        msgRecvMapper.insert(msgRecv);
    }
}
