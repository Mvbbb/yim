package com.mvbbb.yim.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mvbbb.yim.common.entity.MsgRecent;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface MsgRecentMapper extends BaseMapper<MsgRecent> {

    int deleteSingleChatOldMsg(@Param("userId1") String userId1, @Param("userId2") String userId2);

    @Select("select * from yim_msg_recent where user_id = #{userId}")
    List<MsgRecent> selectUserRecentMsg(@Param("userId") String userId);

    int deleteGroupChatOldMsg(@Param("groupId") String groupId, @Param("toUid") String toUid);

    @Select("select * from yim_msg_recent where user_id = #{userId} and session_uid = #{sessionUid}")
    MsgRecent selectUserSingleRecentMsg(@Param("userId") String userId, @Param("sessionUid") String sessionUid);

    @Update("update yim_msg_recent set content = #{content} where ( user_id = #{userId1} and session_uid = #{userId2} ) or ( user_id = #{userId2} and session_uid = #{userId1} )")
    int updateSingleChatContent(@Param("userId1") String userId1, @Param("userId2") String userId2, @Param("content") String content);

    @Select("select * from yim_msg_recent where user_id = #{userId} and session_group_id = #{groupId}")
    MsgRecent selectUserGroupRecentMsg(@Param("userId") String userId, @Param("groupId") String groupId);

    @Update("update yim_msg_recent set content = #{content} where session_group_id = #{groupId}")
    int updateGroupChantContent(@Param("groupId") String groupId, @Param("content") String content);
}
