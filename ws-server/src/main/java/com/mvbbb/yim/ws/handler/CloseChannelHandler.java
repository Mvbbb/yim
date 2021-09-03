package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.logic.service.UserService;
import com.mvbbb.yim.logic.service.UserStatusService;
import com.mvbbb.yim.ws.ConnectionPool;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CloseChannelHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(CloseChannelHandler.class);

    ConnectionPool connectionPool = ConnectionPool.getInstance();
    @DubboReference
    UserStatusService userStatusService;

    private static CloseChannelHandler closeChannelHandler;

    @PostConstruct
    public void init(){
        closeChannelHandler = this;
    }

    // 用户关闭连接时清理状态
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String userId = connectionPool.getUseridByChannel(channel);
        if(userId!=null){
            connectionPool.removeConnection(channel,userId);
            closeChannelHandler.userStatusService.userOffline(userId);
            logger.error("user close the websocket connection. userId：[{}],channel: [{}]",userId,channel);
        }
    }
}