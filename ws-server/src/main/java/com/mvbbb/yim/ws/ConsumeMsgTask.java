package com.mvbbb.yim.ws;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.WsServerRoute;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.mq.MqManager;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ConsumeMsgTask implements Runnable {

    private Logger logger = LoggerFactory.getLogger(ConsumeMsgTask.class);

    @Resource
    MqManager mqManager;
    ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public void run() {
        logger.info("start listen mq");
        while(true){
            WsServerRoute wsServerRoute = new WsServerRoute(WsServerConfig.host,WsServerConfig.port);
            MsgData msgData = mqManager.consume(wsServerRoute);
            if(msgData==null){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                String recvUserId = msgData.getRecvUserId();
                Channel channel = connectionPool.findChannel(recvUserId);
                if(channel==null){
                    logger.error("channel for user not found, put this msg to mq. user: [{}], msg: [{}]",recvUserId,msgData);
                    mqManager.reDeliver(msgData);
                }else{
                    String msg = JSONObject.toJSONString(msgData);
                    TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(msg);
                    channel.writeAndFlush(textWebSocketFrame);
                    logger.info("send msg to user [{}]",recvUserId);
                }
            }
        }
    }
}
