package com.mvbbb.yim.ws.service;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.constant.ZkConstant;
import com.mvbbb.yim.ws.ConnectionPool;
import com.mvbbb.yim.ws.WsServerConfig;
import com.mvbbb.yim.common.WsStatus;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ZkService {

    private Logger logger = LoggerFactory.getLogger(ZkService.class);

    ConnectionPool connectionPool = ConnectionPool.getInstance();
    @Resource
    WsServerConfig wsServerConfig;
    private CuratorFramework client;

    public void start() {

        if(client==null){
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
            client = CuratorFrameworkFactory.newClient(wsServerConfig.getZkAddr(), retryPolicy);
            client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
                @Override
                public void stateChanged(CuratorFramework client, ConnectionState newState) {
                    switch (newState){
                        case CONNECTED:
                            logger.info("成功与 zookeeper 建立连接。 {}",wsServerConfig.getZkAddr());
                            createNode();
                            reportStatus();
                            break;
                        case SUSPENDED:
                            logger.error("与 zookeeper 连接断开。 {}",wsServerConfig.getZkAddr());
                            break;
                        case RECONNECTED:
                            logger.error("重新与 zookeeper 建立连接。{}",wsServerConfig.getZkAddr());
                            createNode();
                            break;
                        case LOST:
                            logger.error("与 zookeeper 会话过期");
                            break;
                        default: break;
                    }
                }
            });
            client.start();
        }
    }

    private void createNode(){
        String path = ZkConstant.ZK_ROOT+"/"+wsServerConfig.getHost()+":"+wsServerConfig.getPort();
        createRootNode();
        try {
            int retry = 0;
            while(retry<3&&client.checkExists().forPath(path)==null){
                logger.info("第 {} 次尝试注册节点",retry);
                client.create().withMode(CreateMode.EPHEMERAL).forPath(path);
                if(client.checkExists().forPath(path)!=null){
                    logger.info("注册 zookeeper 节点成功， path = [{}]",path);
                    break;
                }
                retry++;
            }
            if(retry==3){
                logger.error("节点注册失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createRootNode() {
        try {
            int retry = 0;
            while(retry<3&&client.checkExists().forPath(ZkConstant.ZK_ROOT)==null){
                client.create().withMode(CreateMode.PERSISTENT).forPath(ZkConstant.ZK_ROOT);
                retry++;
            }
            if(client.checkExists().forPath(ZkConstant.ZK_ROOT)!=null){
                logger.info("创建 根节点： {}",ZkConstant.ZK_ROOT);
            }else{
                logger.error("根节点创建失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void reportStatus(){
        new Thread(()->{
            while (true) {
                try {
                    String path = ZkConstant.ZK_ROOT + "/" + wsServerConfig.getHost() + ":" + wsServerConfig.getPort();
                    int connectionCnt = connectionPool.getConnectionCnt();
                    WsStatus wsStatus = new WsStatus();
                    wsStatus.setWsAddr("ws://" + wsServerConfig.getHost() + ":" + wsServerConfig.getPort());
                    wsStatus.setConnectionCnt(connectionCnt);
                    String status = JSONObject.toJSONString(wsStatus);
                    try {
                        if (client.checkExists().forPath(path) != null) {
                            logger.info("上报 ws 连接状态 {}",status);
                            client.setData().forPath(path, status.getBytes());
                        }else{
                            logger.error("zookeeper 中本 server 临时节点丢失，重新创建节点中");
                            createNode();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            }).start();
    }
}
