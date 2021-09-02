package com.mvbbb.yim.common.protoc.http.request;

import lombok.Data;

@Data
public class GenericRequest<T> {
    private String userId;
    private String token;
    private T data;
}
