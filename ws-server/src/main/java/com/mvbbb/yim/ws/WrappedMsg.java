package com.mvbbb.yim.ws;

import com.mvbbb.yim.common.protoc.MsgData;
import lombok.Data;

@Data
public class WrappedMsg {
    private int sendTimes;
    private MsgData msgData;
}
