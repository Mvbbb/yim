package com.mvbbb.yim.msg.mq;

import com.mvbbb.yim.common.constant.RedisConstant;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.mq.BaseContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * TODO 优化 ack 机制，确保消息成功消费
 */
@Component
public class FailMsgListenerContainer extends BaseContainer {

    private static final String STREAM_FAILMSG_KEY = RedisConstant.STREAM_FAILED_MSG;
    private final Logger logger = LoggerFactory.getLogger(FailMsgListenerContainer.class);
    private final String CONSUMER_GROUP = RedisConstant.STREAM_FAILED_MSG_CONSUMER_GROUP_NAME;
    private final String CONSUMER_NAME = RedisConstant.STREAM_FAILED_MSG_CONSUMER_NAME;

    @Resource
    FailMsgListener failMsgListener;

    @Override
    protected void initializeGroup() {
        createConsumerGroup(STREAM_FAILMSG_KEY, CONSUMER_GROUP);
    }

    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, MsgData>> wsListenerContainer(RedisConnectionFactory factory) {
        return (StreamMessageListenerContainer<String, ObjectRecord<String, MsgData>>) generate(factory);
    }

    @Override
    protected StreamMessageListenerContainer<String, ?> generateContainer(RedisConnectionFactory factory) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, MsgData>> options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(0))
                .targetType(MsgData.class)
                .batchSize(10)
                .build();
        StreamMessageListenerContainer<String, ObjectRecord<String, MsgData>> container = StreamMessageListenerContainer.create(factory, options);
        container.receiveAutoAck(Consumer.from(CONSUMER_GROUP, CONSUMER_NAME),
                StreamOffset.create(STREAM_FAILMSG_KEY, ReadOffset.lastConsumed()), failMsgListener);
        return container;
    }
}
