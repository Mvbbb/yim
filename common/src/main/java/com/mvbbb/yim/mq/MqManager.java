package com.mvbbb.yim.mq;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.WsServerRoute;
import com.mvbbb.yim.common.constant.RedisConstant;
import com.mvbbb.yim.common.protoc.MsgData;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Deprecated
@Service
public class MqManager {

    @Resource(name = "jsonRedisTemplate")
    RedisTemplate<Object,Object> redisTemplate;

    public void produce(WsServerRoute route, MsgData msgData){
        String topic = RedisConstant.MQ+":"+route.getIp()+":"+route.getPort();
        redisTemplate.opsForList().leftPush(topic,msgData);
    }

    public MsgData consume(WsServerRoute route){
        String topic = RedisConstant.MQ+":"+route.getIp()+":"+route.getPort();
        return ((MsgData) redisTemplate.opsForList().rightPop(topic));
    }

    public void reDeliver(MsgData msgData){
        String topic = RedisConstant.FAILED_DELIVER_MSG;
        redisTemplate.opsForList().leftPush(topic,msgData);
    }

    public MsgData consumeRedeliver(){
        return ((MsgData) redisTemplate.opsForList().rightPop(RedisConstant.FAILED_DELIVER_MSG));
    }
}
