package com.mvbbb.yim.gateway;

import com.mvbbb.yim.gateway.service.RegisterService;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.Resource;

@SpringBootApplication(scanBasePackages = {"com.mvbbb.yim.gateway", "com.mvbbb.yim.common"})
@EnableDubbo
@PropertySource("classpath:/dubbo-consumer.properties")
public class GatewayApplication implements CommandLineRunner {
    @Resource
    RegisterService zkService;

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        zkService.createClient();
        zkService.cache();
    }
}
