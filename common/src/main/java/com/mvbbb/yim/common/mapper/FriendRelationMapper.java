package com.mvbbb.yim.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mvbbb.yim.common.entity.FriendRelation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FriendRelationMapper extends BaseMapper<FriendRelation> {

    @Select("select * from yim_friend_relation where user_id_1 = ${userId} or user_id_2 = ${userId}")
    List<FriendRelation> selectFriendRelation(@Param("userId") String userId);

    @Delete("delete from yim_friend_relation where (user_id_1 = ${userId1} and user_id_2 = ${userId2}) or (user_id_2 = ${userId1} and user_id_1 = ${userId2})")
    int deleteFriendRelation(@Param("userId1") String userId1,@Param("userId2") String userId2);
}
