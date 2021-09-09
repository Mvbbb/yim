package com.mvbbb.yim.msg.mq;

import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.msg.MsgHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class FailMsgListener implements StreamListener<String, ObjectRecord<String, MsgData>> {

    private static final Logger logger = LoggerFactory.getLogger(FailMsgListener.class);

    @Resource
    MsgHandler msgHandler;

    @Override
    public void onMessage(ObjectRecord<String, MsgData> message) {
        RecordId id = message.getId();
        String stream = message.getStream();
        MsgData msgData = message.getValue();
        logger.info("从 redis stream 中读取到发送失败的消息。 stream: {}, id: {}, msgData: {}",stream,id,msgData);
        // 这个消息是投放给用户的消息，只需要再发送一次
        // FIXME 重发失败机制完善
        msgHandler.sendSingleMsg(msgData);
    }
}
