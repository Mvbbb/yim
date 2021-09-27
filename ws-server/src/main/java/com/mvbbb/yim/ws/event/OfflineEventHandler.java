package com.mvbbb.yim.ws.event;

import com.mvbbb.yim.logic.service.UserStatusService;
import com.mvbbb.yim.ws.pool.ConnectionPool;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OfflineEventHandler implements IEvent<Object> {

    private static final Logger logger = LoggerFactory.getLogger(OfflineEventHandler.class);
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
    @DubboReference(check = false)
    UserStatusService userStatusService;

    @Override
    public void handler(Object o, ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        String userId = connectionPool.getUseridByChannel(channel);
        if (userId != null) {
            connectionPool.removeConnection(channel, userId);
            userStatusService.userOffline(userId);
            logger.error("用户断开 socket 连接. userId：[{}],channel: [{}]", userId, channel);
        }
    }
}
