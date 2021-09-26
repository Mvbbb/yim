package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.common.protoc.Ack;
import com.mvbbb.yim.ws.pool.ConnectionPool;
import com.mvbbb.yim.ws.IEvent;
import com.mvbbb.yim.ws.pool.MsgAckPool;
import com.mvbbb.yim.ws.service.MsgSendService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AckHandler implements IEvent<Ack> {

    private static final Logger logger = LoggerFactory.getLogger(AckHandler.class);
    private static final MsgAckPool MSG_ACK_POOL = MsgAckPool.getInstance();
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();

    @Resource
    MsgSendService msgSendService;

    @Override
    public void handler(Ack ack, ChannelHandlerContext ctx) {

        if (CONNECTION_POOL.checkToClose(ctx.channel())) {
            return;
        }
        String userId = CONNECTION_POOL.getUseridByChannel(ctx.channel());
        logger.info("收到 Ack 消息：{}", ack);
        String key = ack.getServerMsgId() + ":" + userId;
        logger.info("消息投递成功 {}", key);
        MSG_ACK_POOL.acked(key);
        msgSendService.sendAckToUser(userId, "服务器接收到消息");
    }
}
