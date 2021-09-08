//package com.mvbbb.yim.ws.task;
//
//import com.alibaba.fastjson.JSONObject;
//import com.mvbbb.yim.common.WsServerRoute;
//import com.mvbbb.yim.mq.RedisStreamManager;
//import com.mvbbb.yim.common.protoc.MsgData;
//import com.mvbbb.yim.mq.MqManager;
//import com.mvbbb.yim.common.protoc.Protobuf;
//import com.mvbbb.yim.common.util.BeanConvertor;
//import com.mvbbb.yim.ws.ConnectionPool;
//import com.mvbbb.yim.ws.ProtobufDataPacketUtil;
//import com.mvbbb.yim.ws.WsServerConfig;
//import io.netty.channel.Channel;
//import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
//import org.apache.dubbo.common.serialize.protobuf.support.ProtobufUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
//@Deprecated
//@Component
//public class ConsumeMsgTask implements Runnable {
//
//    private Logger logger = LoggerFactory.getLogger(ConsumeMsgTask.class);
//
//    @Resource
//    RedisStreamManager redisStreamManager;
//    ConnectionPool connectionPool = ConnectionPool.getInstance();
//    @Resource
//    WsServerConfig wsServerConfig;
//
//    @Override
//    public void run() {
//        logger.info("start listen mq");
//        while(true){
//            WsServerRoute wsServerRoute = new WsServerRoute(wsServerConfig.getHost(),wsServerConfig.getPort(),wsServerConfig.getRpcPort());
//            MsgData msgData = redisStreamManager.consume(wsServerRoute);
//            if(msgData==null){
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }else{
//                String recvUserId = msgData.getRecvUserId();
//                Channel channel = connectionPool.findChannel(recvUserId);
//                if(channel==null){
//                    logger.error("指定用户的 channel 没找到，将消息投放到消息队列中. user: [{}], msg: [{}]",recvUserId,msgData);
//                    mqManager.reDeliver(msgData);
//                }else{
//                    //发送 json 文本
//                    String msg = JSONObject.toJSONString(msgData);
//                    TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(msg);
//                    channel.writeAndFlush(textWebSocketFrame);
//
////                    Protobuf.DataPacket dataPacket = ProtobufDataPacketUtil.buildMsgData(msgData);
////                    channel.writeAndFlush(dataPacket);
//                    logger.info("发送消息给用户 [{}]",recvUserId);
//                }
//            }
//        }
//    }
//}
