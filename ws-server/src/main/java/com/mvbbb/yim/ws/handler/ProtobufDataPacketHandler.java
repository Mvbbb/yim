package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.Protobuf;
import com.mvbbb.yim.common.protoc.ws.request.ByeRequest;
import com.mvbbb.yim.common.protoc.ws.request.GreetRequest;
import com.mvbbb.yim.common.util.BeanConvertor;
import com.mvbbb.yim.ws.service.MsgTransfer;
import com.mvbbb.yim.ws.service.StatusService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@ChannelHandler.Sharable
public class ProtobufDataPacketHandler extends SimpleChannelInboundHandler<Protobuf.DataPacket> {

    private static ProtobufDataPacketHandler dataPacketHandler;
    Logger logger = LoggerFactory.getLogger(ProtobufDataPacketHandler.class);
    @Resource
    StatusService statusHandler;
    @Resource
    MsgTransfer msgHandler;

    @PostConstruct
    public void init() {
        dataPacketHandler = this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protobuf.DataPacket dataPacket) throws Exception {
        Protobuf.CmdType cmd = dataPacket.getCmd();

        switch (cmd) {
            case ACK:
                // todo
                logger.info("receive ack");
                break;
            case BYE:
                logger.info("receive bye");
                ByeRequest byeRequest = BeanConvertor.protocToBye(dataPacket.getBye());
                dataPacketHandler.statusHandler.bye(ctx.channel(), byeRequest);
                break;
            case GREET:
                logger.info("receive greet");
                GreetRequest greetRequest = BeanConvertor.protocToGreetRequest(dataPacket.getGreet());
                dataPacketHandler.statusHandler.greet(ctx.channel(), greetRequest);
                break;
            case MSG_DATA:
                logger.info("receive msg data");
                MsgData msgData = BeanConvertor.protocToMsgData(dataPacket.getMsgData());
                dataPacketHandler.msgHandler.sendMsg(msgData);
                break;
            default:
                break;
        }
    }
}
