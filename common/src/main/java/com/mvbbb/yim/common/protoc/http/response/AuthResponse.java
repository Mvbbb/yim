package com.mvbbb.yim.common.protoc.http.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String userId;
    private String token;
}
