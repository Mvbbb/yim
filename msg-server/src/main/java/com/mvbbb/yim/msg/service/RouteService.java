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

    @Resource
    RedisTemplate<String,String> redisTemplate;

    public RouteService(RedisTemplate<String,String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public WsServerRoute getUserWsServer(String userId){
        String wsRoute = redisTemplate.opsForValue().get(RedisConstant.STATUS_USER_ROUTE_PREFIX + userId);
        WsServerRoute wsServerRoute = JSONObject.parseObject(wsRoute, WsServerRoute.class);
        if(null == wsServerRoute){
            logger.error("user: [{}] not connect to WsServer",userId);
        }
        return wsServerRoute;
    }
}
