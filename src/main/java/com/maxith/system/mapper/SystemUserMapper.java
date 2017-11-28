package com.maxith.system.mapper;


import com.maxith.system.entity.SystemUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SystemUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemUser record);

    int insertSelective(SystemUser record);

    SystemUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemUser record);

    int updateByPrimaryKey(SystemUser record);

    List<SystemUser> selectSystemUserByUsername(String username);

    List<SystemUser> selectUserByUsernameAndPassword(@Param(value = "username") String username, @Param(value = "password") String password);

    void updateLoginTime(String gid);
}