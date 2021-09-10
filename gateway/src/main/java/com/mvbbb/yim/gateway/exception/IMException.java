package com.mvbbb.yim.gateway.exception;

import com.mvbbb.yim.common.protoc.http.ResCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class IMException extends RuntimeException{
    private static final long serialVersionUID = 194906846739586856L;

    protected ResCode resultCode;

    protected Object data;


    public ResCode getResultCode() {
        return resultCode;
    }

    public Object getData() {
        return data;
    }
}
