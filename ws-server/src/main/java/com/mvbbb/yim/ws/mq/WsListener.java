package com.mvbbb.yim.ws.mq;


import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.entity.MsgSend;
import com.mvbbb.yim.mq.RedisStreamManager;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.ws.ConnectionPool;
import com.mvbbb.yim.ws.service.MsgSendService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消息重发机制：
 * 发送一条消息，开启一个时间为 1s 的定时任务，如果没有收到用户的 ack 消息就再发 1 次
 * 重复上面的过程 3次
 * 一个消息的唯一性是 server_msg_id:recv_user_id 组成的
 */
@Component
public class WsListener implements StreamListener<String, ObjectRecord<String, MsgData>> {

    private final Logger logger = LoggerFactory.getLogger(WsListener.class);
    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Resource
    RedisStreamManager redisStreamManager;
    @Resource
    MsgSendService msgSendService;

    /**
     * 从 redis stream 中读取到消息并发送给用户
     */
    @Override
    public void onMessage(ObjectRecord<String, MsgData> message) {
        // 消息ID
        RecordId messageId = message.getId();
        MsgData msgData = message.getValue();
        logger.info("接收到消息 # consumer # messageId={}, stream={}, value = {}", messageId, message.getStream(), msgData);
        String recvUserId = msgData.getRecvUserId();
        Channel channel = connectionPool.findChannel(recvUserId);
        if(channel==null){
            logger.error("指定用户的 channel 没找到，将用户离线. user: [{}], msg: [{}]",recvUserId,msgData);
            redisStreamManager.failedDeliveredMsg(msgData);
        }else{
            //发送 json 文本
            msgSendService.sendMsg(channel,msgData);
        }
    }
}