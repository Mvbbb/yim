package com.mvbbb.yim.common.protoc.ws.request;

import lombok.Data;

@Data
public class ByeRequest {
    private String userId;
    private String token;
}
