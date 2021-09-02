package com.mvbbb.yim.msg;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mvbbb.yim.common.WsServerRoute;
import com.mvbbb.yim.common.entity.MsgRecv;
import com.mvbbb.yim.common.entity.UserGroupRelation;
import com.mvbbb.yim.common.mapper.MsgRecvMapper;
import com.mvbbb.yim.common.mapper.UserGroupRelationMapper;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.MsgType;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.mq.MqManager;
import com.mvbbb.yim.msg.service.RouteService;
import com.mvbbb.yim.msg.service.UserStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MsgHandler {

    private Logger logger = LoggerFactory.getLogger(MsgHandler.class);

    @Resource
    UserStatusService userStatusService;
    @Resource
    UserGroupRelationMapper userGroupRelationMapper;
    @Resource
    RedisTemplate<Object,Object> redisTemplate;
    @Resource
    MsgRecvMapper msgRecvMapper;
    @Resource
    RouteService routeService;

    MqManager mqManager;


    public void sendSingleMsg(MsgData msgData) {
        if(mqManager==null){
            mqManager = new MqManager(redisTemplate);
        }

        String toUserId = msgData.getRecvUserId();

        if(!userStatusService.isUserOnline(toUserId)){
            // 写入离线消息
            logger.error("user [{}] not online, save to offline table",toUserId);
            saveMsg(msgData);
        }else{
            // 写入消息投递队列
            logger.info("produce to mq, message to user [{}]",toUserId);
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
            if(memberId!=null&&!memberId.equals(msgData.getFromUserId())){
                msgData.setRecvUserId(memberId);
                sendSingleMsg(msgData);
            }
        });
    }

    public void saveMsg(MsgData msgData){
        MsgRecv msgRecv = new MsgRecv();
        msgRecv.setMsgId(msgData.getServerMsgId());
        msgRecv.setMsgFrom(msgData.getFromUserId());
        msgRecv.setMsgTo(msgData.getRecvUserId());
        msgRecv.setGroupId(msgData.getSessionType()==SessionType.GROUP?msgData.getToSessionId():null);
        msgRecv.setMsgType(msgData.getMsgType()== MsgType.TEXT?0:1);
        msgRecv.setTimestamp(msgData.getTimestamp());
        msgRecv.setMsgData(msgData.getData());
        msgRecv.setDeleted(false);
        msgRecvMapper.insert(msgRecv);
    }
}
