package com.mvbbb.yim.gateway.controller;

import com.mvbbb.yim.common.entity.Group;
import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.common.protoc.http.request.CreateGroupRequest;
import com.mvbbb.yim.common.protoc.http.request.GenericRequest;
import com.mvbbb.yim.common.protoc.http.response.GenericResponse;
import com.mvbbb.yim.common.protoc.http.request.GroupMemberRequest;
import com.mvbbb.yim.common.vo.GroupVO;
import com.mvbbb.yim.logic.service.RelationService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class RelationController {

    @DubboReference
    RelationService relationService;


    @RequestMapping(path = "/friend/list", method = RequestMethod.GET)
    public GenericResponse<List<User>> friendList(@RequestBody GenericRequest<String> request) {
        String userId = request.getData();
        Date timestamp = new Date(System.currentTimeMillis());

        List<User> friends = relationService.listFriend(userId);

        return GenericResponse.success(friends);
    }

    @RequestMapping(path = "/friend/add", method = RequestMethod.POST)
    public GenericResponse<Object> addFriend(@RequestBody GenericRequest<String> request) {
        String userId = request.getUserId();
        String friendId = request.getData();

        relationService.addFriend(userId,friendId);

        return GenericResponse.success();
    }

    @RequestMapping(path = "/friend/delete", method = RequestMethod.POST)
    public GenericResponse<Object> deleteFriend(@RequestBody GenericRequest<String> request) {
        String userId = request.getUserId();
        String friendId = request.getData();
        relationService.deleteFriend(userId,friendId);
        return GenericResponse.success();
    }

    // 查看自己加入的群组
    @RequestMapping(path = "/group/list", method = RequestMethod.GET)
    public GenericResponse<List<Group>> groupList(@RequestBody GenericRequest<Object> request) {
        String userId = request.getUserId();
        List<Group> groups = relationService.listGroup(userId);
        return GenericResponse.success(groups);
    }

    // 查看全部群组
    @RequestMapping(path = "/group/list/all", method = RequestMethod.GET)
    public GenericResponse<List<Group>> groupAllList(@RequestBody GenericRequest<Object> request) {
        List<Group> groups = relationService.listAllGroup();
        return GenericResponse.success(groups);
    }

    @RequestMapping(path = "/group/join", method = RequestMethod.POST)
    public GenericResponse<GroupVO> groupJoin(@RequestBody GenericRequest<String> request) {
        String userId = request.getUserId();
        String groupId = request.getData();

        GroupVO groupVO = relationService.joinGroup(userId, groupId);
        return GenericResponse.success(groupVO);
    }

    @RequestMapping(path = "/group/create", method = RequestMethod.POST)
    public GenericResponse<GroupVO> groupCreate(@RequestBody GenericRequest<CreateGroupRequest> request) {
        String userId = request.getUserId();
        List<String> members = request.getData().getMembers();
        String groupName = request.getData().getGroupName();
        GroupVO group = relationService.createGroup(userId, groupName, members);
        return GenericResponse.success(group);
    }

    @RequestMapping(path = "/group/quit", method = RequestMethod.POST)
    public GenericResponse<Object> groupQuit(@RequestBody GenericRequest<String> request) {
        String userId = request.getUserId();
        String groupId = request.getData();
        relationService.quitGroup(userId,groupId);
        return GenericResponse.success();
    }

    @RequestMapping(path = "/group/member/kickout", method = RequestMethod.POST)
    public GenericResponse<GroupVO> groupMemberKickout(@RequestBody GenericRequest<GroupMemberRequest> request) {

        String groupId = request.getData().getGroupId();
        String kickoutMember = request.getData().getMemberId();
        GroupVO groupVO = relationService.kickoutGroupMember(groupId, kickoutMember);
        return GenericResponse.success(groupVO);
    }

    @RequestMapping(path = "/group/member/add", method = RequestMethod.POST)
    public GenericResponse<GroupVO> groupMemberAdd(@RequestBody GenericRequest<GroupMemberRequest> request) {

        String groupId = request.getData().getGroupId();
        String memberId = request.getData().getMemberId();

        GroupVO groupVO = relationService.addGroupMember(groupId, memberId);
        return GenericResponse.success(groupVO);
    }

    @RequestMapping(path = "/group/info", method = RequestMethod.GET)
    public GenericResponse<GroupVO> groupInfo(@RequestBody GenericRequest<String> request){
        String groupId = request.getData();
        GroupVO groupVO = relationService.getGroupInfo(groupId);
        return GenericResponse.success(groupVO);
    }

    @RequestMapping(path = "/group/dissolution", method = RequestMethod.POST)
    public GenericResponse<Object> groupDissolution(@RequestBody GenericRequest<String> request) {
        String groupId = request.getData();
        relationService.dissolutionGroup(groupId);
        return GenericResponse.success();
    }

}