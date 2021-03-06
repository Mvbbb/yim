package com.mvbbb.yim.auth;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"com.mvbbb.yim.auth", "com.mvbbb.yim.common"})
@EnableDubbo(scanBasePackages = "com.mvbbb.yim.auth")
@PropertySource("classpath:/dubbo-provider.properties")
@MapperScan("com.mvbbb.yim.common.mapper")
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}