package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.logic.service.UserStatusService;
import com.mvbbb.yim.ws.pool.ConnectionPool;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;

@Component
@ChannelHandler.Sharable
public class ChannelStatusHandler extends ChannelInboundHandlerAdapter {

    private static ChannelStatusHandler closeChannelHandler;
    private final Logger logger = LoggerFactory.getLogger(ChannelStatusHandler.class);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    @DubboReference(check = false)
    UserStatusService userStatusService;

    @PostConstruct
    public void init() {
        closeChannelHandler = this;
    }

    /**
     * 用户关闭连接时清理状态
     * @param ctx 用户 Channel
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String userId = connectionPool.getUseridByChannel(channel);
        if (userId != null) {
            connectionPool.removeConnection(channel, userId);
            closeChannelHandler.userStatusService.userOffline(userId);
            logger.error("用户断开 socket 连接. userId：[{}],channel: [{}]", userId, channel);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Timer timer = new Timer("time to close channel", false);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String user = connectionPool.getUseridByChannel(ctx.channel());
                if (user == null) {
                    try {
                        logger.error("长时间没有发送认证消息，自动关闭 channel。channel : [{}]", ctx.channel());
                        ctx.channel().close().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 10000);
    }

    /**
     * TODO client 与 server 心跳机制
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            switch (((IdleStateEvent) evt).state()) {
                case ALL_IDLE:
                    logger.info("超时, channel: {}", ctx.channel());
                    break;
                case READER_IDLE:
                    logger.info("客户端读超时, channel: {}", ctx.channel());
                    break;
                case WRITER_IDLE:
                    logger.info("客户端写超时, channel: {}", ctx.channel());
                    break;
                default:
                    break;
            }
        }
    }
}