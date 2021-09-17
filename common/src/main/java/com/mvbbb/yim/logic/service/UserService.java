package com.mvbbb.yim.logic.service;

import com.mvbbb.yim.common.vo.UserVO;

import java.util.List;

public interface UserService {

    List<UserVO> listAllUser();

    UserVO getUserInfo(String userId);

    UserVO updateUserAvatar(String userId, String avatar);

    UserVO updateUserName(String userId, String username);
}
