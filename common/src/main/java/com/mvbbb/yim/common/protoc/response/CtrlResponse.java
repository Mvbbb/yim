package com.mvbbb.yim.common.protoc.response;

import com.mvbbb.yim.common.protoc.enums.CtrlResponseType;
import lombok.Data;

import java.util.Date;

@Data
public class CtrlResponse<T> {
    private String clientMsgId;
    private CtrlResponseType type;
    private String msg;
    private T data;
    private Date timestamp;
}
