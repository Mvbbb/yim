package com.mvbbb.yim.common.protoc.http.response;

import com.mvbbb.yim.common.protoc.http.RecentChatItem;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class RecentChatResponse implements Serializable {
    private List<RecentChatItem> recentChatItems;
}
