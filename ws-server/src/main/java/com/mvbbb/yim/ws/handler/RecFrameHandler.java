package com.mvbbb.yim.ws.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mvbbb.yim.common.protoc.DataPacket;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.request.AuthRequest;
import com.mvbbb.yim.ws.ConnectionPool;
import com.mvbbb.yim.msg.service.MsgService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RecFrameHandler  extends SimpleChannelInboundHandler<WebSocketFrame> {
    @DubboReference
    MsgService msgService;
    @Resource
    AuthHandler authHandler;
    @Resource
    MsgHandler msgHandler;

    ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame frame) throws Exception {

        Logger logger = LoggerFactory.getLogger(RecFrameHandler.class);

        String msgText = ((TextWebSocketFrame) frame).text();
        JSONObject jsonObject = JSONObject.parseObject(msgText);
        Integer cmdId = jsonObject.getInteger("cmdId");
        switch (cmdId){
            case 2:
                // auth request
                DataPacket<AuthRequest> msgDataDataPacket = JSONObject.parseObject(msgText, new TypeReference<DataPacket<AuthRequest>>() {
                });
                boolean tokenValid = authHandler.auth(msgDataDataPacket.getData());
                if(!tokenValid){
                    logger.error("user token is wrong {}",msgDataDataPacket.getData().getToken());
                    // todo return error info
                    channelHandlerContext.channel().close();
                }else{
                    logger.info("add user {} channel {}",msgDataDataPacket.getData().getUserId(),channelHandlerContext.channel());
                    connectionPool.addUserChannel(channelHandlerContext.channel(),msgDataDataPacket.getData().getUserId());
                }
                break;
            case 4:
                //msg data
                DataPacket<MsgData> msgDataDataPacket1 = JSONObject.parseObject(msgText, new TypeReference<DataPacket<MsgData>>() {
                });
                msgHandler.sendMsg(msgDataDataPacket1.getData());
                break;
            default: break;
        }

        DataPacket<MsgData> msgDataDataPacket = JSONObject.parseObject(msgText, new TypeReference<DataPacket<MsgData>>() {
        });
        msgService.handlerMsgData(msgDataDataPacket.getData());
    }

}
