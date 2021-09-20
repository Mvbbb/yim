package com.mvbbb.yim.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mvbbb.yim.common.entity.MsgRecv;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface MsgRecvMapper extends BaseMapper<MsgRecv> {
    @Update("update yim_msg_recv set delivered = 1 where to_uid = #{userId}")
    void delivered(@Param("userId") String userId);
}
