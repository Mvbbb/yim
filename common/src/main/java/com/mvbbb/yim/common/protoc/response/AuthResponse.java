package com.mvbbb.yim.common.protoc.response;

import com.mvbbb.yim.common.protoc.enums.AuthResponseEnum;
import lombok.Data;

@Data
public class AuthResponse {
    private String userId;
    private String token;
    private AuthResponseEnum status;
    private String errMsg;
}
