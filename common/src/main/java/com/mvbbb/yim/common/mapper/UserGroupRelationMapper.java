package com.mvbbb.yim.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mvbbb.yim.common.entity.UserGroupRelation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserGroupRelationMapper extends BaseMapper<UserGroupRelation> {
    @Select("select * from  yim_user_group_relation where user_id = ${userId} and groupId = ${groupId}")
    UserGroupRelation findGroupRelation(@Param("userId") String userId,@Param("groupId") String groupId);
}
