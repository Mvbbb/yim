package com.mvbbb.yim.common.protoc;

import lombok.Builder;
import lombok.Data;

@Data
public class DataPacket<T> {
    private int version;
    private int cmdId;
    T data;
}
