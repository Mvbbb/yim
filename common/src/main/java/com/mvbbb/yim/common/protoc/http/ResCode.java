package com.mvbbb.yim.common.protoc.http;

import lombok.ToString;

@ToString
public enum ResCode {
    SUCCESS(200,"success"),
    FAILED(400,"failed"),
    SYSTEM_ERROR(500,"服务器内部错误");

    private int code;
    private String msg;

    ResCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public ResCode setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResCode setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
