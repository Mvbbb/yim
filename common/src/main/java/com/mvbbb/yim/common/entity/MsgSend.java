package com.mvbbb.yim.common.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yim_msg_send")
public class MsgSend implements Serializable {
    @TableId
    private String id;
    private String clientMsgId;
    private long serverMsgId;
    private String fromUid;
    private String toUid;
    private String groupId;
    private int sessionType;
    private int msgType;
    private String msgData;
    private Date timestamp;
    @TableLogic
    private boolean deleted;
}
