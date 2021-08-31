package com.mvbbb.yim.gateway.controller;

import com.mvbbb.yim.common.entity.Group;
import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.common.protoc.DataPacket;
import com.mvbbb.yim.common.protoc.request.CreateGroupRequest;
import com.mvbbb.yim.common.protoc.request.CtrlRequest;
import com.mvbbb.yim.common.protoc.request.GroupMemberAddRequest;
import com.mvbbb.yim.common.protoc.response.CtrlResponse;
import com.mvbbb.yim.common.util.DataPacketBuilder;
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
    public DataPacket<CtrlResponse<List<User>>> friendList(@RequestBody DataPacket<CtrlRequest<Object>> dataPacket) {
        String userId = dataPacket.getData().getUserId();
        Date timestamp = new Date(System.currentTimeMillis());

        List<User> friends = relationService.listFriend(userId);

        return DataPacketBuilder.buildOkCtrlResponse(dataPacket.getData().getClientMsgId(),"成功",timestamp,friends);
    }

    @RequestMapping(path = "/friend/add", method = RequestMethod.POST)
    public DataPacket<CtrlResponse<Object>> addFriend(@RequestBody DataPacket<CtrlRequest<String>> dataPacket) {
        String friendId = dataPacket.getData().getData();
        String userId = dataPacket.getData().getUserId();
        Date timestamp = new Date(System.currentTimeMillis());

        relationService.addFriend(userId,friendId);

        return DataPacketBuilder.buildOkCtrlResponse(dataPacket.getData().getClientMsgId(),"成功",timestamp,null);
    }

    @RequestMapping(path = "/friend/delete", method = RequestMethod.POST)
    public DataPacket<CtrlResponse<Object>> deleteFriend(@RequestBody DataPacket<CtrlRequest<String>> dataPacket) {
        String clientMsgId = dataPacket.getData().getClientMsgId();
        String userId = dataPacket.getData().getUserId();
        String friendId = dataPacket.getData().getData();
        Date timestamp = new Date(System.currentTimeMillis());

        relationService.deleteFriend(userId,friendId);
        return DataPacketBuilder.buildOkCtrlResponse(clientMsgId,"成功",timestamp,null);
    }

    // 查看自己加入的群组
    @RequestMapping(path = "/group/list", method = RequestMethod.GET)
    public DataPacket<CtrlResponse<List<Group>>> groupList(@RequestBody DataPacket<CtrlRequest<Object>> dataPacket) {
        String clientMsgId = dataPacket.getData().getClientMsgId();
        String userId = dataPacket.getData().getUserId();
        Date timestamp = new Date(System.currentTimeMillis());
        List<Group> groups = relationService.listGroup(userId);
        return DataPacketBuilder.buildOkCtrlResponse(clientMsgId,"成功",timestamp,groups);
    }

    // 查看全部群组
    @RequestMapping(path = "/group/list/all", method = RequestMethod.GET)
    public DataPacket<CtrlResponse<List<Group>>> groupAllList(@RequestBody DataPacket<CtrlRequest<Object>> dataPacket) {
        String clientMsgId = dataPacket.getData().getClientMsgId();
        Date timestamp = new Date(System.currentTimeMillis());
        List<Group> groups = relationService.listAllGroup();
        return DataPacketBuilder.buildOkCtrlResponse(clientMsgId,"成功",timestamp,groups);
    }

    @RequestMapping(path = "/group/join", method = RequestMethod.POST)
    public DataPacket<CtrlResponse<GroupVO>> groupJoin(@RequestBody DataPacket<CtrlRequest<String>> dataPacket) {
        String userId = dataPacket.getData().getUserId();
        String groupId = dataPacket.getData().getData();
        Date timestamp = new Date(System.currentTimeMillis());
        String clientMsgId = dataPacket.getData().getClientMsgId();

        GroupVO groupVO = relationService.joinGroup(userId, groupId);

        return DataPacketBuilder.buildOkCtrlResponse(clientMsgId,"成功",timestamp,groupVO);
    }

    @RequestMapping(path = "/group/create", method = RequestMethod.POST)
    public DataPacket<CtrlResponse<GroupVO>> groupCreate(@RequestBody DataPacket<CtrlRequest<CreateGroupRequest>> dataPacket) {
        String userId = dataPacket.getData().getUserId();
        List<String> members = dataPacket.getData().getData().getMembers();
        String groupName = dataPacket.getData().getData().getGroupName();
        Date timestamp = new Date(System.currentTimeMillis());
        String clientMsgId = dataPacket.getData().getClientMsgId();
        GroupVO group = relationService.createGroup(userId, groupName, members);
        return DataPacketBuilder.buildOkCtrlResponse(clientMsgId,"成功",timestamp,group);
    }

    @RequestMapping(path = "/group/quit", method = RequestMethod.POST)
    public DataPacket<CtrlResponse<Object>> groupQuit(@RequestBody DataPacket<CtrlRequest<String>> dataPacket) {
        String userId = dataPacket.getData().getUserId();
        String groupId = dataPacket.getData().getData();
        Date timestamp = new Date(System.currentTimeMillis());
        String clientMsgId = dataPacket.getData().getClientMsgId();
        relationService.quitGroup(userId,groupId);
        return DataPacketBuilder.buildOkCtrlResponse(clientMsgId,"成功",timestamp,null);
    }

    @RequestMapping(path = "/group/member/kickout", method = RequestMethod.POST)
    public DataPacket<CtrlResponse<GroupVO>> groupMemberKickout(@RequestBody DataPacket<CtrlRequest<String>> dataPacket) {
        String userId = dataPacket.getData().getUserId();
        String groupId = dataPacket.getData().getData();
        Date timestamp = new Date(System.currentTimeMillis());
        String clientMsgId = dataPacket.getData().getClientMsgId();
        GroupVO groupVO = relationService.kickoutGroupMember(groupId, userId);
        return DataPacketBuilder.buildOkCtrlResponse(clientMsgId,"成功",timestamp,groupVO);
    }

    @RequestMapping(path = "/group/member/add", method = RequestMethod.POST)
    public DataPacket<CtrlResponse<GroupVO>> groupMemberAdd(@RequestBody DataPacket<CtrlRequest<GroupMemberAddRequest>> dataPacket) {
        String userId = dataPacket.getData().getUserId();
        String groupId = dataPacket.getData().getData().getGroupId();
        String memberId = dataPacket.getData().getData().getMemberId();
        Date timestamp = new Date(System.currentTimeMillis());
        String clientMsgId = dataPacket.getData().getClientMsgId();
        GroupVO groupVO = relationService.addGroupMember(groupId, memberId);
        return DataPacketBuilder.buildOkCtrlResponse(clientMsgId,"成功",timestamp,groupVO);
    }

    @RequestMapping(path = "/group/info", method = RequestMethod.GET)
    public DataPacket<CtrlResponse<GroupVO>> groupInfo(@RequestBody DataPacket<CtrlRequest<String>> dataPacket){
        String groupId = dataPacket.getData().getData();
        Date timestamp = new Date(System.currentTimeMillis());
        String clientMsgId = dataPacket.getData().getClientMsgId();

        GroupVO groupVO = relationService.getGroupInfo(groupId);
        return DataPacketBuilder.buildOkCtrlResponse(clientMsgId,"成功",timestamp,groupVO);
    }

    @RequestMapping(path = "/group/dissolution", method = RequestMethod.POST)
    public DataPacket<CtrlResponse<Object>> groupDissolution(@RequestBody DataPacket<CtrlRequest<String>> dataPacket) {
        String groupId = dataPacket.getData().getData();
        Date timestamp = new Date(System.currentTimeMillis());
        String clientMsgId = dataPacket.getData().getClientMsgId();

        relationService.dissolutionGroup(groupId);
        return DataPacketBuilder.buildOkCtrlResponse(clientMsgId,"成功",timestamp,null);
    }

}