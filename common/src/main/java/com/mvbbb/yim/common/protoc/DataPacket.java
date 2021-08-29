package com.mvbbb.yim.common.protoc;

import lombok.Data;

@Data
public class DataPacket<T> {
    private int version;
    private int cmd_id;
    T data;
}
