package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.common.protoc.Ack;
import com.mvbbb.yim.common.protoc.Bye;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.Protobuf;
import com.mvbbb.yim.common.protoc.ws.request.GreetRequest;
import com.mvbbb.yim.common.util.BeanConvertor;
import com.mvbbb.yim.ws.event.EventEnum;
import com.mvbbb.yim.ws.event.EventExecutor;
import com.mvbbb.yim.ws.pool.EventPool;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@ChannelHandler.Sharable
public class ProtobufHandler extends SimpleChannelInboundHandler<Protobuf.DataPacket> {

    private static final Logger logger = LoggerFactory.getLogger(ProtobufHandler.class);
    private static final EventPool eventPool = EventPool.getInstance();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protobuf.DataPacket msg) throws Exception {
        logger.info("接收到 Protobuf 报文：{}", msg);
        Protobuf.CmdType cmd = msg.getCmd();
        switch (cmd) {
            case MSG_DATA:
                Protobuf.MsgData protocBufMsgData = msg.getMsgData();
                MsgData msgData = BeanConvertor.protocToMsgData(protocBufMsgData);
                EventExecutor.execute(eventPool.find(EventEnum.MSG_DATA), msgData, ctx);
                break;
            case BYE:
                Protobuf.Bye protobufBye = msg.getBye();
                Bye bye = BeanConvertor.protocToBye(protobufBye);
                EventExecutor.execute(eventPool.find(EventEnum.BYE),bye,ctx);
                break;
            case GREET:
                Protobuf.Greet protobufGreet = msg.getGreet();
                GreetRequest greetRequest = BeanConvertor.protocToGreetRequest(protobufGreet);
                EventExecutor.execute(eventPool.find(EventEnum.GREET),greetRequest,ctx);
                break;
            case ACK:
                Protobuf.Ack protobufAck = msg.getAck();
                Ack ack = BeanConvertor.protocToAck(protobufAck);
                EventExecutor.execute(eventPool.find(EventEnum.ACK),ack,ctx);
                break;
            default:
                break;
        }
    }
}
