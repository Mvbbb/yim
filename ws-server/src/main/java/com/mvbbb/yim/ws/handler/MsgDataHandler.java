package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.util.SnowflakeIdWorker;
import com.mvbbb.yim.logic.service.RelationService;
import com.mvbbb.yim.msg.service.MsgService;
import com.mvbbb.yim.ws.ConnectionPool;
import com.mvbbb.yim.ws.service.MsgSendService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@ChannelHandler.Sharable
public class MsgDataHandler extends SimpleChannelInboundHandler<MsgData> {

    private static final Logger logger = LoggerFactory.getLogger(MsgDataHandler.class);
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static MsgDataHandler msgDataHandler;
    @DubboReference(check = false)
    MsgService msgService;
    @DubboReference(check = false)
    RelationService relationService;
    @Resource
    MsgSendService sendDataToUserHandler;

    @PostConstruct
    public void init() {
        msgDataHandler = this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MsgData data) throws Exception {

        Channel channel = ctx.channel();
        if (CONNECTION_POOL.checkToClose(channel)) {
            logger.error("请先发送 Greet 消息进行认证");
            return;
        }

        String userId = CONNECTION_POOL.getUseridByChannel(channel);
        data.setFromUserId(userId);
        checkMsgValid(channel, data);


        logger.info("收到 msg data 消息：{}", data);
        data.setServerMsgId(SnowflakeIdWorker.generateId());

        if (!checkRelationValid(channel, data)) {
            return;
        }

        msgDataHandler.sendDataToUserHandler.sendAckToUser(data.getFromUserId(), "服务器接收到消息了");
        logger.info("消息传递给 MsgService。MsgData:{}", data);
        msgDataHandler.msgService.handlerMsgData(data);
    }

    private void checkMsgValid(Channel channel, MsgData data) {
        if (
                data.getMsgType() == null || data.getFromUserId() == null || (data.getToUserId() == null && data.getGroupId() == null)
        ) {
            logger.error("消息格式错误，缺少必要信息。MsgData:{}", data);
            msgDataHandler.sendDataToUserHandler.send(channel, "消息格式错误，缺少必要信息");
        }
    }

    private boolean checkRelationValid(Channel channel, MsgData data) {
        String fromUserId = data.getFromUserId();
        String toSessionId = data.getSessionType()== SessionType.SINGLE?data.getToUserId():data.getGroupId();
        switch (data.getSessionType()) {
            case SINGLE:
                boolean friend = msgDataHandler.relationService.isFriend(fromUserId, toSessionId);
                if (!friend) {
                    logger.error("非法发送消息，并非朋友关系。MsgData:{}", data);
                    msgDataHandler.sendDataToUserHandler.send(channel, "你们还不是朋友关系，不能发送消息。");
                    return false;
                }
                break;
            case GROUP:
                boolean member = msgDataHandler.relationService.isMember(fromUserId, toSessionId);
                if (!member) {
                    logger.error("非法发送消息，并非群成员。MsgData：{}", data);
                    msgDataHandler.sendDataToUserHandler.send(channel, "不是群成员，不能发送消息");
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
    }
}
