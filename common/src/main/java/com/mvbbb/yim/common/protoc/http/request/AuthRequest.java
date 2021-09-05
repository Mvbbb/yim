package com.mvbbb.yim.common.protoc.http.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String userId;
    private String token;
}
