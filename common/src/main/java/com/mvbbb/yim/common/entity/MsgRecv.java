package com.mvbbb.yim.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yim_msg_recv")
public class MsgRecv implements Serializable {
    @TableId
    private long id;
    private String clientMsgId;
    private long serverMsgId;
    private String fromUid;
    private String toUid;
    private String groupId;
    private int sessionType;
    private int msgType;
    private String msgData;
    private Date timestamp;
    private boolean delivered;
    @TableLogic
    private boolean deleted;
}
