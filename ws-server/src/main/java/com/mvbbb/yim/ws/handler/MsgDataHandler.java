package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.util.SnowflakeIdWorker;
import com.mvbbb.yim.msg.service.MsgService;
import com.mvbbb.yim.ws.ConnectionPool;
import com.mvbbb.yim.ws.service.MsgSendService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@ChannelHandler.Sharable
public class MsgDataHandler extends SimpleChannelInboundHandler<MsgData> {

    private static final Logger logger = LoggerFactory.getLogger(MsgDataHandler.class);
    private static MsgDataHandler msgDataHandler;
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    @DubboReference(check = false)
    MsgService msgService;
    @Resource
    MsgSendService sendDataToUserHandler;

    @PostConstruct
    public void init(){
        msgDataHandler = this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MsgData data) throws Exception {

        Channel channel = ctx.channel();
        if(connectionPool.checkToClose(ctx.channel())){
            return;
        };

        String userId = connectionPool.getUseridByChannel(channel);
        data.setFromUserId(userId);

        logger.info("收到 msg data 消息：{}",data);
        data.setServerMsgId(SnowflakeIdWorker.generateId());
        msgDataHandler.sendDataToUserHandler.sendAckToUser(data.getFromUserId(), "Ws Server receive msg.");
        msgDataHandler.msgService.handlerMsgData(data);
    }
}
