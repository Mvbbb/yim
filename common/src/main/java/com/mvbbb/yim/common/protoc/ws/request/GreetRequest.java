package com.mvbbb.yim.common.protoc.ws.request;

import lombok.Data;

@Data
public class GreetRequest {
    private String userId;
    private String token;
}
