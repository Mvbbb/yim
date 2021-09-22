package com.mvbbb.yim.common.protoc.http.request;

import lombok.Data;

@Data
public class GenericRequest<T> {
    private T data;
}
