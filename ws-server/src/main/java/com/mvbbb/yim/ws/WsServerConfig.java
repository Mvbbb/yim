package com.mvbbb.yim.ws;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Data
@Component
public final class WsServerConfig {
    private int port;
    private int rpcPort;
    private String host;
    @Value("${zk.addr}")
    private String zkAddr;
    @Value("${zk.connect.timeout}")
    private int zkConnectTimeout;

    @PostConstruct
    public void init(){
        port = Integer.parseInt(System.getProperty("server-port"));
        rpcPort = Integer.parseInt(System.getProperty("dubbo.protocol.port"));
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}
