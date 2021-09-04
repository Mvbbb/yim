package com.mvbbb.yim.ws;

import com.mvbbb.yim.ws.task.ConsumeMsgTask;
import com.mvbbb.yim.ws.task.ReportWsTask;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.Resource;

@SpringBootApplication(scanBasePackages = {"com.mvbbb.yim.ws","com.mvbbb.yim.common"})
@EnableDubbo
@PropertySource({"classpath:/dubbo-consumer.properties","classpath:/dubbo-provider.properties"})
public class WsServerApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(WsServerApplication.class,args);
    }

    @Resource
    ConsumeMsgTask consumeMsgTask;
    @Resource
    ReportWsTask reportWsTask;

    @Override
    public void run(String... args) throws Exception {
        new Thread(()->{
            new WebSocketServer(WsServerConfig.port).startWebSocketServer();
        }).start();
        new Thread(consumeMsgTask).start();
        new Thread(reportWsTask).start();
    }
}
