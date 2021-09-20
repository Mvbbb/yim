package com.mvbbb.yim.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mvbbb.yim.common.entity.MsgSend;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MsgSendMapper extends BaseMapper<MsgSend> {

    @Select("select * from yim_msg_send where to_uid = #{userId} and from_uid = #{friendId} order by `timestamp` desc limit ${from},${limit}")
    List<MsgSend> selectHistorySingleMsg(@Param("userId") String userId, @Param("friendId") String friendId, @Param("from") int from, @Param("limit") int limit);

    @Select("select * from yim_msg_send where group_id #{groupId}  order by `timestamp` desc limit ${from},${limit}")
    List<MsgSend> selectHistoryGroupMsg(@Param("groupId") String groupId, @Param("from") int from, @Param("limit") int limit);
}
