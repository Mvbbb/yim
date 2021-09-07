package com.mvbbb.yim.ws.service;

import com.mvbbb.yim.ws.ConnectionPool;
import io.netty.channel.Channel;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;


@DubboService
public class WsUserStatusServiceImpl implements WsUserStatusService{

    private static final Logger logger = LoggerFactory.getLogger(WsUserStatusServiceImpl.class);

    ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Resource
    SendDataToUserService sendDataToUserHandler;
    @Resource
    StatusService statusHandler;

    @Override
    public void kickout(String userId) {
        Channel channel = connectionPool.findChannel(userId);
        if(channel==null){
            logger.error("channel is null");
            return ;
        }

        sendDataToUserHandler.send(channel,"异地登录，本机下线");
        connectionPool.removeConnection(channel,userId);
        logger.error("user channel was removed from connection pool, because another login event. userId: [{}], channel: [{}]",userId,channel);

        try {
            channel.close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userOffline(String userId) {
        Channel channel = connectionPool.findChannel(userId);
        if(channel==null){
            logger.error("channel is null");
            return ;
        }
        statusHandler.bye(channel,userId);
    }
}
