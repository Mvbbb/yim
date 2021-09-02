package com.mvbbb.yim.common.protoc.http.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
}
