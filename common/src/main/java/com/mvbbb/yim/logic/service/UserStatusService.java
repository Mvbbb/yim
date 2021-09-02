package com.mvbbb.yim.logic.service;

import com.mvbbb.yim.common.WsServerRoute;

import java.util.List;

public interface UserStatusService {
    void userOnline(String userId,int port);

    void userOffline(String userId);

    List<String> onlineFriends(String userId);

    List<String> onlineGroupMembers(String groupId);

    boolean isUserOnline(String userId);
}
