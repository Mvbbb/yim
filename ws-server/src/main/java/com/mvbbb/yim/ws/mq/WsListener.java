package com.mvbbb.yim.ws.mq;


import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.mq.RedisStreamManager;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.ws.ConnectionPool;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class WsListener implements StreamListener<String, ObjectRecord<String, MsgData>> {

    private final Logger logger = LoggerFactory.getLogger(WsListener.class);
    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Resource
    RedisStreamManager redisStreamManager;

    @Override
    public void onMessage(ObjectRecord<String, MsgData> message) {
        // 消息ID
        RecordId messageId = message.getId();
        MsgData msgData = message.getValue();
        logger.info("接收到消息 # consumer # messageId={}, stream={}, value = {}", messageId, message.getStream(), msgData);
        String recvUserId = msgData.getRecvUserId();
        Channel channel = connectionPool.findChannel(recvUserId);
        if(channel==null){
            logger.error("指定用户的 channel 没找到，将消息投放到消息队列中. user: [{}], msg: [{}]",recvUserId,msgData);
            redisStreamManager.reDeliver(msgData);
        }else{
            //发送 json 文本
            String msg = JSONObject.toJSONString(msgData);
            TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(msg);
            channel.writeAndFlush(textWebSocketFrame);

//                    Protobuf.DataPacket dataPacket = ProtobufDataPacketUtil.buildMsgData(msgData);
//                    channel.writeAndFlush(dataPacket);
            logger.info("发送消息给用户 [{}]",recvUserId);
        }
    }
}