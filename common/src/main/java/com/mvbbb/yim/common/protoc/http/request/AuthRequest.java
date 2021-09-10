package com.mvbbb.yim.common.protoc.http.request;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRequest {
    @NotNull
    private String userId;
    @NotNull
    private String token;
}
