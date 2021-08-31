package com.mvbbb.yim.common.protoc.request;

import lombok.Data;

@Data
public class GroupMemberAddRequest {
    private String groupId;
    private String memberId;
}
