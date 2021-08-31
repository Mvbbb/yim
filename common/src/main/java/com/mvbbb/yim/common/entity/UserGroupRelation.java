package com.mvbbb.yim.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("yim_user_group_relation")
public class UserGroupRelation implements Serializable {

    @TableId
    private long id;
    private String userId;
    private String groupId;
    @TableField("last_acked_msgid")
    private long lastAckedMsgid;
    @TableLogic
    private boolean deleted;
}
