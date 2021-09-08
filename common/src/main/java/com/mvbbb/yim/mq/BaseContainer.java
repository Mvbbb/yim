package com.mvbbb.yim.mq;

import io.lettuce.core.RedisBusyException;
import io.lettuce.core.RedisCommandExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;


//@Component
@Service
public abstract class BaseContainer {

    private static final Logger logger = LoggerFactory.getLogger(BaseContainer.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 生成实际的
     * @param factory 工厂
     * @return StreamMessageListenerContainer
     */
    protected StreamMessageListenerContainer<String, ?> generate(RedisConnectionFactory factory) {
        // 初始化redis stream 的消费组
        initializeGroup();
        return generateContainer(factory);
    }

    /**
     * 初始化redis stream 的消费组
     */
    protected void initializeGroup() {}

    /**
     * generateContainer
     * @param factory 工厂
     * @return StreamMessageListenerContainer
     */
    protected abstract StreamMessageListenerContainer<String, ?> generateContainer(RedisConnectionFactory factory);

    /**
     * 初始化stream key 的消费组
     * @param key key
     * @param group 消费组
     */
    protected void createConsumerGroup(String key, String group) {
        try {
            stringRedisTemplate.opsForStream().createGroup(key, group);
        } catch (RedisSystemException e) {
            Class errorClass = e.getRootCause()==null?null:e.getRootCause().getClass();
            if (RedisBusyException.class.equals(errorClass)) {
                logger.info("STREAM - Redis group already exists, skipping Redis group creation");
            } else if (RedisCommandExecutionException.class.equals(errorClass)) {
                logger.info("STREAM - Stream does not yet exist, creating empty stream");
                stringRedisTemplate.opsForStream().add(key, Collections.singletonMap("", ""));
                stringRedisTemplate.opsForStream().createGroup(key, group);
            } else {
                throw e;
            }
        }
    }
}
