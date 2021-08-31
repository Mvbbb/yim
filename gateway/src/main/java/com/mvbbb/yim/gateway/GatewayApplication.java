package com.mvbbb.yim.gateway;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages ={ "com.mvbbb.yim.gateway","com.mvbbb.yim.common"})
@EnableDubbo
@PropertySource("classpath:/dubbo-consumer.properties")
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
