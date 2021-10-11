package com.mvbbb.yim.ws.mq;

import com.mvbbb.yim.common.constant.MqConstant;
import com.mvbbb.yim.common.protoc.MsgData;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: mvbbb
 */
@Component
public class MqSender {

    private static final Logger logger = LoggerFactory.getLogger(MqSender.class);

    @Resource
    RocketMQTemplate mqTemplate;

    /**
     * @param msgData 失败的消息
     */
    public void failedDeliveredMsg(MsgData msgData) {

        logger.error("消息投递失败，将消息放到失败的消息队列：{}", msgData);
        mqTemplate.send(MqConstant.TOPIC_FAILED_MSG, MessageBuilder.withPayload(msgData).build());

    }

}
