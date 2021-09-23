package com.mvbbb.yim.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mvbbb.yim.common.entity.MsgRecent;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MsgRecentMapper extends BaseMapper<MsgRecent> {

    @Delete("delete from yim_msg_recent where to_uid = #{toUid} and group_id = #{groupId} and from_uid = #{fromUid}")
    int deleteSessionOldMsg(@Param("fromUid") String fromUid, @Param("toUid") String toUid, @Param("groupId") String groupId);

    @Select("select * from yim_msg_recent where to_uid = #{userId} or from_uid = #{userId}")
    List<MsgRecent> selectUserRecentMsg(@Param("userId") String userId);
}
