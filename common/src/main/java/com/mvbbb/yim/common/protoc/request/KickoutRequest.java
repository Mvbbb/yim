package com.mvbbb.yim.common.protoc.request;

import lombok.Data;

import java.util.Date;

@Data
public class KickoutRequest {
    private String userId;
    private Date timestamp;
}
