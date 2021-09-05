package com.mvbbb.yim.common;

import lombok.Data;

@Data
public class WsStatus {
    private String wsAddr;
    private int connectionCnt;
}
