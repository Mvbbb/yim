package com.mvbbb.yim.msg;

import com.mvbbb.yim.common.protoc.MsgData;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootApplication(scanBasePackages = {"com.mvbbb.yim.msg","com.mvbbb.yim.common","com.mvbbb.yim.mq"})
@EnableDubbo
@PropertySource("classpath:/dubbo-provider.properties")
@MapperScan("com.mvbbb.yim.common.mapper")
public class MsgServerApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(MsgServerApplication.class,args);
    }

//    @Resource
//    RedeliverTask redeliverTask;

    @Override
    public void run(String... args) throws Exception {
//        new Thread(redeliverTask).start();
    }
}
