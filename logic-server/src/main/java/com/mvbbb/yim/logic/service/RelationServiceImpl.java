package com.mvbbb.yim.logic.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mvbbb.yim.common.entity.FriendRelation;
import com.mvbbb.yim.common.entity.Group;
import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.common.entity.UserGroupRelation;
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
import java.util.List;
import java.util.stream.Collectors;

@DubboService
public class RelationServiceImpl implements RelationService{

    private Logger logger = LoggerFactory.getLogger(RelationServiceImpl.class);
    @Resource
    FriendRelationMapper friendRelationMapper;
    @Resource
    UserGroupRelationMapper groupRelationMapper;
    @Resource
    GroupMapper groupMapper;
    @Resource
    UserMapper userMapper;

    @Override
    public List<User> listFriend(String userId) {
        List<String> friendIds = friendRelationMapper.selectFriendRelation(userId).stream().map((friendRelation -> {
            return friendRelation.getUserid1().equals(userId) ? friendRelation.getUserid2() : friendRelation.getUserid1();
        })).collect(Collectors.toList());
        return friendIds.stream().map((friendId) -> {
            return userMapper.selectById(friendId);
        }).collect(Collectors.toList());
    }

    @Override
    public void addFriend(String userId, String friendId) {
        FriendRelation friendRelation = new FriendRelation();
        friendRelation.setUserid1(userId);
        friendRelation.setUserid2(friendId);
        friendRelationMapper.insert(friendRelation);
    }

    @Override
    public void deleteFriend(String userId, String friendId) {
        friendRelationMapper.deleteFriendRelation(userId,friendId);
    }

    @Override
    public List<Group> listGroup(String userId) {
        List<String> groupIds = groupRelationMapper.selectList(new LambdaQueryWrapper<UserGroupRelation>().eq(UserGroupRelation::getUserId, userId))
                .stream().map(UserGroupRelation::getGroupId).collect(Collectors.toList());
        return groupIds.stream().map((groupId) -> groupMapper.selectById(groupId)).collect(Collectors.toList());
    }

    @Override
    public GroupVO joinGroup(String userId, String groupId) {
        UserGroupRelation userGroupRelation = new UserGroupRelation();
        userGroupRelation.setGroupId(groupId);
        userGroupRelation.setUserId(userId);
        userGroupRelation.setLastAckedMsgid(0);
        groupRelationMapper.insert(userGroupRelation);
        groupMapper.addOneMember(groupId);
        return getGroupInfo(groupId);
    }

    @Override
    public GroupVO createGroup(String userId, String groupName, List<String> members) {
        Group group = new Group();
        group.setGroupId(GenRandomUtil.genGroupid());
        group.setGroupName(groupName);
        group.setAvatar(GenRandomUtil.randomAvatar());
        group.setOwnerUid(userId);
        group.setUserCnt(members.size()+1);
        groupMapper.insert(group);
        UserGroupRelation ownerGroupRelation = new UserGroupRelation();
        ownerGroupRelation.setGroupId(group.getGroupId());
        ownerGroupRelation.setUserId(userId);
        ownerGroupRelation.setLastAckedMsgid(-1);
        groupRelationMapper.insert(ownerGroupRelation);
        members.forEach((memberId)->{
            User user = userMapper.selectById(memberId);
            if(user==null){
                logger.error("user not exist, can not add to group. user: [{}]",user);
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
        int delete = groupRelationMapper.delete(new LambdaQueryWrapper<UserGroupRelation>().eq(UserGroupRelation::getGroupId, groupId).eq(UserGroupRelation::getUserId, userId));
        groupMapper.removeOneMember(groupId);
    }

    @Override
    public GroupVO kickoutGroupMember(String groupId, String userId) {
        int delete = groupRelationMapper.delete(new LambdaQueryWrapper<UserGroupRelation>().eq(UserGroupRelation::getGroupId, groupId).eq(UserGroupRelation::getUserId, userId));
        groupMapper.removeOneMember(groupId);
        return getGroupInfo(groupId);
    }

    @Override
    public GroupVO addGroupMember(String groupId, String userId) {
        return joinGroup(userId,groupId);
    }

    @Override
    public GroupVO getGroupInfo(String groupId) {

        GroupVO groupVO = new GroupVO();
        Group group = groupMapper.selectById(groupId);

        // 查询群组内的所有成员id，包含群主
        List<String> userIds = groupRelationMapper.selectList(new LambdaQueryWrapper<UserGroupRelation>().eq(UserGroupRelation::getGroupId, groupId))
                .stream().map(UserGroupRelation::getUserId).collect(Collectors.toList());
        List<UserVO> members = userIds.stream().map((memberId) -> {
            User user = userMapper.selectById(memberId);
            UserVO userVO = new UserVO();
            BeanUtil.copyProperties(user,userVO);
            return userVO;
        }).collect(Collectors.toList());
        groupVO.setUserCnt(members.size());
        groupVO.setMembers(members);
        return groupVO;
    }

    @Override
    public boolean dissolutionGroup(String userId,String groupId) {
        Group group = groupMapper.selectGroup(userId,groupId);
        if(group==null){
            logger.error("group not exist. group: [{}]",groupId);
            return false;
        }
        int delete = groupRelationMapper.delete(new LambdaQueryWrapper<UserGroupRelation>().eq(UserGroupRelation::getGroupId, groupId));
        int delete1 = groupMapper.delete(new LambdaQueryWrapper<Group>().eq(Group::getGroupId, groupId));
        return true;
    }

    @Override
    public List<Group> listAllGroup() {
        return groupMapper.selectAll();
    }
}
