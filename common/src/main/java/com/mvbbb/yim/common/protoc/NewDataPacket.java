package com.mvbbb.yim.common.protoc;

import com.mvbbb.yim.common.config.ProtocConstant;
import lombok.Data;

@Data
public class NewDataPacket {
    private byte headFlag = ProtocConstant.HEAD;
    private int version;
    private byte cmd;
    private byte msgType;
    private int logId;
    private int sequenceId;
    private int dataLength;
    private byte[] data;
}
