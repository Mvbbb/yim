package com.mvbbb.yim.common.mq;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.WsServerRoute;
import com.mvbbb.yim.common.constant.RedisConstant;
import com.mvbbb.yim.common.protoc.MsgData;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

public class MqManager {

    RedisTemplate<Object,Object> redisTemplate;

    public MqManager(RedisTemplate<Object,Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void produce(WsServerRoute route, MsgData msgData){
        String topic = RedisConstant.MQ+":"+route.getIp()+":"+route.getPort();
        String msg = JSONObject.toJSONString(msgData);
        redisTemplate.opsForList().leftPush(topic,msg);
    }

    public MsgData consume(WsServerRoute route){
        String topic = RedisConstant.MQ+":"+route.getIp()+":"+route.getPort();
        String msg = ((String) redisTemplate.opsForList().rightPop(topic));
        if(StringUtils.isEmpty(msg)){
            return null;
        }
        return JSONObject.parseObject(msg,MsgData.class);
    }

}
