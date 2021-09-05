package com.mvbbb.yim.ws.task;

import com.mvbbb.yim.ws.WsServerConfig;
import com.mvbbb.yim.ws.service.ZkService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RegistryTask implements Runnable{

    @Resource
    private ZkService zkService;

    @Resource
    private WsServerConfig wsServerConfig;

    @Override
    public void run() {
        zkService.start();
        zkService.reportStatus();
    }
}
