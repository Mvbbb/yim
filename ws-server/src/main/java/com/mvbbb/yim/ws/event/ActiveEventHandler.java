package com.mvbbb.yim.ws.event;

import com.mvbbb.yim.ws.pool.ConnectionPool;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class ActiveEventHandler implements IEvent<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ActiveEventHandler.class);
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public void handler(Object o, ChannelHandlerContext ctx) {
        Timer timer = new Timer("time to close channel", false);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String user = connectionPool.getUseridByChannel(ctx.channel());
                if (user == null) {
                    try {
                        logger.error("长时间没有发送认证消息，自动关闭 channel。channel : [{}]", ctx.channel());
                        ctx.channel().close().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 10000);
    }
}
