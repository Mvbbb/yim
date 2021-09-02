package com.mvbbb.yim.common.protoc;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ack {
    private String clientMsgId;
    private String serverMsgId;
    private String msg;

    public Ack(String msg) {
        this(null,null,msg);
    }
}
