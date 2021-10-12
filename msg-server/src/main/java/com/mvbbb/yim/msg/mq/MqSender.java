package com.mvbbb.yim.msg.mq;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.WsServerRoute;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.util.MqUtil;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author: mvbbb
 */
@Component
public class MqSender {

    @Resource
    RocketMQTemplate mqTemplate;

    public void produce(WsServerRoute route, MsgData msgData) {

        String topic = MqUtil.getWsTopicName(route.getIp(), route.getPort());
        String json = JSONObject.toJSONString(msgData);
        mqTemplate.send(topic, MessageBuilder.withPayload(json.getBytes(StandardCharsets.UTF_8)).build());
    }
}
