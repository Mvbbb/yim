package com.mvbbb.yim.ws.event;

import io.netty.channel.ChannelHandlerContext;

public interface IEvent<T> {
    void handler(T t, ChannelHandlerContext ctx);
}
