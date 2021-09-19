package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.common.protoc.Ack;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.Protobuf;
import com.mvbbb.yim.common.protoc.ws.request.ByeRequest;
import com.mvbbb.yim.common.protoc.ws.request.GreetRequest;
import com.mvbbb.yim.common.util.BeanConvertor;
import com.mvbbb.yim.ws.ProtobufDataPacketUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@ChannelHandler.Sharable
@Component
public class ProtobufHandler extends MessageToMessageDecoder<Protobuf.DataPacket> {

    private static final Logger logger = LoggerFactory.getLogger(ProtobufHandler.class);

    private static ProtobufHandler protobufHandler;

    @PostConstruct
    public void init(){
        protobufHandler = this;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Protobuf.DataPacket msg, List<Object> out) throws Exception {
        logger.info("接收到 Protobuf 报文：{}",msg);
        Protobuf.CmdType cmd = msg.getCmd();
        switch (cmd){
            case MSG_DATA:
                Protobuf.MsgData protocBufMsgData = msg.getMsgData();
                MsgData msgData = BeanConvertor.protocToMsgData(protocBufMsgData);
                out.add(msgData);
                break;
            case BYE:
                Protobuf.Bye protobufBye = msg.getBye();
                ByeRequest byeRequest = BeanConvertor.protocToBye(protobufBye);
                out.add(byeRequest);
                break;
            case GREET:
                Protobuf.Greet protobufGreet = msg.getGreet();
                GreetRequest greetRequest = BeanConvertor.protocToGreetRequest(protobufGreet);
                out.add(greetRequest);
                break;
            case ACK:
                Protobuf.Ack protobufAck = msg.getAck();
                Ack ack = BeanConvertor.protocToAck(protobufAck);
                out.add(ack);
                break;
            default:break;
        }
    }
}
