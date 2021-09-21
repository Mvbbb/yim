package com.mvbbb.yim.logic.service;

import cn.hutool.core.bean.BeanUtil;
import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.common.mapper.UserMapper;
import com.mvbbb.yim.common.vo.UserVO;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@DubboService
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;
    @Resource
    UserStatusService userStatusService;


    @Override
    public List<UserVO> listAllUser() {
        return userMapper.selectAllUser().stream().map((user -> {
            UserVO userVO = new UserVO();
            BeanUtil.copyProperties(user, userVO);
            boolean userOnline = userStatusService.isUserOnline(user.getUserId());
            userVO.setOnline(userOnline);
            return userVO;
        })).collect(Collectors.toList());
    }

    @Override
    public UserVO getUserInfo(String userId) {
        User user = userMapper.selectById(userId);
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        boolean userOnline = userStatusService.isUserOnline(user.getUserId());
        userVO.setOnline(userOnline);
        return userVO;
    }

    @Override
    public UserVO updateUserAvatar(String userId, String avatar) {
        userMapper.updateAvatar(userId, avatar);
        return getUserInfo(userId);
    }

    @Override
    public UserVO updateUserName(String userId, String username) {
        userMapper.updateUsername(userId, username);
        return getUserInfo(userId);
    }

    @Override
    public String echo() {
        return "Logic server is ok~";
    }
}
