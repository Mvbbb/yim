package com.mvbbb.yim.ws.handler;

import com.mvbbb.yim.common.protoc.DataPacket;
import com.mvbbb.yim.common.protoc.NewDataPacket;
import com.mvbbb.yim.common.protoc.ws.CmdIdEnum;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.concurrent.EventExecutorGroup;

public class DataPacketHandler extends SimpleChannelInboundHandler<NewDataPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,  NewDataPacket dataPacket) throws Exception {

    }
}
