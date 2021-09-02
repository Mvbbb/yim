package com.mvbbb.yim.ws;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class WsServerConfig {
    public static final int port = Integer.parseInt(System.getenv("server-port"));
    public static String host = null;

    static {
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
