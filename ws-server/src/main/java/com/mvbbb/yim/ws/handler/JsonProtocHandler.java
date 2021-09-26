package com.mvbbb.yim.ws.handler;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.protoc.Ack;
import com.mvbbb.yim.common.protoc.Bye;
import com.mvbbb.yim.common.protoc.DataPacket;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.request.GreetRequest;
import com.mvbbb.yim.ws.EventEnum;
import com.mvbbb.yim.ws.EventExecutor;
import com.mvbbb.yim.ws.pool.EventPool;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@ChannelHandler.Sharable
@Component
public class JsonProtocHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(JsonProtocHandler.class);
    private static final EventPool eventPool = EventPool.getInstance();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {

        String jsonText = textWebSocketFrame.text();
        logger.info("接收到 JSON 报文：{}", jsonText);
        DataPacket dataPacket = JSONObject.parseObject(jsonText, DataPacket.class);
        JSONObject data = (JSONObject) dataPacket.getData();

        switch (dataPacket.getCmdType()) {
            case MSG_DATA:
                MsgData msgData = data.toJavaObject(MsgData.class);
                EventExecutor.execute(eventPool.find(EventEnum.MSG_DATA), msgData, ctx);
                break;
            case BYE:
                Bye bye = data.toJavaObject(Bye.class);
                EventExecutor.execute(eventPool.find(EventEnum.BYE),bye,ctx);
                break;
            case GREET:
                GreetRequest greetRequest = data.toJavaObject(GreetRequest.class);
                EventExecutor.execute(eventPool.find(EventEnum.GREET),greetRequest,ctx);
                break;
            case ACK:
                Ack ack = data.toJavaObject(Ack.class);
                EventExecutor.execute(eventPool.find(EventEnum.ACK),ack,ctx);
                break;
            default:
                break;
        }
    }

}
