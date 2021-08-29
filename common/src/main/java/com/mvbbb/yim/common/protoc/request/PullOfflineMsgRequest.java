package com.mvbbb.yim.common.protoc.request;

import lombok.Data;

@Data
public class PullOfflineMsgRequest {
    private String userId;
    private String token;
    private String clientMsgId;
}
