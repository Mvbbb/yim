package com.mvbbb.yim.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mvbbb.yim.common.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    @Update("update yim_user set password = ${newPassword} where user")
    int changePassword(String userId,String oldPassword,String newPassword);

    @Select("select * from yim_user")
    List<User> selectAllUser();

    @Update("update yim_user set avatar = ${avatar} where user_id = ${userId}")
    int updateAvatar(@Param("userId") String userId, String avatar);


    @Update("update yim_user set username = ${username} where user_id = ${userId}")
    int updateUsername(String userId, @Param("username") String username);

    @Update("update yim_user set password = ${newPassword} where user_id = ${userId} and password = ${oldPassword}")
    int updatePassword(@Param("userId")String userId,@Param("oldPassword")String oldPassword,@Param("newPassword")String newPassword);
}
