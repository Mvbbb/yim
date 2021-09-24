package com.mvbbb.yim.common.protoc.http;

import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.vo.MsgVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class RecentChatItem implements Serializable {
    private SessionType sessionType;
    private String fromUid;
    private String toUid;
    private String groupId;
    private String name;
    private String avatar;
    private Integer unread;
    private String latestMsgContent;
    private List<MsgVO> msgs;
    private Date latestMsgDate;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RecentChatItem) {
            RecentChatItem chatItem = (RecentChatItem) obj;
            if (chatItem.getSessionType() == SessionType.SINGLE && this.sessionType == SessionType.SINGLE) {
                return (chatItem.getFromUid().equals(this.fromUid) && chatItem.getToUid().equals(this.toUid))
                        ||(chatItem.getFromUid().equals(this.toUid)&&chatItem.getToUid().equals(this.fromUid));
            } else if (chatItem.getSessionType() == SessionType.GROUP && this.sessionType == SessionType.GROUP) {
                return chatItem.getGroupId().equals(this.groupId);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
