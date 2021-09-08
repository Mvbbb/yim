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

    public void produce(WsServerRoute route, MsgData msgData){
        // todo
        String key = RedisConstant.STREAM_DELIVER_WS_PREFIX + route.getIp()+ ":" + route.getPort();
        ObjectRecord<String, MsgData> record = Record.of(msgData).withStreamKey(key);
        RecordId recordId = stringRedisTemplate.opsForStream().add(record);
        // print log
    }

    public void reDeliver(MsgData msgData){
        String key = RedisConstant.STREAM_FAILED_MSG;
        ObjectRecord<String, MsgData> record = Record.of(msgData).withStreamKey(key);
        stringRedisTemplate.opsForStream().add(record);
        logger.error("投放消息到失败队列中 {}",msgData);
    }
}
