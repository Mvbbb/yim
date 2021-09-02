package com.mvbbb.yim.ws.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mvbbb.yim.common.protoc.DataPacket;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.CmdIdEnum;
import com.mvbbb.yim.common.protoc.ws.request.GreetRequest;
import com.mvbbb.yim.common.protoc.ws.request.ByeRequest;
import com.mvbbb.yim.msg.service.MsgService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class RecFrameHandler  extends SimpleChannelInboundHandler<WebSocketFrame> {
    @DubboReference
    MsgService msgService;
    @Resource
    StatusHandler statusHandler;
    @Resource
    MsgHandler msgHandler;

    private static RecFrameHandler recFrameHandler;

    @PostConstruct
    public void init(){
        recFrameHandler = this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame frame) throws Exception {

        Logger logger = LoggerFactory.getLogger(RecFrameHandler.class);

        String msgText = ((TextWebSocketFrame) frame).text();
        JSONObject jsonObject = JSONObject.parseObject(msgText);

        CmdIdEnum cmdId = CmdIdEnum.valueOf(jsonObject.getString("cmdId"));
        switch (cmdId){
            case GREET_REQ:
                // greet request
                DataPacket<GreetRequest> msgDataDataPacket = JSONObject.parseObject(msgText, new TypeReference<DataPacket<GreetRequest>>() {
                });
                GreetRequest authRequest = msgDataDataPacket.getData();
                recFrameHandler.statusHandler.auth(channelHandlerContext.channel(),authRequest);
                break;
            case BYE_REQ:
                DataPacket<ByeRequest> byeRequestDataPacket = JSONObject.parseObject(msgText, new TypeReference<DataPacket<ByeRequest>>() {
                });
                ByeRequest byeRequestDataPacketData = byeRequestDataPacket.getData();
                recFrameHandler.statusHandler.bye(channelHandlerContext.channel(),byeRequestDataPacketData);
                break;
            case MSG_DATA:
                //msg data
                DataPacket<MsgData> msgDataDataPacket1 = JSONObject.parseObject(msgText, new TypeReference<DataPacket<MsgData>>() {
                });
                recFrameHandler.msgHandler.sendMsg(msgDataDataPacket1.getData());
                break;
            case MSG_DATA_ACK:
                // todo
                break;
            default: break;
        }
    }
}
