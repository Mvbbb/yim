package com.mvbbb.yim.common.protoc.request;

import com.mvbbb.yim.common.protoc.enums.CtrlRequestType;
import lombok.Data;

@Data
public class CtrlRequest<T> {
    private String userId;
    private String token;
    private String clientMsgId;
    private CtrlRequestType type;
    private T data;

}