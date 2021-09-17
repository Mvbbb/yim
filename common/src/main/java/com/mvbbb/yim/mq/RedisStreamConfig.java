package com.mvbbb.yim.mq;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@Component
@Service
public class RedisStreamConfig {

    @Resource
    private Map<String, StreamMessageListenerContainer<String, ?>> listenerContainerMap;
    /**
     * 监听容器列表
     */
    private List<StreamMessageListenerContainer<String, ?>> containerList = new ArrayList<>();

    /**
     * 初始化redis stream 的消费组
     */
    private void initializeGroup() {
    }

    @Bean
    public Subscription subscription(RedisConnectionFactory factory) {

        // 初始化各个stream的消费组
        initializeGroup();

        // 注册监听容器
        registerContainer();

        // 启动所有的容器
        containerList.forEach(StreamMessageListenerContainer::start);

        return null;
    }

    private void registerContainer() {
        containerList.addAll(listenerContainerMap.values());
    }

}
