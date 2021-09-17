package com.mvbbb.yim.gateway.service;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.WsStatus;
import com.mvbbb.yim.common.constant.ZkConstant;
import com.mvbbb.yim.gateway.config.GatewayConfig;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class RegisterService {

    private final Logger logger = LoggerFactory.getLogger(RegisterService.class);
    private final Random random = new Random();
    @Resource
    GatewayConfig gatewayConfig;
    private CuratorFramework client;
    private TreeCache treeCache;

    public void createClient() {

        if (client == null) {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            client = CuratorFrameworkFactory.newClient(gatewayConfig.getZkAddr(), retryPolicy);
            client.start();
        }
        List<String> wses = null;
        try {
            wses = client.getChildren().forPath(ZkConstant.ZK_ROOT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 为用户选择一个连接数最小的 wsserver
     *
     * @return
     */
    public String getWsForUser() {
        Map<String, ChildData> datas = treeCache.getCurrentChildren(ZkConstant.ZK_ROOT);
        List<WsStatus> wses = datas.values().stream().map((childData -> {
            String status = new String(childData.getData());
            return JSONObject.parseObject(status, WsStatus.class);
        })).collect(Collectors.toList());
        if (wses.size() == 0) {
            logger.error("no ws server available");
            return null;
        }
        wses.sort((w1, w2) -> {
            return w1.getConnectionCnt() - w2.getConnectionCnt();
        });
        return wses.get(0).getWsAddr();
    }

    public void cache() {
        if (treeCache != null) {
            return;
        }
        treeCache = TreeCache.newBuilder(client, ZkConstant.ZK_ROOT).setCacheData(true).build();
//        treeCache.getListenable().addListener((c,event)->{
//            if(event.getData()!=null){
//                System.out.println("TreeCache,type=" + event.getType() + " path=" + event.getData().getPath());
//            }else{
//                System.out.println("TreeCache,type=" + event.getType());
//            }
//        });
        try {
            treeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
