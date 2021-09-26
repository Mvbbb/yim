package com.mvbbb.yim.ws;

import io.netty.channel.ChannelHandlerContext;

public final class EventExecutor {

    public static void execute(IEvent iEvent, Object t, ChannelHandlerContext ctx){
        new Thread(()->iEvent.handler(t,ctx)).start();
    }

}
