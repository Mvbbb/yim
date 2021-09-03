package com.mvbbb.yim.logic.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mvbbb.yim.common.entity.MsgRecv;
import com.mvbbb.yim.common.entity.MsgSend;
import com.mvbbb.yim.common.mapper.MsgRecvMapper;
import com.mvbbb.yim.common.mapper.MsgSendMapper;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.http.OfflineSessionMsg;
import com.mvbbb.yim.common.protoc.ws.MsgType;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.protoc.http.response.PullOfflineMsgResponse;
import com.mvbbb.yim.common.util.BeanConvertor;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@DubboService
public class MsgServiceImpl implements MsgService{

    @Resource
    MsgRecvMapper msgRecvMapper;
    @Resource
    MsgSendMapper msgSendMapper;

    @Override
    public List<MsgData> getHistoryMsg(String userId, String sessionId, SessionType sessionType,int from,int limit) {
        List<MsgData> res = null;
        switch (sessionType){
            case SINGLE:
                List<MsgSend> singleMsgSends = msgSendMapper.selectHistorySingleMsg(userId,sessionId,from,limit);
                res = singleMsgSends.stream().map((msgSend -> {
                    return BeanConvertor.msgSendToMsgData(userId, msgSend, SessionType.SINGLE);
                })).collect(Collectors.toList());
                break;
            case GROUP:
                List<MsgSend> groupMsgSends = msgSendMapper.selectHistoryGroupMsg(sessionId,from,limit);
                res = groupMsgSends.stream().map((msgSend -> {
                    return BeanConvertor.msgSendToMsgData(userId, msgSend, SessionType.GROUP);
                })).collect(Collectors.toList());
                break;
            default:break;
        }
        return res;
    }

    @Override
    public PullOfflineMsgResponse getOfflineMsg(String userId) {
        List<MsgRecv> msgRecvs = msgRecvMapper.selectList(new LambdaQueryWrapper<MsgRecv>().eq(MsgRecv::getMsgTo, userId).eq(MsgRecv::isDelivered,false));
        msgRecvMapper.delivered(userId);

        PullOfflineMsgResponse pullOfflineMsgResponse = new PullOfflineMsgResponse();
        pullOfflineMsgResponse.setCnt(msgRecvs.size());
        // 群聊消息
        Set<String> groupIds = msgRecvs.stream().map(MsgRecv::getGroupId).collect(Collectors.toSet());
        groupIds.forEach((groupId)->{
            if(groupId!=null){
                OfflineSessionMsg offlineSessionMsg = new OfflineSessionMsg();
                offlineSessionMsg.setSessionId(groupId);

                List<MsgData> groupMsgData = msgRecvs.stream().filter((msgRecv) -> {
                    if(msgRecv.getGroupId()!=null&&msgRecv.getGroupId().equals(groupId)){
                        return true;
                    }else{
                        return false;
                    }
                }).collect(Collectors.toList()).stream().map((BeanConvertor::msgRecvToMsgData)).collect(Collectors.toList());
                offlineSessionMsg.setSessionType(SessionType.GROUP);
                offlineSessionMsg.setMsgs(groupMsgData);
                offlineSessionMsg.setCnt(groupMsgData.size());
                offlineSessionMsg.setTimestamp(groupMsgData.get(groupMsgData.size()-1).getTimestamp());
                pullOfflineMsgResponse.getSessionsMsgs().add(offlineSessionMsg);
            }
        });

        // 私聊消息
        Set<String> friendIds = msgRecvs.stream().filter((msgRecv)->{
            return msgRecv.getGroupId()==null;
        }).map(MsgRecv::getMsgFrom).collect(Collectors.toSet());
        friendIds.forEach((fromId)->{
            if(fromId!=null){
                OfflineSessionMsg friendSessionMsgs = new OfflineSessionMsg();
                friendSessionMsgs.setSessionId(fromId);
                friendSessionMsgs.setSessionType(SessionType.SINGLE);
                List<MsgData> friendMsgs = msgRecvs.stream().filter((msgRecv -> {
                    return msgRecv.getGroupId()==null&&msgRecv.getMsgFrom().equals(fromId);
                })).collect(Collectors.toList()).stream().map(BeanConvertor::msgRecvToMsgData).collect(Collectors.toList());
                friendSessionMsgs.setMsgs(friendMsgs);
                friendSessionMsgs.setCnt(friendMsgs.size());
                friendSessionMsgs.setTimestamp(friendMsgs.get(friendMsgs.size()-1).getTimestamp());
                pullOfflineMsgResponse.getSessionsMsgs().add(friendSessionMsgs);
            }
        });
        return pullOfflineMsgResponse;
    }
}
