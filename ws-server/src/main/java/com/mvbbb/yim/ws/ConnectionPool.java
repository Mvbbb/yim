package com.mvbbb.yim.ws;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
    private static ConnectionPool connectionPool = null;
    private ConcurrentHashMap<String, Channel> channels = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Channel, String> userids = new ConcurrentHashMap<>();

    public ConnectionPool() {
    }

    public static ConnectionPool getInstance() {
        if (connectionPool == null) {
            synchronized (ConnectionPool.class) {
                if (connectionPool == null) {
                    connectionPool = new ConnectionPool();
                }
            }
        }
        return connectionPool;
    }

    public Channel findChannel(String userid) {
        return channels.get(userid);
    }

    public String getUseridByChannel(Channel channel) {
        return userids.get(channel);
    }

    public void addUserChannel(Channel channel, String userid) {
        channels.put(userid, channel);
        userids.put(channel, userid);
    }

    public void removeConnection(Channel channel, String userid) {
        channels.remove(userid);
        userids.remove(channel);
    }

    public int getConnectionCnt() {
        return userids.size();
    }

    public void closeConnection(Channel channel) {
        try {
            channel.close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查是否有不在 pool 里面的连接做出非法的请求，将其关闭
     *
     * @param channel
     */
    public boolean checkToClose(Channel channel) {
        if (connectionPool.getUseridByChannel(channel) == null) {
            try {
                logger.error("非法请求，将关闭连接。{}", channel);
                channel.close().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

}
