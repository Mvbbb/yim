package com.mvbbb.yim.ws.mq;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.logic.service.UserStatusService;
import com.mvbbb.yim.ws.WsServerConfig;
import com.mvbbb.yim.ws.pool.ConnectionPool;
import com.mvbbb.yim.ws.service.MsgSendService;
import io.netty.channel.Channel;
import org.apache.dubbo.config.annotation.DubboReference;
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
public class DeliverMsgMqListener {

    private static final Logger logger = LoggerFactory.getLogger(DeliverMsgMqListener.class);
    @Resource
    WsServerConfig wsServerConfig;
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    @Resource
    MqSender mqSender;
    @Resource
    MsgSendService msgSendService;
    @DubboReference(check = false)
    UserStatusService userStatusService;
    @Value("${rocketmq.name-server}")
    private String nameServer;

    public void listen() {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(wsServerConfig.getMqConsumerGroupName());
        consumer.setNamesrvAddr(nameServer);
        try {
            consumer.subscribe(wsServerConfig.getMqTopicName(), "*");
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.setConsumeMessageBatchMaxSize(1);
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                for (Message msg : msgs) {
                    logger.info("从 MQ 中接收到待投递消息：{}", msg);
                    byte[] body = msg.getBody();
                    String json = new String(body);
                    MsgData msgData = JSONObject.parseObject(json, MsgData.class);
                    String toUserId = msgData.getToUserId();
                    Channel channel = connectionPool.findChannel(toUserId);
                    if (channel == null) {
                        logger.error("指定用户的 channel 没找到，将用户离线. user: [{}], msg: [{}]", toUserId, msgData);
                        mqSender.failedDeliveredMsg(msgData);
                        userStatusService.userOffline(toUserId);
                    } else {
                        //发送 json 文本
                        msgSendService.sendMsg(channel, msgData);
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

    }


}
