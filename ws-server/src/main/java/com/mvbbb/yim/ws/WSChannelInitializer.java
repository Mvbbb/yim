package com.mvbbb.yim.ws;


import com.mvbbb.yim.ws.handler.RecFrameHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WSChannelInitializer extends ChannelInitializer<SocketChannel> {

    ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // WebSocket 部分
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(65536));

        pipeline.addLast(new WebSocketServerProtocolHandler("/"));
        pipeline.addLast(new RecFrameHandler());
    }
}
