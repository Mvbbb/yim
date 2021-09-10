package com.mvbbb.yim.common.protoc.http.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegisterRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
