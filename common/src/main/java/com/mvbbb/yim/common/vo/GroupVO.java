package com.mvbbb.yim.common.vo;

import com.mvbbb.yim.common.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GroupVO implements Serializable {

    private String groupId;

    private String groupName;
    private String avatar;
    private String ownerUid;
    private int userCnt;
    private List<UserVO> members;
}
