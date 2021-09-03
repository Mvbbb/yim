package com;

import com.mvbbb.yim.common.protoc.MsgData;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
@ComponentScan(basePackages = {"com.mvbbb.yim"})
public class SBTest {

    @Resource
    RedisTemplate<Object,Object> redisTemplate;

    @Test
    public void test(){
        redisTemplate.opsForValue().set("rtet",new MsgData());
    }
}
