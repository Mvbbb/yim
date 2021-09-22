package com.mvbbb.yim.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GroupVO implements Serializable {

    private String groupId;
    private String groupName;
    private String avatar;
    private String ownerUid;
    private Integer userCnt;
    private List<UserVO> members;
}
