package com.mvbbb.yim.msg.service;

import com.mvbbb.yim.common.entity.MsgSend;
import com.mvbbb.yim.common.mapper.MsgSendMapper;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.util.BeanConvertor;
import com.mvbbb.yim.msg.MsgHandler;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

@DubboService
public class MsgServiceImpl implements MsgService {

    private static final Logger logger = LoggerFactory.getLogger(MsgServiceImpl.class);
    @Resource
    MsgHandler msgHandler;
    @Resource
    MsgSendMapper msgSendMapper;

    @Override
    public void handlerMsgData(MsgData msgData) {

        // 持久化消息
        MsgSend msgSend = BeanConvertor.msgDataToMsgSend(msgData);
        logger.info("持久化消息到到表中：{}", msgData);
        msgSendMapper.insert(msgSend);

        // 发送消息
        switch (msgData.getSessionType()) {
            case SINGLE:
                logger.info("发送私聊消息。MsgData:{}", msgData);
                msgHandler.sendSingleMsg(msgData, false);
                break;
            case GROUP:
                logger.info("发送群聊消息。MsgData:{}", msgData);
                msgHandler.sendGroupMsg(msgData);
                break;
            default:
                logger.error("请提供消息类型");
                break;
        }
    }

}
