package com.mvbbb.yim.common.protoc;

import com.mvbbb.yim.common.protoc.ws.CmdType;
import lombok.Data;

@Data
public class DataPacket<T> {
    private int version;
    private CmdType cmdId;
    T data;
}