package com.mvbbb.yim.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mvbbb.yim.common.entity.UserGroupRelation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserGroupRelationMapper extends BaseMapper<UserGroupRelation> {
    @Select("select * from  yim_user_group_relation where user_id = #{userId} and group_id = #{groupId}")
    UserGroupRelation findGroupRelation(@Param("userId") String userId, @Param("groupId") String groupId);

    @Delete("delete from yim_user_group_relation where user_id = #{userId} and group_id = #{groupId}")
    int deleteUserGroupRelation(@Param("userId") String userId,@Param("groupId") String groupId);

    @Select("select user_id from yim_user_group_relation where user_id = #{userId}")
    List<String> findUserGroupIds(@Param("userId") String userId);
}
