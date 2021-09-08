package com.mvbbb.yim.logic.service;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.WsServerRoute;
import com.mvbbb.yim.common.constant.RedisConstant;
import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.common.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.List;

@DubboService
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;
    @Resource(name = "jsonRedisTemplate")
    RedisTemplate<Object,Object> redisTemplate;


    @Override
    public List<User> listAllUser() {
        return userMapper.selectAllUser();
    }

    @Override
    public User getUserInfo(String userId) {
        User user = userMapper.selectById(userId);
        if(user==null){
            throw new RuntimeException();
        }
        return user;
    }

    @Override
    public User updateUserAvatar(String userId, String avatar) {
        userMapper.updateAvatar(userId,avatar);
        return userMapper.selectById(userId);
    }

    @Override
    public User updateUserName(String userId, String username) {
        userMapper.updateUsername(userId,username);
        return userMapper.selectById(userId);
    }

}
