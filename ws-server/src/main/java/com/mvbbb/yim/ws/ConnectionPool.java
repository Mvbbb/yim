package com.mvbbb.yim.ws;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionPool {

    private static ConnectionPool connectionPool = null;
    private ConcurrentHashMap<String, Channel> channels = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Channel, String> userids = new ConcurrentHashMap<>();

    public ConnectionPool() {
    }

    public static synchronized ConnectionPool getInstance() {
        if (connectionPool == null) {
            connectionPool = new ConnectionPool();
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

    public void checkToClose(Channel channel) {
        if (connectionPool.getUseridByChannel(channel) == null) {
            try {
                channel.close().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
