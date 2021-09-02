package com.mvbbb.yim.common.protoc.http.response;

import lombok.Data;

@Data
public class RegisterResponse {
    private String username;
    private String userId;
    private String avatar;
}
