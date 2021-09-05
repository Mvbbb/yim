package com.mvbbb.yim.common;

import lombok.Data;

/**
 * ws server 的状态，用于负载均衡
 */
@Data
public class WsStatus {
    private String wsAddr;
    private int connectionCnt;
}
