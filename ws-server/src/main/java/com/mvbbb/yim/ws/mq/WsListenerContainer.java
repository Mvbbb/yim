package com.mvbbb.yim.ws.mq;

import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.mq.BaseContainer;
import com.mvbbb.yim.ws.WsServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;

@Component
@SuppressWarnings("unchecked")
public class WsListenerContainer extends BaseContainer {

    @Resource
    WsServerConfig wsServerConfig;

    @Resource
    private WsListener wsListener;

    @Override
    protected void initializeGroup() {
        createConsumerGroup(wsServerConfig.getRedisStreamKey(), wsServerConfig.getRedisStreamConsumerGroupName());
    }

    @Bean(name = "wsContainer")
    public StreamMessageListenerContainer<String, ObjectRecord<String, MsgData>> wsListenerContainer(RedisConnectionFactory factory) {
        return (StreamMessageListenerContainer<String, ObjectRecord<String, MsgData>>) generate(factory);
    }

    @Override
    public StreamMessageListenerContainer<String, ObjectRecord<String, MsgData>> generateContainer(RedisConnectionFactory factory) {
        // 管理员监听配置
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, MsgData>> wsOptions = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder()
                // 一直不过期
                .pollTimeout(Duration.ofSeconds(0))
                // 目标类型
                .targetType(MsgData.class)
                // 每次最多取的条数
                .batchSize(10)
                .build();
        StreamMessageListenerContainer<String, ObjectRecord<String, MsgData>> wsContainer = StreamMessageListenerContainer.create(factory, wsOptions);
        wsContainer.receiveAutoAck(
                Consumer.from(wsServerConfig.getRedisStreamConsumerGroupName(), wsServerConfig.getRedisStreamConsumerName()),
                StreamOffset.create(wsServerConfig.getRedisStreamKey(), ReadOffset.lastConsumed()),
                wsListener);
        return wsContainer;
    }
}
