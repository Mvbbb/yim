package com.mvbbb.yim.common.protoc.http.response;

import lombok.Data;

@Data
public class AuthWsInfoResponse {
    private String userId;
    private String token;
    private String wsUrl;
}
