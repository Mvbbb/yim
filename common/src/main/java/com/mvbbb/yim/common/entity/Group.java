package com.mvbbb.yim.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@TableName("yim_group")
@ToString
public class Group implements Serializable {
    @TableId
    private String groupId;

    private String groupName;
    private String avatar;
    private String ownerUid;
    private int userCnt;
    @TableLogic
    private boolean deleted;

}
