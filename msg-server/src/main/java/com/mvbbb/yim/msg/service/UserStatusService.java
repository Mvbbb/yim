package com.mvbbb.yim.msg.service;

import com.mvbbb.yim.common.constant.RedisConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserStatusService {

    @Resource(name = "jsonRedisTemplate")
    RedisTemplate<Object, Object> redisTemplate;

    public boolean isUserOnline(String userId) {
        return redisTemplate.opsForValue().get(RedisConstant.STATUS_USER_ROUTE_PREFIX + userId) != null;
    }

    /**
     * 出现离线消息
     */
    public void offlineMsgOccur(String userId) {
        redisTemplate.opsForValue().set(RedisConstant.OFFLINE_MSG_NOT_POOL_OVER_PREFIX + userId, 1);
    }
}
