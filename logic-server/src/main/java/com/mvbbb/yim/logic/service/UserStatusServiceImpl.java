package com.mvbbb.yim.logic.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mvbbb.yim.common.WsServerRoute;
import com.mvbbb.yim.common.constant.RedisConstant;
import com.mvbbb.yim.common.entity.UserGroupRelation;
import com.mvbbb.yim.common.mapper.FriendRelationMapper;
import com.mvbbb.yim.common.mapper.UserGroupRelationMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@DubboService
public class UserStatusServiceImpl implements UserStatusService{

    Logger logger  = LoggerFactory.getLogger(UserStatusServiceImpl.class);

    @Resource
    UserGroupRelationMapper userGroupRelationMapper;
    @Resource
    FriendRelationMapper friendRelationMapper;

    @Resource
    RedisTemplate<Object,Object> redisTemplate;

    @Override
    public void userOnline(String userId,int port) {
        String wsServerIp = RpcContext.getContext().getRemoteAddress().getAddress().getHostAddress();
        WsServerRoute wsServerRoute = new WsServerRoute(wsServerIp, port);
        String routeToWsSever = JSONObject.toJSONString(wsServerRoute);
        logger.info("add ws route to redis {}:{}",wsServerRoute.getIp(),wsServerRoute.getPort());
        redisTemplate.opsForValue().set(RedisConstant.STATUS_USER_ROUTE_PREFIX+userId,routeToWsSever);
    }

    @Override
    public void userOffline(String userId) {
        logger.error("user {} disconnect channel",userId);
        redisTemplate.delete(RedisConstant.STATUS_USER_ROUTE_PREFIX+userId);
    }

    @Override
    public List<String> onlineFriends(String userId) {
        List<String> friends = friendRelationMapper.selectFriendRelation(userId).stream().map((friendRelation -> {
            return friendRelation.getUserid1().equals(userId) ? friendRelation.getUserid2() : friendRelation.getUserid1();
        })).collect(Collectors.toList());
        return friends.stream().filter(this::isUserOnline).collect(Collectors.toList());
    }

    @Override
    public List<String> onlineGroupMembers(String groupId) {
        List<String> members = userGroupRelationMapper.selectList(new LambdaQueryWrapper<UserGroupRelation>().eq(UserGroupRelation::getGroupId, groupId))
                .stream().map(UserGroupRelation::getUserId).collect(Collectors.toList());
        return members.stream().filter(this::isUserOnline).collect(Collectors.toList());
    }

    @Override
    public boolean isUserOnline(String userId) {
        return redisTemplate.opsForValue().get(RedisConstant.STATUS_USER_ROUTE_PREFIX + userId) != null;
    }
}
