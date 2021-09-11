package com.mvbbb.yim.ws;

import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.vo.MsgVO;
import lombok.Data;

@Data
public class WrappedMsg {
    private int sendTimes;
    private MsgData msgData;
}
