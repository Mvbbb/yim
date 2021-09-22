package com.mvbbb.yim.ws.service;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.protoc.Ack;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.util.BeanConvertor;
import com.mvbbb.yim.common.vo.MsgVO;
import com.mvbbb.yim.logic.service.UserStatusService;
import com.mvbbb.yim.mq.RedisStreamManager;
import com.mvbbb.yim.ws.ConnectionPool;
import com.mvbbb.yim.ws.MsgAckPool;
import com.mvbbb.yim.ws.WrappedMsg;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 向用户发送信息
 */
@Service
public class MsgSendService {

    private static final Logger logger = LoggerFactory.getLogger(MsgSendService.class);

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    @Resource
    RedisStreamManager redisStreamManager;
    @DubboReference(check = false)
    UserStatusService userStatusService;
    private MsgAckPool msgAckPool = MsgAckPool.getInstance();
    // FIXME
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

    public void sendAckToUser(String userId, String msg) {
        logger.info("发送 ack 消息给用户。 UserId:{}，msg:{}",userId,msg);

        Channel channel = connectionPool.findChannel(userId);
        if (channel == null) {
            logger.error("连接未建立");
            return;
        }
        Ack ack = new Ack();
        ack.setUserId(userId);
        ack.setMsg(msg);
//      Protobuf.DataPacket dataPacket = ProtobufDataPacketUtil.buildAck(ack);
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(JSONObject.toJSONString(ack));
        try {
//            channel.writeAndFlush(dataPacket).sync();
            channel.writeAndFlush(textWebSocketFrame).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * FIXME 添加消息下行通知协议
     */
    public void send(Channel channel, String msg) {
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(msg);
//        Ack ack = new Ack(msg);
//        Protobuf.DataPacket dataPacket = ProtobufDataPacketUtil.buildAck(ack);
        try {
//          channel.writeAndFlush(dataPacket).sync();
            channel.writeAndFlush(textWebSocketFrame).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定时任务 https://www.cnkirito.moe/timer/#4-1-Timer
     */
    public void sendMsg(Channel channel, MsgData msgData) {
        MsgVO msgVO = BeanConvertor.msgDataToMsgVO(msgData);
        String msg = JSONObject.toJSONString(msgVO);
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(msg);
        channel.writeAndFlush(textWebSocketFrame);
        logger.info("发送消息给用户 [{}]", msgData.getRecvUserId());

        // TODO 使用时间轮优化
        Thread waitAckedTask = new Thread(() -> {
            msgAckPool.putMsg(msgData);
            boolean success = false;
            for (int i = 0; i < 3; i++) {
                try {
                    Thread.sleep(Duration.ofSeconds(10).toMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                WrappedMsg wrappedMsg = msgAckPool.getWrappedMsg(msgData);
                if (wrappedMsg == null) {
                    success = true;
                    break;
                }
            }
            if (!success) {
                logger.error("没有收到用户的 ack 消息，将用户离线");
                userStatusService.userOffline(msgData.getRecvUserId());
                redisStreamManager.failedDeliveredMsg(msgData);
                channel.close();
            }
        });
        threadPoolExecutor.execute(waitAckedTask);
    }
}
