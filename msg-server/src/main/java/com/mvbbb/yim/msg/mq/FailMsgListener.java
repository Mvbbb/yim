package com.mvbbb.yim.msg.mq;

import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.msg.MsgHandler;
import com.mvbbb.yim.msg.service.UserStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class FailMsgListener implements StreamListener<String, ObjectRecord<String, MsgData>> {

    private static final Logger logger = LoggerFactory.getLogger(FailMsgListener.class);

    @Resource
    MsgHandler msgHandler;
    @Resource
    UserStatusService userStatusService;

    @Override
    public void onMessage(ObjectRecord<String, MsgData> message) {
        RecordId id = message.getId();
        String stream = message.getStream();
        MsgData msgData = message.getValue();
        logger.info("从 redis stream 中读取到发送失败的消息,将消息存入到离线消息表中。 stream: {}, id: {}, msgData: {}", stream, id, msgData);
        // ws 反馈用户已经下线了，将改消息写入到离线表中
        msgHandler.saveMsg(msgData);
        userStatusService.offlineMsgOccur(msgData.getRecvUserId());
    }
}
