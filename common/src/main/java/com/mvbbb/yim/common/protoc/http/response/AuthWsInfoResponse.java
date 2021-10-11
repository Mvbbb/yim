package com.mvbbb.yim.common.protoc.http.response;

import com.mvbbb.yim.common.vo.UserVO;
import lombok.Data;

@Data
public class AuthWsInfoResponse {
    private UserVO info;
    private String token;
    private String wsUrl;
}
