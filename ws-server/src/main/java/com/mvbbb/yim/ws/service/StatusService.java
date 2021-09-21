package com.mvbbb.yim.ws.service;

import com.mvbbb.yim.auth.service.AuthService;
import com.mvbbb.yim.common.protoc.AuthEnum;
import com.mvbbb.yim.common.protoc.ws.request.ByeRequest;
import com.mvbbb.yim.common.protoc.ws.request.GreetRequest;
import com.mvbbb.yim.logic.service.UserStatusService;
import com.mvbbb.yim.ws.ConnectionPool;
import com.mvbbb.yim.ws.WsServerConfig;
import io.netty.channel.Channel;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 管理 Channel 状态
 * TODO 添加心跳保活机制
 */
@Service
public class StatusService {

    Logger logger = LoggerFactory.getLogger(StatusService.class);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    @Resource
    MsgSendService sendDataToUserHandler;
    @Resource
    WsServerConfig wsServerConfig;
    @DubboReference(check = false)
    private AuthService authService;
    @DubboReference(check = false)
    private UserStatusService userStatusService;

    public void greet(Channel channel, GreetRequest greetRequest) {
        String userId = greetRequest.getUserId();
        String token = greetRequest.getToken();
        boolean tokenValid = this.checkToken(userId, token);
        if (tokenValid) {
            if (!userStatusService.isOfflineMsgPoolOver(userId)) {
                logger.error("离线消息还没拉取完毕，无法建立长连接 userId: [{}]", userId);
                sendDataToUserHandler.send(channel, "离线消息还没拉取完毕，建立ws连接失败");
                connectionPool.removeConnection(channel, userId);
                channel.close();
            } else {
                logger.info("add user {} channel {}", userId, channel);
                connectionPool.addUserChannel(channel, userId);
                sendDataToUserHandler.sendAckToUser(userId, "Connect success");
                if (userStatusService.isUserOnline(userId)) {
                    logger.error("user切换 ws 服务器到本节点。 userid: {}", userId);
                }
                userStatusService.userOnline(userId, wsServerConfig.getPort(), wsServerConfig.getRpcPort());
            }
        } else {

            logger.error("用户 token 错误，关闭连接中:{}", token);
            sendDataToUserHandler.sendAckToUser(userId, "Wrong token");
            channel.close();
        }
    }

    private boolean checkToken(String userId, String token) {
        return AuthEnum.SUCCESS == authService.checkToken(userId, token);
    }

    public void bye(Channel channel, ByeRequest byeRequestDataPacketData) {
        String userId = connectionPool.getUseridByChannel(channel);
        this.bye(channel, userId);
    }

    public void bye(Channel channel, String userId) {
        userStatusService.userOffline(userId);
        sendDataToUserHandler.send(channel, "断开连接");
        logger.info("user {} close channel {}", userId, channel);
        connectionPool.removeConnection(channel, userId);
        connectionPool.closeConnection(channel);
        channel.close();
    }
}
