package com.mvbbb.yim.ws.handler;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.protoc.Ack;
import com.mvbbb.yim.ws.ConnectionPool;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Service;

@Service
public class SendDataToUserHandler {

    ConnectionPool connectionPool = ConnectionPool.getInstance();

    void sendAckToUser(String userId,String msg){
        Channel channel = connectionPool.findChannel(userId);
        Ack ack = new Ack(msg);
        String json = JSONObject.toJSONString(ack);
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(json);
        try {
            channel.writeAndFlush(textWebSocketFrame).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
