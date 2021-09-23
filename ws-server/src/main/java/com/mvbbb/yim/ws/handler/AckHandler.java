package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.common.protoc.Ack;
import com.mvbbb.yim.ws.ConnectionPool;
import com.mvbbb.yim.ws.MsgAckPool;
import com.mvbbb.yim.ws.service.MsgSendService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@ChannelHandler.Sharable
public class AckHandler extends SimpleChannelInboundHandler<Ack> {

    private static final Logger logger = LoggerFactory.getLogger(AckHandler.class);
    private static final MsgAckPool MSG_ACK_POOL = MsgAckPool.getInstance();
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static AckHandler ackHandler;

    @Resource
    MsgSendService msgSendService;


    @PostConstruct
    public void init() {
        ackHandler = this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Ack data) throws Exception {

        if (CONNECTION_POOL.checkToClose(ctx.channel())) {
            return;
        }
        String userId = CONNECTION_POOL.getUseridByChannel(ctx.channel());
        logger.info("收到 Ack 消息：{}", data);
        String key = data.getServerMsgId() + ":" + userId;
        logger.info("消息投递成功 {}", key);
        MSG_ACK_POOL.acked(key);
        ackHandler.msgSendService.sendAckToUser(userId, "服务器接收到消息");
    }
}
