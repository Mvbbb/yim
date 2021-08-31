package com.mvbbb.yim.mq;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.WsServerRoute;
import com.mvbbb.yim.common.constant.RedisConstant;
import com.mvbbb.yim.common.protoc.MsgData;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

public class MqManager {

    RedisTemplate<String,String> redisTemplate;

    public MqManager(RedisTemplate<String,String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void produce(WsServerRoute route, MsgData msgData){
        String topic = RedisConstant.MQ+":"+route.getIp()+":"+route.getPort();
        String msg = JSONObject.toJSONString(msgData);
        redisTemplate.opsForList().leftPush(topic,msg);
    }

    public MsgData consume(WsServerRoute route){
        String topic = RedisConstant.MQ+":"+route.getIp()+":"+route.getPort();
        String msg = redisTemplate.opsForList().rightPop(topic);
        if(StringUtils.isEmpty(msg)){
            return null;
        }
        return JSONObject.parseObject(msg,MsgData.class);
    }

}
