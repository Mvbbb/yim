package com.mvbbb.yim.common.protoc.http.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateGroupRequest {
    @NotNull
    private String groupName;
    private List<String> members;
}
