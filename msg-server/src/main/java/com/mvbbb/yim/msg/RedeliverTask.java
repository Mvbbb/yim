package com.mvbbb.yim.msg;

import com.mvbbb.yim.common.mq.MqManager;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.msg.service.MsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedeliverTask implements Runnable{

    private final Logger logger = LoggerFactory.getLogger(RedeliverTask.class);

    @Resource
    MqManager mqManager;
    @Resource
    MsgService msgService;

    @Override
    public void run() {
        while (true){
            MsgData msgData = mqManager.consumeRedeliver();
            if(msgData==null){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                logger.info("redeliver msg. msgData: [{}]",msgData);
                msgService.handlerMsgData(msgData);
            }
        }
    }
}
