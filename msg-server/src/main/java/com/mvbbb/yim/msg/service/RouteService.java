package com.mvbbb.yim.msg.service;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.constant.RedisConstant;
import com.mvbbb.yim.common.WsServerRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class RouteService {

    static Logger logger = LoggerFactory.getLogger(RouteService.class);

    @Resource(name = "jsonRedisTemplate")
    RedisTemplate<Object,Object> redisTemplate;

    public RouteService(RedisTemplate<Object,Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public WsServerRoute getUserWsServer(String userId){
        WsServerRoute wsServerRoute = ((WsServerRoute) redisTemplate.opsForValue().get(RedisConstant.STATUS_USER_ROUTE_PREFIX + userId));
        if(null == wsServerRoute){
            logger.error("user: [{}] not connect to WsServer",userId);
        }
        return wsServerRoute;
    }
}
