package com.mvbbb.yim.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("yim_friend_relation")
public class FriendRelation implements Serializable {
    @TableId
    private long id;

    @TableField("user_id_1")
    private String userid1;

    @TableField("user_id_2")
    private String userid2;

    
    private boolean deleted;
}
