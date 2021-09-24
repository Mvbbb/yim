package com.mvbbb.yim.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yim_msg_recent")
public class MsgRecent implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String fromUid;
    private String toUid;
    private String groupId;
    private int sessionType;
    private String content;
    private String avatar;
    private String name;
    private Date timestamp;
    private boolean deleted;
}
