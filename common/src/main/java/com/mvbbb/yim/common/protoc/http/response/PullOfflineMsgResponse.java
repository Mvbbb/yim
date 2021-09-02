package com.mvbbb.yim.common.protoc.http.response;

import com.mvbbb.yim.common.protoc.http.OfflineSessionMsg;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class PullOfflineMsgResponse implements Serializable {
    private int cnt;
    private List<OfflineSessionMsg> sessionsMsgs = new ArrayList<>();
}
