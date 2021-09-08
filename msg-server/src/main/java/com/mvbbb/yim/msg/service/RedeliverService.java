package com.mvbbb.yim.msg.service;

import com.mvbbb.yim.common.protoc.MsgData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Deprecated
@Service
public class RedeliverService {

    private static final Logger logger = LoggerFactory.getLogger(RedeliverService.class);
    @Resource
    MsgService msgService;

    public void redeliver(MsgData msgData){
        logger.info("正在重发消息");
        msgService.handlerMsgData(msgData);
    }
}
