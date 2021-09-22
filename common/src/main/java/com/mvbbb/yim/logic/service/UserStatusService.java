package com.mvbbb.yim.logic.service;

import java.util.List;

public interface UserStatusService {
    void userOnline(String userId, String host, int port, int rpcPort);

    void userOffline(String userId);

    void userLogout(String userId);

    List<String> onlineFriends(String userId);

    List<String> onlineGroupMembers(String groupId);

    boolean isUserOnline(String userId);

    void offlineMsgPoolOver(String userId);

    boolean isOfflineMsgPoolOver(String userId);

    void kickout(String userId);
}
