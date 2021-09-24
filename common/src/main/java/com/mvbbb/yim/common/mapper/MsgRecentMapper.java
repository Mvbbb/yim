package com.mvbbb.yim.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mvbbb.yim.common.entity.MsgRecent;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MsgRecentMapper extends BaseMapper<MsgRecent> {

    @Delete("delete from yim_msg_recent where ( to_uid = #{userId1} and from_uid = #{userId2} ) or ( to_uid = #{userId2} and from_uid = #{userId1} ) and group_id is NULL")
    int deleteSingleChatOldMsg(@Param("userId1") String userId1, @Param("userId2") String userId2);

    @Select("select * from yim_msg_recent where to_uid = #{userId} or from_uid = #{userId}")
    List<MsgRecent> selectUserRecentMsg(@Param("userId") String userId);

    @Delete("delete from yim_msg_recent where group_id = #{groupId} and to_uid = #{toUid}")
    int deleteGroupChatOldMsg(@Param("groupId") String groupId,@Param("toUid")String toUid);
}
