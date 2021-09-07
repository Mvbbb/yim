package com.mvbbb.yim.ws.service;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.protoc.Ack;
import com.mvbbb.yim.common.protoc.Protobuf;
import com.mvbbb.yim.ws.ConnectionPool;
import com.mvbbb.yim.ws.ProtobufDataPacketUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 向用户发送信息
 */
@Service
public class SendDataToUserService {

    private static final Logger logger = LoggerFactory.getLogger(SendDataToUserService.class);

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    public void sendAckToUser(String userId,String msg){
        Channel channel = connectionPool.findChannel(userId);
        if(channel==null){
            logger.error("连接未建立");
            return;
        }
        Ack ack = new Ack(msg);
//        Protobuf.DataPacket dataPacket = ProtobufDataPacketUtil.buildAck(ack);
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(JSONObject.toJSONString(ack));
        try {
//            channel.writeAndFlush(dataPacket).sync();
            channel.writeAndFlush(textWebSocketFrame).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * FIXME 添加消息下行通知协议
     */
    public void send(Channel channel,String msg){
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(msg);
//        Ack ack = new Ack(msg);
//        Protobuf.DataPacket dataPacket = ProtobufDataPacketUtil.buildAck(ack);
        try{
//            channel.writeAndFlush(dataPacket).sync();
            channel.writeAndFlush(textWebSocketFrame).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
