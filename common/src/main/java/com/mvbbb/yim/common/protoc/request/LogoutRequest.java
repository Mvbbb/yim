package com.mvbbb.yim.common.protoc.request;

import lombok.Data;

import java.util.Date;

@Data
public class LogoutRequest {
    private String userId;
    private String token;
    private Date timestamp;
}
