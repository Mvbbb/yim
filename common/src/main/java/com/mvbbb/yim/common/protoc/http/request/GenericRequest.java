package com.mvbbb.yim.common.protoc.http.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GenericRequest<T> {
//    @NotNull
//    private String userId;
//    @NotNull
//    private String token;
    private T data;
}
