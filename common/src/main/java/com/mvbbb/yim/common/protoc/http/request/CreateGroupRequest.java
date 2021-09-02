package com.mvbbb.yim.common.protoc.http.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateGroupRequest {
    private String groupName;
    private List<String> members;
}
