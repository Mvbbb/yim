package com.mvbbb.yim.ws;

import io.netty.channel.ChannelHandlerContext;

public interface IEvent <T>{
    void handler(T t, ChannelHandlerContext ctx);
}
