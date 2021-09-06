package com.mvbbb.yim.ws.handler;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.protoc.Ack;
import com.mvbbb.yim.ws.ConnectionPool;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SendDataToUserHandler {

    private static final Logger logger = LoggerFactory.getLogger(SendDataToUserHandler.class);

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    public void sendAckToUser(String userId,String msg){
        Channel channel = connectionPool.findChannel(userId);
        if(channel==null){
            logger.error("连接未建立");
            return;
        }
        Ack ack = new Ack(msg);
        String json = JSONObject.toJSONString(ack);
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(json);
        try {
            channel.writeAndFlush(textWebSocketFrame).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void send(Channel channel,String msg){
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(msg);
        try{
            channel.writeAndFlush(textWebSocketFrame).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
