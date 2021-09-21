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
    private static final MsgAckPool msgAckPool = MsgAckPool.getInstance();
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static AckHandler ackHandler;

    @Resource
    MsgSendService msgSendService;


    @PostConstruct
    public void init() {
        ackHandler = this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Ack data) throws Exception {

        if (connectionPool.checkToClose(ctx.channel())) {
            return;
        }
        ;

        logger.info("收到 Ack 消息：{}", data);
        String key = data.getServerMsgId() + ":" + data.getUserId();
        logger.info("消息投递成功 {}", key);
        msgAckPool.acked(key);
        ackHandler.msgSendService.sendAckToUser(data.getUserId(), "服务器接收到消息");
    }
}
