package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.common.protoc.ws.request.GreetRequest;
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
public class GreetHandler extends SimpleChannelInboundHandler<GreetRequest> {

    private static final Logger logger = LoggerFactory.getLogger(GreetHandler.class);

    private static GreetHandler greetHandler;
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Resource
    StatusService statusService;

    @PostConstruct
    public void init(){
        greetHandler = this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GreetRequest data) throws Exception {
        Channel channel = ctx.channel();
        logger.info("receive greet message: {}",data);
        String userId = connectionPool.getUseridByChannel(channel);
        greetHandler.statusService.greet(channel,data);
    }
}
