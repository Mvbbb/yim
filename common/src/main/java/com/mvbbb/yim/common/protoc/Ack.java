package com.mvbbb.yim.common.protoc;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Ack implements Serializable {
    private String clientMsgId;
    private long serverMsgId;
    private String msg;

    public Ack(String msg) {
        this(null,0,msg);
    }
}
