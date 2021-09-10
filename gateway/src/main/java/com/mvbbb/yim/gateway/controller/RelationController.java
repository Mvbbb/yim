package com.mvbbb.yim.gateway.controller;

import com.mvbbb.yim.common.entity.Group;
import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.common.protoc.http.ResCode;
import com.mvbbb.yim.common.protoc.http.request.CreateGroupRequest;
import com.mvbbb.yim.common.protoc.http.request.GenericRequest;
import com.mvbbb.yim.common.protoc.http.response.GenericResponse;
import com.mvbbb.yim.common.protoc.http.request.GroupMemberRequest;
import com.mvbbb.yim.common.vo.GroupVO;
import com.mvbbb.yim.logic.service.RelationService;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class RelationController {

    @DubboReference(check = false)
    RelationService relationService;


    @ApiOperation("获取朋友列表")
    @RequestMapping(path = "/friend/list", method = RequestMethod.GET)
    public GenericResponse<List<User>> friendList(@RequestBody GenericRequest<Object> request) {
        String userId = request.getUserId();
        List<User> friends = relationService.listFriend(userId);

        return GenericResponse.success(friends);
    }

    @ApiOperation("添加朋友")
    @RequestMapping(path = "/friend/add", method = RequestMethod.POST)
    public GenericResponse<Object> addFriend(@RequestBody GenericRequest<String> request) {
        String userId = request.getUserId();
        String friendId = request.getData();

        relationService.addFriend(userId, friendId);

        return GenericResponse.success();
    }

    @ApiOperation("删除朋友")
    @RequestMapping(path = "/friend/delete", method = RequestMethod.POST)
    public GenericResponse<Object> deleteFriend(@RequestBody GenericRequest<String> request) {
        String userId = request.getUserId();
        String friendId = request.getData();
        relationService.deleteFriend(userId, friendId);
        return GenericResponse.success();
    }


    @ApiOperation("查看自己加入的群组")
    @RequestMapping(path = "/group/list", method = RequestMethod.GET)
    public GenericResponse<List<Group>> groupList(@RequestBody GenericRequest<Object> request) {
        String userId = request.getUserId();
        List<Group> groups = relationService.listGroup(userId);
        return GenericResponse.success(groups);
    }


    @ApiOperation("查看全部群组")
    @RequestMapping(path = "/group/list/all", method = RequestMethod.GET)
    public GenericResponse<List<Group>> groupAllList(@RequestBody GenericRequest<Object> request) {
        List<Group> groups = relationService.listAllGroup();
        return GenericResponse.success(groups);
    }

    @ApiOperation("加入群组")
    @RequestMapping(path = "/group/join", method = RequestMethod.POST)
    public GenericResponse<GroupVO> groupJoin(@RequestBody GenericRequest<String> request) {
        String userId = request.getUserId();
        String groupId = request.getData();

        GroupVO groupVO = relationService.joinGroup(userId, groupId);
        return GenericResponse.success(groupVO);
    }

    @ApiOperation("创建群组")
    @RequestMapping(path = "/group/create", method = RequestMethod.POST)
    public GenericResponse<GroupVO> groupCreate(@RequestBody GenericRequest<CreateGroupRequest> request) {
        String userId = request.getUserId();
        List<String> members = request.getData().getMembers();
        String groupName = request.getData().getGroupName();
        GroupVO group = relationService.createGroup(userId, groupName, members);
        return GenericResponse.success(group);
    }

    @ApiOperation("退出群组")
    @RequestMapping(path = "/group/quit", method = RequestMethod.POST)
    public GenericResponse<Object> groupQuit(@RequestBody GenericRequest<String> request) {
        String userId = request.getUserId();
        String groupId = request.getData();
        relationService.quitGroup(userId, groupId);
        return GenericResponse.success();
    }

    @ApiOperation("踢人")
    @RequestMapping(path = "/group/member/kickout", method = RequestMethod.POST)
    public GenericResponse<GroupVO> groupMemberKickout(@RequestBody GenericRequest<GroupMemberRequest> request) {

        String groupId = request.getData().getGroupId();
        String kickoutMember = request.getData().getMemberId();
        GroupVO groupVO = relationService.kickoutGroupMember(groupId, kickoutMember);
        return GenericResponse.success(groupVO);
    }

    @ApiOperation("添加群组成员")
    @RequestMapping(path = "/group/member/add", method = RequestMethod.POST)
    public GenericResponse<GroupVO> groupMemberAdd(@RequestBody GenericRequest<GroupMemberRequest> request) {

        String groupId = request.getData().getGroupId();
        String memberId = request.getData().getMemberId();

        GroupVO groupVO = relationService.addGroupMember(groupId, memberId);
        return GenericResponse.success(groupVO);
    }

    @ApiOperation("查看群组信息")
    @RequestMapping(path = "/group/info", method = RequestMethod.GET)
    public GenericResponse<GroupVO> groupInfo(@RequestBody GenericRequest<String> request) {
        String groupId = request.getData();
        GroupVO groupVO = relationService.getGroupInfo(groupId);
        return GenericResponse.success(groupVO);
    }

    @ApiOperation("解散群组")
    @RequestMapping(path = "/group/dissolution", method = RequestMethod.POST)
    public GenericResponse<Object> groupDissolution(@RequestBody GenericRequest<String> request) {
        String groupId = request.getData();
        String userId = request.getUserId();
        boolean success = relationService.dissolutionGroup(userId, groupId);
        return success ? GenericResponse.success() : GenericResponse.failed(ResCode.FAILED);
    }

}