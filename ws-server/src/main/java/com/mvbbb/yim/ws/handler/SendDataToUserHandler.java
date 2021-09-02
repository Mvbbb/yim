package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.common.protoc.Ack;
import com.mvbbb.yim.ws.ConnectionPool;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

@Service
public class SendDataToUserHandler {

    ConnectionPool connectionPool = ConnectionPool.getInstance();

    void sendAckToUser(String userId,String msg){
        Channel channel = connectionPool.findChannel(userId);
        Ack ack = new Ack(msg);
        try {
            channel.writeAndFlush(ack).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
