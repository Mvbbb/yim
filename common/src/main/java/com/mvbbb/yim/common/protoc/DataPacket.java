package com.mvbbb.yim.common.protoc;

import com.mvbbb.yim.common.protoc.ws.CmdType;
import lombok.Data;

/**
 * FIXME 完善
 * @param <T>
 */
@Deprecated
@Data
public class DataPacket<T> {
    private int version;
    private CmdType cmdId;
    T data;
}