package com.mvbbb.yim.auth.service;

import com.mvbbb.yim.common.constant.RedisConstant;
import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.common.exception.IMException;
import com.mvbbb.yim.common.mapper.UserMapper;
import com.mvbbb.yim.common.protoc.AuthEnum;
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

    @Resource
    UserMapper userMapper;
    @Resource(name = "jsonRedisTemplate")
    RedisTemplate<Object, Object> redisTemplate;
    @DubboReference(check = false)
    UserStatusService userStatusService;
    private Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    // TODO set ttl for token and refresh
    @Override
    public AuthEnum checkToken(String userId, String token) {
        String rToken = ((String) redisTemplate.opsForValue().get(RedisConstant.USER_TOKEN_PREFIX + userId));
        if (rToken != null && rToken.equals(token)) {
            logger.info("token 通过. userId: [{}]", userId);
            return AuthEnum.SUCCESS;
        }
        logger.error("token 校验失败. userId: [{}], token: [{}]", userId, token);
        return AuthEnum.WRONG_TOKEN;
    }

    @Override
    public String checkLogin(String userId, String password) {
        User user = userMapper.selectById(userId);
        if (!user.getPassword().equals(password)) {
            logger.error("密码错误. userId: [{}], password: [{}]", userId, password);
            return null;
        }
        // 判断用户是否已经登录
        boolean userOnline = userStatusService.isUserOnline(userId);
        if (userOnline) {
            logger.error("用户被挤下线 userId: {}", userId);
            redisTemplate.delete(RedisConstant.USER_TOKEN_PREFIX + userId);
            userStatusService.kickout(userId);
        }

        String token = UUID.randomUUID().toString().substring(0, 8);
        //重新登录之前的 token 会失效
        redisTemplate.opsForValue().set(RedisConstant.USER_TOKEN_PREFIX + userId, token);
        logger.info("签发 token 给用户. userId: [{}], token: [{}]", userId, token);
        return token;
    }

    @Override
    public AuthEnum changePassword(String userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (!user.getPassword().equals(oldPassword)) {
            logger.error("密码错误. userId: [{}], password: [{}]", userId, oldPassword);
            return AuthEnum.WRONG_PASSWORD;
        }
        int i = userMapper.updatePassword(userId, oldPassword, newPassword);
        return AuthEnum.SUCCESS;
    }

    @Override
    public User register(String username, String password) {
        User user = new User();
        user.setPassword(password);
        user.setAvatar(GenRandomUtil.randomAvatar());

        if (userMapper.selectByUsername(username) != null) {
            throw new IMException("用户名被占用");
        }

        user.setUsername(username);
        // 随机id可能重复，导致无法
        String userId = null;
        int retry = 0;
        while (true) {
            userId = GenRandomUtil.genUserid();
            User existUser = userMapper.selectById(userId);
            if (existUser != null) {
                retry++;
                if (retry == 3) {
                    throw new IMException("无法分配id，系统故障，联系管理员");
                }
            } else {
                break;
            }
        }
        user.setUserId(userId);
        userMapper.insert(user);
        return user;
    }

    @Override
    public AuthEnum logout(String userId, String token) {
        AuthEnum validToken = checkToken(userId, token);
        if (validToken == AuthEnum.WRONG_TOKEN) {
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
