package com.mvbbb.yim.ws;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class WsServerConfig {
    public static final int port = Integer.parseInt(System.getProperty("server-port"));
    public static final int rpcPort = Integer.parseInt(System.getProperty("dubbo.protocol.port"));
    public static String host = null;

    static {
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}
