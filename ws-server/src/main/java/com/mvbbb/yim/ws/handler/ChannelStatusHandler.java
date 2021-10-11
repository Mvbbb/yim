package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.ws.event.EventEnum;
import com.mvbbb.yim.ws.event.EventExecutor;
import com.mvbbb.yim.ws.pool.EventPool;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class ChannelStatusHandler extends ChannelInboundHandlerAdapter {

    private static final EventPool eventPool = EventPool.getInstance();
    private final Logger logger = LoggerFactory.getLogger(ChannelStatusHandler.class);

    /**
     * 用户关闭连接时清理状态
     *
     * @param ctx 用户 Channel
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        EventExecutor.execute(eventPool.find(EventEnum.OFFLINE), null, ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        EventExecutor.execute(eventPool.find(EventEnum.ACTIVE), null, ctx);
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