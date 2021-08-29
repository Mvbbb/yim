package com.mvbbb.yim.common.protoc.request;

import lombok.Data;

import java.util.Date;

@Data
public class LoginRequest {
    private String userId;
    private String password;
    private String client_id;
    private Date timestamp;
}
