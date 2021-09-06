package com.mvbbb.yim.auth.service;

import com.mvbbb.yim.common.protoc.AuthEnum;
import com.mvbbb.yim.common.constant.RedisConstant;
import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.common.mapper.UserMapper;
import com.mvbbb.yim.common.util.GenRandomUtil;
import com.mvbbb.yim.logic.service.UserStatusService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.UUID;

@DubboService
public class AuthServiceImpl implements AuthService {

    private Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Resource
    UserMapper userMapper;
    @Resource
    RedisTemplate<Object,Object> redisTemplate;
    @DubboReference(check = false)
    UserStatusService userStatusService;



    // TODO set ttl for token and refresh
    @Override
    public AuthEnum checkToken(String userId, String token) {
        String rToken  = ((String) redisTemplate.opsForValue().get(RedisConstant.USER_TOKEN_PREFIX + userId));
        if (rToken!=null&&rToken.equals(token)) {
            logger.info("token 校验跳过. userId: [{}]",userId);
            return AuthEnum.SUCCESS;
        }
        logger.error("token 校验失败. userId: [{}], token: [{}]",userId,token);
        return AuthEnum.WRONG_TOKEN;
    }

    @Override
    public String checkLogin(String userId, String password) {
        User user = userMapper.selectById(userId);
        if(!user.getPassword().equals(password)){
            logger.error("密码错误. userId: [{}], password: [{}]",userId,password);
            return null;
        }
        // 判断用户是否已经登录
        boolean userOnline = userStatusService.isUserOnline(userId);
        if(userOnline){
            logger.error("用户被挤下线 userId: {}",userId);
            redisTemplate.delete(RedisConstant.USER_TOKEN_PREFIX+userId);
            userStatusService.kickout(userId);
        }

        String token = UUID.randomUUID().toString().substring(0,8);
        //重新登录之前的 token 会失效
        redisTemplate.opsForValue().set(RedisConstant.USER_TOKEN_PREFIX+userId,token);
        logger.info("签发 token 给用户. userId: [{}], token: [{}]",userId,token);
        return token;
    }

    @Override
    public AuthEnum changePassword(String userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if(!user.getPassword().equals(oldPassword)){
            logger.error("密码错误. userId: [{}], password: [{}]",userId,oldPassword);
            return AuthEnum.WRONG_PASSWORD;
        }
        int i = userMapper.updatePassword(userId, oldPassword, newPassword);
        return AuthEnum.SUCCESS;
    }

    @Override
    public User register(String username, String password) {
        User user = new User();
        user.setUserId(GenRandomUtil.genUserid());
        user.setPassword(password);
        user.setAvatar(GenRandomUtil.randomAvatar());
        user.setUsername(username);
        userMapper.insert(user);
        return user;
    }

    @Override
    public AuthEnum logout(String userId, String token) {
        AuthEnum validToken = checkToken(userId, token);
        if(validToken==AuthEnum.WRONG_TOKEN){
            return AuthEnum.WRONG_TOKEN;
        }
        userStatusService.userLogout(userId);
        redisTemplate.delete(RedisConstant.USER_TOKEN_PREFIX + userId);
        return AuthEnum.SUCCESS;
    }

    @Override
    public String echo() {
        return "auth server OK~~";
    }
}
