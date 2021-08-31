package com.mvbbb.yim.msg;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mvbbb.yim.common.WsServerRoute;
import com.mvbbb.yim.common.entity.MsgRecv;
import com.mvbbb.yim.common.entity.UserGroupRelation;
import com.mvbbb.yim.common.mapper.MsgRecvMapper;
import com.mvbbb.yim.common.mapper.UserGroupRelationMapper;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.enums.MsgType;
import com.mvbbb.yim.common.protoc.enums.SessionType;
import com.mvbbb.yim.mq.MqManager;
import com.mvbbb.yim.msg.service.RouteService;
import com.mvbbb.yim.msg.service.UserStatusService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    RedisTemplate<String,String> redisTemplate;
    @Resource
    MsgRecvMapper msgRecvMapper;
    @Resource
    RouteService routeService;

    MqManager mqManager;


    public void sendSingleMsg(MsgData msgData) {
        if(mqManager==null){
            mqManager = new MqManager(redisTemplate);
        }

        String toUserId = msgData.getToSessionId();

        if(userStatusService.isUserOnline(toUserId)){
            // 写入离线消息
            MsgRecv msgRecv = new MsgRecv();
            msgRecv.setMsgId(msgData.getServerMsgId());
            msgRecv.setMsgFrom(msgData.getFromUserId());
            msgRecv.setMsgTo(msgData.getSessionType()== SessionType.SINGLE?msgData.getToSessionId():null);
            msgRecv.setGroupId(msgData.getSessionType()== SessionType.GROUP?msgData.getToSessionId():null);
            msgRecv.setMsgType(msgData.getMsgType()== MsgType.TEXT?0:1);
            msgRecv.setTimestamp(msgData.getTimestamp());
            msgRecv.setDeleted(false);
            msgRecvMapper.insert(msgRecv);

        }else{
            // 写入消息投递队列
            WsServerRoute userWsServer = routeService.getUserWsServer(toUserId);
            mqManager.produce(userWsServer,msgData);
        }
    }

    // 扩散写
    public void sendGroupMsg(MsgData msgData) {
        String groupId = msgData.getToSessionId();
        List<String> members = userGroupRelationMapper.selectList(new LambdaQueryWrapper<UserGroupRelation>().eq(UserGroupRelation::getGroupId, groupId))
                .stream().map(UserGroupRelation::getUserId).collect(Collectors.toList());
        members.forEach((memberId)->{
            if(!memberId.equals(msgData.getFromUserId())){
                msgData.setRecvUserId(memberId);
                sendSingleMsg(msgData);
            }
        });
    }
}
