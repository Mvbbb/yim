package com.mvbbb.yim.ws.service;

public interface WsUserStatusService {
    void kickout(String userId);

    void userOffline(String userId);
}
