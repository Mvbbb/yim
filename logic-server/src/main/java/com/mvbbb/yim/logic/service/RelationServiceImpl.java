package com.mvbbb.yim.logic.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mvbbb.yim.common.entity.FriendRelation;
import com.mvbbb.yim.common.entity.Group;
import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.common.entity.UserGroupRelation;
import com.mvbbb.yim.common.exception.IMException;
import com.mvbbb.yim.common.mapper.FriendRelationMapper;
import com.mvbbb.yim.common.mapper.GroupMapper;
import com.mvbbb.yim.common.mapper.UserGroupRelationMapper;
import com.mvbbb.yim.common.mapper.UserMapper;
import com.mvbbb.yim.common.util.GenRandomUtil;
import com.mvbbb.yim.common.vo.GroupVO;
import com.mvbbb.yim.common.vo.UserVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@DubboService
public class RelationServiceImpl implements RelationService {

    @Resource
    FriendRelationMapper friendRelationMapper;
    @Resource
    UserGroupRelationMapper groupRelationMapper;
    @Resource
    GroupMapper groupMapper;
    @Resource
    UserStatusService userStatusService;
    @Resource
    UserMapper userMapper;
    private Logger logger = LoggerFactory.getLogger(RelationServiceImpl.class);

    @Override
    public List<UserVO> listFriend(String userId) {
        List<String> friendIds = friendRelationMapper.selectFriendRelation(userId).stream().map((friendRelation -> {
            return friendRelation.getUserid1().equals(userId) ? friendRelation.getUserid2() : friendRelation.getUserid1();
        })).collect(Collectors.toList());
        return friendIds.stream().map((friendId) -> {
            UserVO userVO = new UserVO();
            User user = userMapper.selectById(friendId);
            BeanUtil.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
    }

    @Override
    public void addFriend(String userId, String friendId) {

        User user = userMapper.selectById(friendId);
        if (user == null) {
            throw new IMException("???????????????");
        }

        FriendRelation friendRelation = null;
        friendRelation = friendRelationMapper.findFriendRelation(userId, friendId);
        if (friendRelation != null) {
            throw new IMException("???????????????????????????????????????");
        }
        friendRelation = new FriendRelation();
        friendRelation.setUserid1(userId);
        friendRelation.setUserid2(friendId);
        friendRelationMapper.insert(friendRelation);
    }

    @Override
    public void deleteFriend(String userId, String friendId) {
        friendRelationMapper.deleteFriendRelation(userId, friendId);
    }

    @Override
    public List<GroupVO> listGroup(String userId) {
        List<String> groupIds = groupRelationMapper.findUserGroupIds(userId).stream().distinct().collect(Collectors.toList());
        return groupIds.stream()
                .map((groupId) -> groupMapper.selectById(groupId))
                .map((group -> {
                    GroupVO groupVO = new GroupVO();
                    BeanUtil.copyProperties(group, groupVO);
                    return groupVO;
                })).collect(Collectors.toList());
    }

    @Override
    public GroupVO joinGroup(String userId, String groupId) {

        if (userMapper.selectById(userId) == null) {
            throw new IMException("????????????????????????????????????");
        }

        UserGroupRelation userGroupRelation = null;
        userGroupRelation = groupRelationMapper.findGroupRelation(userId, groupId);
        if (userGroupRelation != null) {
            throw new IMException("??????????????????????????????????????????");
        }
        userGroupRelation = new UserGroupRelation();
        userGroupRelation.setGroupId(groupId);
        userGroupRelation.setUserId(userId);
        userGroupRelation.setLastAckedMsgid(0);
        groupRelationMapper.insert(userGroupRelation);
        groupMapper.addOneMember(groupId);
        return getGroupInfo(groupId);
    }

    @Override
    public GroupVO createGroup(String userId, String groupName, List<String> members) {
        if (members == null) {
            members = new ArrayList<>();
        }
        members.add(userId);
        // ??????
        members = members.stream().distinct().collect(Collectors.toList());

        Group group = new Group();

        String groupId = null;
        int retry = 0;
        while (true) {
            groupId = GenRandomUtil.genGroupid();
            Group existGroup = groupMapper.selectById(groupId);
            if (existGroup != null) {
                retry++;
                if (retry == 3) {
                    throw new IMException("????????????id?????????????????????????????????");
                }
            } else {
                break;
            }
        }

        group.setGroupId(groupId);
        group.setGroupName(groupName);
        group.setAvatar(GenRandomUtil.randomAvatar());
        group.setOwnerUid(userId);
        group.setUserCnt(members.size());
        groupMapper.insert(group);
        members.forEach((memberId) -> {
            User user = userMapper.selectById(memberId);
            if (user == null) {
                logger.error("user not exist, can not add to group. user: [{}]", user);
            }
            UserGroupRelation groupRelation = new UserGroupRelation();
            groupRelation.setGroupId(group.getGroupId());
            groupRelation.setUserId(memberId);
            groupRelation.setLastAckedMsgid(-1);
            groupRelationMapper.insert(groupRelation);
        });
        return getGroupInfo(group.getGroupId());
    }

    @Override
    public void quitGroup(String userId, String groupId) {
        Group group = groupMapper.selectById(groupId);
        if (group == null) {
            throw new IMException("???????????????");
        }
        if (group.getOwnerUid().equals(userId)) {
            throw new IMException("??????????????????????????????");
        }
        groupRelationMapper.deleteUserGroupRelation(userId, groupId);
        groupMapper.removeOneMember(groupId);
    }

    @Override
    public GroupVO kickoutGroupMember(String ownerId, String groupId, String kickoutUid) {

        Group group = groupMapper.selectById(groupId);
        if (group == null) {
            throw new IMException("???????????????");
        }
        if (group.getOwnerUid() != ownerId) {
            throw new IMException("???????????????????????????");
        }

        UserGroupRelation groupRelation = groupRelationMapper.findGroupRelation(kickoutUid, groupId);
        if (groupRelation == null) {
            throw new IMException("???????????????????????????????????????");
        }

        int delete = groupRelationMapper.deleteUserGroupRelation(kickoutUid, groupId);
        groupMapper.removeOneMember(groupId);
        return getGroupInfo(groupId);
    }

    @Override
    public GroupVO addGroupMember(String groupId, String userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new IMException("???????????????");
        }
        Group group = groupMapper.selectById(groupId);
        if (group == null) {
            throw new IMException("???????????????");
        }
        return joinGroup(userId, groupId);
    }

    @Override
    public GroupVO getGroupInfo(String groupId) {

        if (groupId == null) {
            throw new IMException("???????????? groupId");
        }
        GroupVO groupVO = new GroupVO();
        Group group = groupMapper.selectById(groupId);
        if (group == null) {
            throw new IMException("???????????????" + groupId);
        }

        // ??????????????????????????????id???????????????
        List<String> userIds = groupRelationMapper.selectList(new LambdaQueryWrapper<UserGroupRelation>().eq(UserGroupRelation::getGroupId, groupId))
                .stream().map(UserGroupRelation::getUserId).collect(Collectors.toList());
        List<UserVO> members = userIds.stream().map((memberId) -> {
            User user = userMapper.selectById(memberId);
            UserVO userVO = new UserVO();
            userVO.setOnline(userStatusService.isUserOnline(memberId));
            BeanUtil.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
        groupVO.setGroupId(group.getGroupId());
        groupVO.setGroupName(group.getGroupName());
        groupVO.setAvatar(group.getAvatar());
        groupVO.setOwnerUid(group.getOwnerUid());
        groupVO.setUserCnt(members.size());
        groupVO.setMembers(members);
        return groupVO;
    }

    @Override
    public boolean dissolutionGroup(String userId, String groupId) {
        Group group = groupMapper.selectGroup(userId, groupId);
        if (group == null) {
            logger.error("group not exist. group: [{}]", groupId);
            return false;
        }
        // ?????????????????????
        int delete = groupRelationMapper.delete(new LambdaQueryWrapper<UserGroupRelation>().eq(UserGroupRelation::getGroupId, groupId));
        // ?????????
        int delete1 = groupMapper.delete(new LambdaQueryWrapper<Group>().eq(Group::getGroupId, groupId));
        return true;
    }

    @Override
    public List<GroupVO> listAllGroup() {
        return groupMapper.selectAll().stream().map((group -> {
            GroupVO groupVO = new GroupVO();
            BeanUtil.copyProperties(group, groupVO);
            return groupVO;
        })).collect(Collectors.toList());
    }

    @Override
    public boolean isFriend(String userId1, String userId2) {
        FriendRelation friendRelation = friendRelationMapper.findFriendRelation(userId1, userId2);
        return friendRelation != null;
    }

    @Override
    public boolean isMember(String userId, String groupId) {
        UserGroupRelation groupRelation = groupRelationMapper.findGroupRelation(userId, groupId);
        return groupRelation != null;
    }
}
