package com.mvbbb.yim.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@TableName("yim_user")
public class User implements Serializable {
    @TableId
    private String userId;
    private String username;
    private String avatar;
    private String password;
    @TableLogic
    private boolean deleted;
}
