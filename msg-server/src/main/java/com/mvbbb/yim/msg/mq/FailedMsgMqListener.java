package com.mvbbb.yim.msg.mq;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.constant.MqConstant;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.msg.MsgHandler;
import com.mvbbb.yim.msg.service.UserStatusService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: mvbbb
 */
@Component
public class FailedMsgMqListener {
    private static final Logger logger = LoggerFactory.getLogger(FailedMsgMqListener.class);
    @Resource
    MsgHandler msgHandler;
    @Resource
    UserStatusService userStatusService;
    @Value("${rocketmq.name-server}")
    private String nameServer;

    public void listen() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(MqConstant.GROUP_CONSUMER_FAILED_MSG);
        consumer.setNamesrvAddr(nameServer);
        try {
            consumer.subscribe(MqConstant.TOPIC_FAILED_MSG, "*");
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.setConsumeMessageBatchMaxSize(1);
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {

                for (Message msg : msgs) {
                    byte[] body = msg.getBody();
                    String json = new String(body);
                    logger.error("接收到MQ 中发送失败的消息：{}", json);

                    MsgData msgData = JSONObject.parseObject(json, MsgData.class);
                    // ws 反馈用户已经下线了，将改消息写入到离线表中
                    msgHandler.saveMsg(msgData);
                    userStatusService.offlineMsgOccur(msgData.getToUserId());
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}
