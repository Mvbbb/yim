package com.mvbbb.yim.gateway.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class GatewayConfig {

    @Value("${zk.addr}")
    private String zkAddr;

    @Value("${zk.connect.timeout}")
    private int zkConnectTimeout;
}
