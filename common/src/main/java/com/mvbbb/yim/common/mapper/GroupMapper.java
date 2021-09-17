package com.mvbbb.yim.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mvbbb.yim.common.entity.Group;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface GroupMapper extends BaseMapper<Group> {

    @Select("select * from yim_group")
    List<Group> selectAll();

    @Update("update yim_group set user_cnt = user_cnt+1 where group_id = ${groupId}")
    int addOneMember(@Param("groupId") String groupId);

    @Update("update yim_group set user_cnt = user_cnt-1 where group_id = ${groupId}")
    int removeOneMember(@Param("groupId") String groupId);

    @Select("select * from yim_group where group_id = ${groupId} and owner_uid = ${ownerUid}")
    Group selectGroup(@Param("ownerUid") String ownerUid, @Param("groupId") String groupId);
}