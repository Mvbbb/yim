package com.mvbbb.yim.auth;

import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.common.mapper.UserMapper;
import com.mvbbb.yim.common.util.GenRandomUtil;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class AuthServiceImpl implements AuthService{

    @Resource
    UserMapper userMapper;

    @Override
    public String genToken(String userId) {
        return "token";
    }

    @Override
    public boolean checkToken(String userId, String token) {
        return "token".equals(token);
    }

    @Override
    public String checkLogin(String userId, String password) {
        if("123".equals(password)){
            return "token";
        }else{
            return null;
        }
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        return false;
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
    public String echo() {
        return "auth server OK~~";
    }
}
