package com.mvbbb.yim.common.protoc;

import com.mvbbb.yim.common.constant.ProtocConstant;
import com.mvbbb.yim.common.protoc.ws.CmdType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DataPacket<T> {
    private byte headFlag = ProtocConstant.HEAD;
    private int version;
    private CmdType cmdType;
    private int logId;
    private int sequenceId;
    private T data;
}