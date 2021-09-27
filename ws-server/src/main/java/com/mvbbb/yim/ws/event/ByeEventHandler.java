package com.mvbbb.yim.ws.event;

import com.mvbbb.yim.common.protoc.Bye;
import com.mvbbb.yim.ws.pool.ConnectionPool;
import com.mvbbb.yim.ws.service.StatusService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ByeEventHandler implements IEvent<Bye> {

    private static final Logger logger = LoggerFactory.getLogger(ByeEventHandler.class);
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    @Resource
    StatusService statusService;

    @Override
    public void handler(Bye bye, ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        logger.info("收到 Bye 消息：{}", bye);
        String userId = CONNECTION_POOL.getUseridByChannel(channel);
        statusService.bye(ctx.channel(), userId);
    }
}

