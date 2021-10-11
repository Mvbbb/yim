package com.mvbbb.yim.logic.service;

import com.mvbbb.yim.common.vo.GroupVO;
import com.mvbbb.yim.common.vo.UserVO;

import java.util.List;

public interface RelationService {

    List<UserVO> listFriend(String userId);

    void addFriend(String userId, String friendId);

    void deleteFriend(String userId, String friendId);

    List<GroupVO> listGroup(String userId);

    GroupVO joinGroup(String userId, String groupId);

    GroupVO createGroup(String userId, String groupName, List<String> members);

    void quitGroup(String userId, String groupId);

    GroupVO kickoutGroupMember(String ownerId, String groupId, String kickoutUid);

    GroupVO addGroupMember(String groupId, String userId);

    GroupVO getGroupInfo(String groupId);

    boolean dissolutionGroup(String userId, String groupId);

    List<GroupVO> listAllGroup();

    boolean isFriend(String userId1, String userId2);

    boolean isMember(String userId, String groupId);

}
