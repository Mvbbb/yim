package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.common.protoc.Bye;
import com.mvbbb.yim.ws.ConnectionPool;
import com.mvbbb.yim.ws.service.StatusService;
import io.netty.channel.Channel;
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
public class ByeHandler extends SimpleChannelInboundHandler<Bye> {
    private static final Logger logger = LoggerFactory.getLogger(ByeHandler.class);
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static ByeHandler byeHandler;
    @Resource
    StatusService statusService;

    @PostConstruct
    public void init() {
        byeHandler = this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Bye msg) throws Exception {
        Channel channel = ctx.channel();
        logger.info("收到 Bye 消息：{}", msg);
        String userId = CONNECTION_POOL.getUseridByChannel(channel);
        byeHandler.statusService.bye(ctx.channel(), userId);
    }
}
