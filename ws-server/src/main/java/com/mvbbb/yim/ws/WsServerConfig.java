package com.mvbbb.yim.ws;

import com.mvbbb.yim.common.WsServerRoute;
import com.mvbbb.yim.common.util.MqUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Data
@Component
public final class WsServerConfig {
    public static final int READ_IDEL_TIME_OUT = 4;
    public static final int WRITE_IDEL_TIME_OUT = 5;
    public static final int ALL_IDEL_TIME_OUT = 7;
    public static final int MSG_SEND_FAILED_RETRY = 3;
    public static final int REGISTRY_FAILED_RETRY = 3;
    private int port;
    private int rpcPort;
    private String host;
    @Value("${zk.addr}")
    private String zkAddr;
    @Value("${zk.connect.timeout}")
    private int zkConnectTimeout;
    private String mqTopicName;
    private String mqConsumerGroupName;


    @PostConstruct
    public void init() {
        port = Integer.parseInt(System.getenv("server-port"));
        rpcPort = Integer.parseInt(System.getenv("dubbo.protocol.port"));
        try {
            String envAddress = System.getenv("host.addr");
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            host = envAddress == null ? hostAddress : envAddress;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.mqTopicName = MqUtil.getWsTopicName(host, port);
        this.mqConsumerGroupName = MqUtil.getWsConsumerGroupName(host, port);
    }

    public WsServerRoute getRoute() {
        return new WsServerRoute(host, port, rpcPort);
    }
}
