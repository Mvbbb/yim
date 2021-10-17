package com.mvbbb.yim.msg;

import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.service.UnreadService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author: mvbbb
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan("com.mvbbb")
public class TestUnreadService {
    @Resource(name = "jsonRedisTemplate")
    RedisTemplate<Object, Object> redisTemplate;
    @Resource
    UnreadService unreadService;

    @Test
    public void testUnreadService(){
        unreadService.updateUnreadCount("1", SessionType.SINGLE,"222",-100);
//        unreadService.clearSessionUnreadCount("1",SessionType.SINGLE,"222");
    }
}
