package com.mvbbb.yim.gateway.controller;

import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.common.protoc.http.ResCode;
import com.mvbbb.yim.common.protoc.http.request.CreateGroupRequest;
import com.mvbbb.yim.common.protoc.http.request.GenericRequest;
import com.mvbbb.yim.common.protoc.http.request.GroupMemberRequest;
import com.mvbbb.yim.common.protoc.http.response.GenericResponse;
import com.mvbbb.yim.common.vo.GroupVO;
import com.mvbbb.yim.gateway.CheckAuth;
import com.mvbbb.yim.logic.service.RelationService;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CheckAuth
public class RelationController {

    @DubboReference(check = false)
    RelationService relationService;

    @ApiOperation("获取朋友列表")
    @RequestMapping(path = "/friend/list", method = RequestMethod.GET)
    public GenericResponse<List<User>> friendList(@RequestHeader String userId, @RequestHeader String token) {
        List<User> friends = relationService.listFriend(userId);
        return GenericResponse.success(friends);
    }

    @ApiOperation("添加朋友")
    @RequestMapping(path = "/friend/add", method = RequestMethod.POST)
    public GenericResponse<Object> addFriend(@RequestHeader String userId, @RequestHeader String token, @RequestBody GenericRequest<String> request) {

        String friendId = request.getData();
        relationService.addFriend(userId, friendId);
        return GenericResponse.success();
    }

    @ApiOperation("删除朋友")
    @RequestMapping(path = "/friend/delete", method = RequestMethod.POST)
    public GenericResponse<Object> deleteFriend(@RequestHeader String userId, @RequestHeader String token, @RequestBody GenericRequest<String> request) {
        String friendId = request.getData();
        relationService.deleteFriend(userId, friendId);
        return GenericResponse.success();
    }


    @ApiOperation("查看自己加入的群组")
    @RequestMapping(path = "/group/list", method = RequestMethod.GET)
    public GenericResponse<List<GroupVO>> groupList(@RequestHeader String userId, @RequestHeader String token) {
        List<GroupVO> groups = relationService.listGroup(userId);
        return GenericResponse.success(groups);
    }


    @ApiOperation("查看全部群组")
    @RequestMapping(path = "/group/list/all", method = RequestMethod.GET)
    public GenericResponse<List<GroupVO>> groupAllList(@RequestHeader String userId, @RequestHeader String token) {
        List<GroupVO> groups = relationService.listAllGroup();
        return GenericResponse.success(groups);
    }

    @ApiOperation("加入群组")
    @RequestMapping(path = "/group/join", method = RequestMethod.POST)
    public GenericResponse<GroupVO> groupJoin(@RequestHeader String userId, @RequestHeader String token, @RequestBody GenericRequest<String> request) {
        String groupId = request.getData();

        GroupVO groupVO = relationService.joinGroup(userId, groupId);
        return GenericResponse.success(groupVO);
    }

    @ApiOperation("创建群组")
    @RequestMapping(path = "/group/create", method = RequestMethod.POST)
    public GenericResponse<GroupVO> groupCreate(@RequestHeader String userId, @RequestHeader String token, @RequestBody GenericRequest<CreateGroupRequest> request) {
        List<String> members = request.getData().getMembers();
        String groupName = request.getData().getGroupName();
        GroupVO group = relationService.createGroup(userId, groupName, members);
        return GenericResponse.success(group);
    }

    @ApiOperation("退出群组")
    @RequestMapping(path = "/group/quit", method = RequestMethod.POST)
    public GenericResponse<Object> groupQuit(@RequestHeader String userId, @RequestHeader String token, @RequestBody GenericRequest<String> request) {
        String groupId = request.getData();
        relationService.quitGroup(userId, groupId);
        return GenericResponse.success();
    }

    @ApiOperation("踢人")
    @RequestMapping(path = "/group/member/kickout", method = RequestMethod.POST)
    public GenericResponse<GroupVO> groupMemberKickout(@RequestHeader String userId, @RequestHeader String token, @RequestBody GenericRequest<GroupMemberRequest> request) {

        String groupId = request.getData().getGroupId();
        String kickoutUid = request.getData().getMemberId();

        if (userId.equals(kickoutUid)) {
            return GenericResponse.failed("不能自个儿踢自己");
        }

        GroupVO groupVO = relationService.kickoutGroupMember(userId, groupId, kickoutUid);
        return GenericResponse.success(groupVO);
    }

    @ApiOperation("添加群组成员")
    @RequestMapping(path = "/group/member/add", method = RequestMethod.POST)
    public GenericResponse<GroupVO> groupMemberAdd(@RequestHeader String userId, @RequestHeader String token, @RequestBody GenericRequest<GroupMemberRequest> request) {

        String groupId = request.getData().getGroupId();
        String memberId = request.getData().getMemberId();

        GroupVO groupVO = relationService.addGroupMember(groupId, memberId);
        return GenericResponse.success(groupVO);
    }

    @ApiOperation("查看群组信息")
    @RequestMapping(path = "/group/info", method = RequestMethod.GET)
    public GenericResponse<GroupVO> groupInfo(@RequestHeader String userId, @RequestHeader String token, @RequestParam String groupId) {

        GroupVO groupVO = relationService.getGroupInfo(groupId);
        return GenericResponse.success(groupVO);
    }

    @ApiOperation("解散群组")
    @RequestMapping(path = "/group/dissolution", method = RequestMethod.POST)
    public GenericResponse<Object> groupDissolution(@RequestHeader String userId, @RequestHeader String token, @RequestBody GenericRequest<String> request) {
        String groupId = request.getData();
        boolean success = relationService.dissolutionGroup(userId, groupId);
        return success ? GenericResponse.success() : GenericResponse.failed(ResCode.FAILED);
    }

}