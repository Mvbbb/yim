package com.mvbbb.yim.ws.handler;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.protoc.Ack;
import com.mvbbb.yim.common.protoc.Bye;
import com.mvbbb.yim.common.protoc.DataPacket;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.request.GreetRequest;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.util.List;

@ChannelHandler.Sharable
@Component
public class JsonProtocHandler extends MessageToMessageDecoder<TextWebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(JsonProtocHandler.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame, List<Object> out) throws Exception {

        String jsonText = textWebSocketFrame.text();
        logger.info("接收到 JSON 报文：{}",jsonText);
        DataPacket dataPacket = JSONObject.parseObject(jsonText, DataPacket.class);
        JSONObject data = (JSONObject) dataPacket.getData();
        switch (dataPacket.getCmdType()) {
            case MSG_DATA:
                MsgData msgData = data.toJavaObject(MsgData.class);
                out.add(msgData);
                break;
            case BYE:
                Bye bye = data.toJavaObject(Bye.class);
                out.add(bye);
                break;
            case GREET:
                GreetRequest greetRequest = data.toJavaObject(GreetRequest.class);
                out.add(greetRequest);
                break;
            case ACK:
                Ack ack = data.toJavaObject(Ack.class);
                out.add(ack);
                break;
            default:break;
        }
    }
}
