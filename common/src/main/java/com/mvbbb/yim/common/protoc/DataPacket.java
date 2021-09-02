package com.mvbbb.yim.common.protoc;

import com.mvbbb.yim.common.protoc.ws.CmdIdEnum;
import lombok.Data;

@Data
public class DataPacket<T> {
    private int version;
    private CmdIdEnum cmdId;
    T data;
}
