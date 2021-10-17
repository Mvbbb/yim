package com.mvbbb.yim.msg.service;

import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.SessionType;

public interface MsgService {

    void handlerMsgData(MsgData msgData);
}
