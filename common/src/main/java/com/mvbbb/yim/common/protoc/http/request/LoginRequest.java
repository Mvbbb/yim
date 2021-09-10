package com.mvbbb.yim.common.protoc.http.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {
    @NotNull
    private String userId;
    @NotNull
    private String password;
}
