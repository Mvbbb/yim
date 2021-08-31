package com.mvbbb.yim.ws;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionPool {

    private static ConnectionPool connectionPool = null;

    public ConnectionPool() {
    }

    public static synchronized ConnectionPool getInstance(){
        if(connectionPool == null){
            connectionPool = new ConnectionPool();
        }
        return connectionPool;
    }

    private ConcurrentHashMap<String, Channel> channels = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Channel,String> userids = new ConcurrentHashMap<>();



    public Channel findChannel(String userid){
        return channels.get(userid);
    }

    public String getUseridByChannel(Channel channel){
        return userids.get(channel);
    }

    public void addUserChannel(Channel channel, String userid){
        channels.put(userid,channel);
        userids.put(channel,userid);
    }

    public void removeConnection(Channel channel,String userid){
        channels.remove(userid);
        userids.remove(channel);
    }


}
