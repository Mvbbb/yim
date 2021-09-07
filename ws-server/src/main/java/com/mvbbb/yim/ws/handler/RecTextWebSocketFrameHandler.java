package com.mvbbb.yim.ws.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mvbbb.yim.common.protoc.DataPacket;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.CmdType;
import com.mvbbb.yim.common.protoc.ws.request.GreetRequest;
import com.mvbbb.yim.common.protoc.ws.request.ByeRequest;
import com.mvbbb.yim.msg.service.MsgService;
import com.mvbbb.yim.ws.service.MsgTransfer;
import com.mvbbb.yim.ws.service.StatusService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@ChannelHandler.Sharable
public class RecTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    Logger logger = LoggerFactory.getLogger(RecTextWebSocketFrameHandler.class);

    @DubboReference(check = false)
    MsgService msgService;
    @Resource
    StatusService statusHandler;
    @Resource
    MsgTransfer msgHandler;

    private static RecTextWebSocketFrameHandler recFrameHandler;


    @PostConstruct
    public void init(){
        recFrameHandler = this;
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
                recFrameHandler.statusHandler.auth(channelHandlerContext.channel(),authRequest);
                break;
            case BYE:
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
            case ACK:
                // todo
                break;
            default: break;
        }
    }
}
