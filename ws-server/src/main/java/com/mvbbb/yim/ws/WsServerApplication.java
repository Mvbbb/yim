package com.mvbbb.yim.ws;

import com.mvbbb.yim.ws.event.*;
import com.mvbbb.yim.ws.mq.DeliverMsgMqListener;
import com.mvbbb.yim.ws.pool.EventPool;
import com.mvbbb.yim.ws.task.RegistryTask;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

@SpringBootApplication(scanBasePackages = {"com.mvbbb.yim.ws", "com.mvbbb.yim.common"})
@EnableDubbo
@EnableScheduling
@PropertySource({"classpath:/dubbo-consumer.properties", "classpath:/dubbo-provider.properties"})
public class WsServerApplication implements CommandLineRunner {

    private static final EventPool eventPool = EventPool.getInstance();
    @Resource
    WsServerConfig wsServerConfig;
    @Resource
    RegistryTask registryTask;
    @Resource
    AckEventHandler ackEventHandler;
    @Resource
    ByeEventHandler byeEventHandler;
    @Resource
    GreetEventHandler greetEventHandler;
    @Resource
    MsgDataEventHandler msgDataEventHandler;
    @Resource
    ActiveEventHandler activeEventHandler;
    @Resource
    OfflineEventHandler offlineEventHandler;
    @Resource
    DeliverMsgMqListener deliverMsgMqListener;

    public static void main(String[] args) {
        SpringApplication.run(WsServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        registerEvents();
        new Thread(() -> {
            new WebSocketServer(wsServerConfig.getPort()).startWebSocketServer();
        }).start();
        new Thread(registryTask).start();
        new Thread(() -> {
            deliverMsgMqListener.listen();
        }).start();
    }

    void registerEvents() {
        eventPool.register(EventEnum.ACK, ackEventHandler);
        eventPool.register(EventEnum.BYE, byeEventHandler);
        eventPool.register(EventEnum.GREET, greetEventHandler);
        eventPool.register(EventEnum.MSG_DATA, msgDataEventHandler);
        eventPool.register(EventEnum.ACTIVE, activeEventHandler);
        eventPool.register(EventEnum.OFFLINE, offlineEventHandler);
    }
}