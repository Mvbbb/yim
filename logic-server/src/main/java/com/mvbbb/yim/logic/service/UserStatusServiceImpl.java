package com.mvbbb.yim.logic.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mvbbb.yim.common.WsServerRoute;
import com.mvbbb.yim.common.constant.RedisConstant;
import com.mvbbb.yim.common.entity.UserGroupRelation;
import com.mvbbb.yim.common.mapper.FriendRelationMapper;
import com.mvbbb.yim.common.mapper.UserGroupRelationMapper;
import com.mvbbb.yim.ws.service.WsUserStatusService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.cluster.router.address.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
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
    @DubboReference(parameters = {"router","address"},check = false)
    WsUserStatusService wsUserStatusService;

    @Override
    public void userOnline(String userId,int port,int rpcPort) {
        String wsServerIp = RpcContext.getContext().getRemoteAddress().getAddress().getHostAddress();
        WsServerRoute wsServerRoute = new WsServerRoute(wsServerIp, port,rpcPort);
        logger.info("add ws route to redis {}:{}",wsServerRoute.getIp(),wsServerRoute.getPort());
        redisTemplate.opsForValue().set(RedisConstant.STATUS_USER_ROUTE_PREFIX+userId,wsServerRoute);
    }

    @Override
    public void userOffline(String userId) {
        logger.error("user {} route removed from redis",userId);
        redisTemplate.delete(RedisConstant.STATUS_USER_ROUTE_PREFIX+userId);
    }

    @Override
    public void userLogout(String userId) {
        wsUserStatusService.userOffline(userId);
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


    @Override
    public void offlineMsgPoolOver(String userId) {
        redisTemplate.delete(RedisConstant.OFFLINE_MSG_NOT_POOL_OVER_PREFIX+userId);
    }

    @Override
    public boolean isOfflineMsgPoolOver(String userId) {
        return redisTemplate.opsForValue().get(RedisConstant.OFFLINE_MSG_NOT_POOL_OVER_PREFIX+userId)==null;
    }

    @Override
    public void kickout(String userId) {
        WsServerRoute wsServerRoute = (WsServerRoute) redisTemplate.opsForValue().get(RedisConstant.STATUS_USER_ROUTE_PREFIX + userId);
        // 调用用户所在的wsserver
        Address address = new Address(wsServerRoute.getIp(), wsServerRoute.getRpcPort());
        RpcContext.getContext().setObjectAttachment("address",address);
        redisTemplate.delete(RedisConstant.STATUS_USER_ROUTE_PREFIX+userId);
        logger.info("调用 kickout");
        wsUserStatusService.kickout(userId);
    }
}
