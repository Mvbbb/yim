package com.mvbbb.yim.mq;

import com.mvbbb.yim.common.WsServerRoute;
import com.mvbbb.yim.common.constant.RedisConstant;
import com.mvbbb.yim.common.protoc.MsgData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisStreamManager {

    private static final Logger logger = LoggerFactory.getLogger(RedisStreamManager.class);

    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 将消息投放到 ws server 的消息队列中
     * @param route ws server 的路由
     * @param msgData 消息
     */
    public void produce(WsServerRoute route, MsgData msgData){
        String key = RedisConstant.STREAM_DELIVER_WS_PREFIX + route.getIp()+ ":" + route.getPort();
        ObjectRecord<String, MsgData> record = Record.of(msgData).withStreamKey(key);
        RecordId recordId = stringRedisTemplate.opsForStream().add(record);
        // print log
    }

    /**
     * @param msgData 失败的消息
     */
    public void failedDeliveredMsg(MsgData msgData){
        String key = RedisConstant.STREAM_FAILED_MSG;
        ObjectRecord<String, MsgData> record = Record.of(msgData).withStreamKey(key);
        stringRedisTemplate.opsForStream().add(record);
        logger.error("消息发送失败，存入离线消息表 {}",msgData);
    }
}
