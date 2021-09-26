package com.mvbbb.yim.ws;

import com.mvbbb.yim.ws.handler.AckHandler;
import com.mvbbb.yim.ws.handler.ByeHandler;
import com.mvbbb.yim.ws.handler.GreetHandler;
import com.mvbbb.yim.ws.handler.MsgDataHandler;
import com.mvbbb.yim.ws.pool.EventPool;
import com.mvbbb.yim.ws.task.RegistryTask;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

@SpringBootApplication(scanBasePackages = {"com.mvbbb.yim.ws", "com.mvbbb.yim.common", "com.mvbbb.yim.mq"})
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
    AckHandler ackHandler;
    @Resource
    ByeHandler byeHandler;
    @Resource
    GreetHandler greetHandler;
    @Resource
    MsgDataHandler msgDataHandler;

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
    }

    void registerEvents(){
        eventPool.register(EventEnum.ACK,ackHandler);
        eventPool.register(EventEnum.BYE, byeHandler);
        eventPool.register(EventEnum.GREET,greetHandler);
        eventPool.register(EventEnum.MSG_DATA,msgDataHandler);
    }
}