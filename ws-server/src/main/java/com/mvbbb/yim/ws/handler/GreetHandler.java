package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.common.protoc.ws.request.GreetRequest;
import com.mvbbb.yim.ws.pool.ConnectionPool;
import com.mvbbb.yim.ws.IEvent;
import com.mvbbb.yim.ws.service.StatusService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class GreetHandler implements IEvent<GreetRequest> {

    private static final Logger logger = LoggerFactory.getLogger(GreetHandler.class);
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    @Resource
    StatusService statusService;

    @Override
    public void handler(GreetRequest greetRequest, ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        logger.info("receive greet message: {}", greetRequest);
        String userId = CONNECTION_POOL.getUseridByChannel(channel);
        statusService.greet(channel, greetRequest);
    }
}
