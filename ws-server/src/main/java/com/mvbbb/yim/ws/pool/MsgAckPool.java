package com.mvbbb.yim.ws.pool;

import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.ws.WrappedMsg;

import java.util.concurrent.ConcurrentHashMap;

public class MsgAckPool {
    private static MsgAckPool msgAckPool;
    private ConcurrentHashMap<String, WrappedMsg> map = new ConcurrentHashMap<>();

    private MsgAckPool() {
    }

    public static MsgAckPool getInstance() {
        if (msgAckPool == null) {
            synchronized (MsgAckPool.class) {
                if (msgAckPool == null) {
                    msgAckPool = new MsgAckPool();
                }
            }
        }
        return msgAckPool;
    }

    public WrappedMsg getWrappedMsg(MsgData msgData) {
        String key = msgData.getServerMsgId() + ":" + msgData.getToUserId();
        return map.get(key);
    }

    public void acked(String key) {
        map.remove(key);
    }

    public void putMsg(MsgData msgData) {
        String key = msgData.getServerMsgId() + ":" + msgData.getToUserId();
        WrappedMsg wrappedMsg = new WrappedMsg();
        wrappedMsg.setMsgData(msgData);
        // todo times remove
        wrappedMsg.setSendTimes(0);
        map.put(key, wrappedMsg);
    }
}
