package com.mvbbb.yim.ws;


import com.mvbbb.yim.ws.handler.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class WSChannelInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(65536));

        pipeline.addLast(new WebSocketServerProtocolHandler("/"));

        //使用 json 方式
//        pipeline.addLast(new JsonDataPacketHandler());
        pipeline.addLast(new JsonProtocHandler());

        // 使用 protobuf 方式
//        pipeline.addLast(new ProtobufVarint32FrameDecoder());
//        pipeline.addLast(new ProtobufDecoder(Protobuf.DataPacket.getDefaultInstance()));
//        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
//        pipeline.addLast(new ProtobufEncoder());
//        pipeline.addLast(new ProtobufHandler());


        pipeline.addLast(new AckHandler());
        pipeline.addLast(new ByeHandler());
        pipeline.addLast(new GreetHandler());
        pipeline.addLast(new MsgDataHandler());

        pipeline.addLast(new IdleStateHandler(WsServerConfig.READ_IDEL_TIME_OUT, WsServerConfig.WRITE_IDEL_TIME_OUT, WsServerConfig.ALL_IDEL_TIME_OUT, TimeUnit.MINUTES));
        pipeline.addLast(new ChannelStatusHandler());
    }
}
