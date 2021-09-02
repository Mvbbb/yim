package com.mvbbb.yim.common.protoc.http.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String userId;
    private String password;
}
