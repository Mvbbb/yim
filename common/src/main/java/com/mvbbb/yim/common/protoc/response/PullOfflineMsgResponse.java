package com.mvbbb.yim.common.protoc.response;

import com.mvbbb.yim.common.protoc.OfflineSessionMsg;
import lombok.Data;

import java.util.List;

@Data
public class PullOfflineMsgResponse {
    private List<OfflineSessionMsg> sessionsMsgs;
}
