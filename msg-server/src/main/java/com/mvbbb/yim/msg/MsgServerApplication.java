package com.mvbbb.yim.msg;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"com.mvbbb.yim.msg","com.mvbbb.yim.common.config"})
@EnableDubbo
@PropertySource("classpath:/dubbo-provider.properties")
@MapperScan("com.mvbbb.yim.common.mapper")
public class MsgServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsgServerApplication.class,args);
    }
}
