package com.mvbbb.yim.ws.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mvbbb.yim.common.protoc.Ack;
import com.mvbbb.yim.common.protoc.DataPacket;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.CmdType;
import com.mvbbb.yim.common.protoc.ws.request.GreetRequest;
import com.mvbbb.yim.common.protoc.ws.request.ByeRequest;
import com.mvbbb.yim.ws.service.AckService;
import com.mvbbb.yim.ws.service.MsgTransfer;
import com.mvbbb.yim.ws.service.StatusService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@ChannelHandler.Sharable
public class JsonDataPacketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    Logger logger = LoggerFactory.getLogger(JsonDataPacketHandler.class);

    @Resource
    StatusService statusHandler;
    @Resource
    MsgTransfer msgHandler;
    @Resource
    AckService ackService;

    private static JsonDataPacketHandler dataPacketHandler;


    @PostConstruct
    public void init(){
        dataPacketHandler = this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame frame) throws Exception {

        String msgText = frame.text();
        JSONObject jsonObject = JSONObject.parseObject(msgText);

        CmdType cmdId = CmdType.valueOf(jsonObject.getString("cmdId"));
        switch (cmdId){
            case GREET:
                // greet request
                DataPacket<GreetRequest> msgDataDataPacket = JSONObject.parseObject(msgText, new TypeReference<DataPacket<GreetRequest>>() {
                });
                GreetRequest authRequest = msgDataDataPacket.getData();
                dataPacketHandler.statusHandler.greet(channelHandlerContext.channel(),authRequest);
                break;
            case BYE:
                DataPacket<ByeRequest> byeRequestDataPacket = JSONObject.parseObject(msgText, new TypeReference<DataPacket<ByeRequest>>() {
                });
                ByeRequest byeRequestDataPacketData = byeRequestDataPacket.getData();
                dataPacketHandler.statusHandler.bye(channelHandlerContext.channel(),byeRequestDataPacketData);
                break;
            case MSG_DATA:
                //msg data
                DataPacket<MsgData> msgDataDataPacket1 = JSONObject.parseObject(msgText, new TypeReference<DataPacket<MsgData>>() {
                });
                dataPacketHandler.msgHandler.sendMsg(msgDataDataPacket1.getData());
                break;
            case ACK:
                DataPacket<Ack> ackDataPacket = JSONObject.parseObject(msgText, new TypeReference<DataPacket<Ack>>() {
                });
                dataPacketHandler.ackService.recvAck(ackDataPacket.getData());
                break;
            default: break;
        }
    }
}
