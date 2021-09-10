package com.mvbbb.yim.common.protoc.http.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GroupMemberRequest {
    @NotNull
    private String groupId;
    @NotNull
    private String memberId;
}
