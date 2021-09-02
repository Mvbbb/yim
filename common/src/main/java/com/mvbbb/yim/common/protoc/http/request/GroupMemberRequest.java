package com.mvbbb.yim.common.protoc.http.request;

import lombok.Data;

@Data
public class GroupMemberRequest {
    private String groupId;
    private String memberId;
}
