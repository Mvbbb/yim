package com.mvbbb.yim.ws.task;

import com.mvbbb.yim.common.WsServerRoute;
import com.mvbbb.yim.common.constant.RedisConstant;
import com.mvbbb.yim.ws.WsServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ReportWsTask implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ReportWsTask.class);
    @Resource
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public void run() {
        logger.info("上报ws服务");
        WsServerRoute wsServerRoute = new WsServerRoute(WsServerConfig.host, WsServerConfig.port, WsServerConfig.rpcPort);
        redisTemplate.opsForSet().add(RedisConstant.WSES_PREFIX,wsServerRoute);
    }
}
