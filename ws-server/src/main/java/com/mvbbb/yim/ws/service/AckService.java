package com.mvbbb.yim.ws.service;

import com.mvbbb.yim.common.protoc.Ack;
import com.mvbbb.yim.ws.MsgAckPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AckService {
    private static Logger logger = LoggerFactory.getLogger(AckService.class);
    @Resource
    MsgSendService msgSendService;
    private MsgAckPool msgAckPool = MsgAckPool.getInstance();

    public void recvAck(Ack data) {
        String key = data.getServerMsgId() + ":" + data.getUserId();
        logger.info("消息投递成功 {}", key);
        msgAckPool.acked(key);
        msgSendService.sendAckToUser(data.getUserId(), "服务器接收到消息");
    }
}
