package com.mvbbb.yim.common.service;

import com.mvbbb.yim.common.constant.RedisConstant;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: mvbbb
 */
@Service
public class UnreadService {

    @Resource(name = "stringRedisTemplate")
    RedisTemplate<Object, Object> redisTemplate;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @SuppressWarnings("rawtypes")
    public void updateUnreadCount(String userId, SessionType sessionType, String sessionId, int offset) {
        /**
         * local offset = tonumber(ARGV[1])
         * local allUnreadKey = KEYS[1]
         * local sessionUnreadKey = KEYS[2]
         * if  offset < 0 then
         * 	redis.call("DECRBY", allUnreadKey, -offset)
         * 	redis.call("DECRBY", sessionUnreadKey, -offset)
         * else
         * 	redis.call("INCRBY", allUnreadKey, offset)
         * 	redis.call("INCRBY", sessionUnreadKey, offset)
         * end
         * return 0
         */
        String script = "local offset = tonumber(ARGV[1])\n" +
                "local allUnreadKey = KEYS[1]\n" +
                "local sessionUnreadKey = KEYS[2]\n" +
                "if offset < 0 then\n" +
                "\tredis.call(\"DECRBY\", allUnreadKey, -offset)\n" +
                "\tredis.call(\"DECRBY\", sessionUnreadKey, -offset)\n" +
                "else\n" +
                "\tredis.call(\"INCRBY\", allUnreadKey, offset)\n" +
                "\tredis.call(\"INCRBY\", sessionUnreadKey, offset)\n" +
                "end";

        DefaultRedisScript<Void> redisScript = new DefaultRedisScript<>(script);
        redisScript.setResultType(Void.class);
        redisTemplate.execute(redisScript, getKeys(userId, sessionType, sessionId), String.valueOf(offset));
    }

    public void clearSessionUnreadCount(String userId, SessionType sessionType, String sessionId) {
        /**
         * local allUnreadKey = KEYS[1]
         * local sessionUnreadKey = KEYS[2]
         * local sessionUnreadCount = tonumber(redis.call("GET", sessionUnreadKey))
         * redis.call("SET", sessionUnreadKey, 0)
         * redis.call("DECRBY", allUnreadKey, sessionUnreadCount)
         */
        String script = "local allUnreadKey = KEYS[1]\n" +
                "local sessionUnreadKey = KEYS[2]\n" +
                "local sessionUnreadCount = tonumber(redis.call(\"GET\", sessionUnreadKey))\n" +
                "redis.call(\"SET\", sessionUnreadKey, 0)\n" +
                "redis.call(\"DECRBY\", allUnreadKey, sessionUnreadCount)";
        DefaultRedisScript<Void> redisScript = new DefaultRedisScript<>(script);
        redisTemplate.execute(redisScript, getKeys(userId, sessionType, sessionId));
    }

    public long getAllUnreadCount(String userId) {
        String key = RedisConstant.UNREAD_COUNT_ALL + userId;
        String count = (String) redisTemplate.opsForValue().get(key);
        if (count == null || "".equals(count)) {
            return 0;
        }
        return Long.parseLong(count);
    }

    private List<Object> getKeys(String userId, SessionType sessionType, String sessionId) {
        String allUnreadCountKey = RedisConstant.UNREAD_COUNT_ALL + userId;
        String sessionUnreadCountKey = null;
        if (sessionType == SessionType.GROUP) {
            sessionUnreadCountKey = RedisConstant.UNREAD_COUNT_SESSION + "group:" + userId + ":" + sessionId;
        } else {
            sessionUnreadCountKey = RedisConstant.UNREAD_COUNT_SESSION + "single:" + userId + ":" + sessionId;
        }
        List<Object> keys = new ArrayList<>();
        keys.add(allUnreadCountKey);
        keys.add(sessionUnreadCountKey);
        return keys;
    }

}
