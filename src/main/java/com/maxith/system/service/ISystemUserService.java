package com.maxith.system.service;


import com.maxith.system.entity.SystemUser;

/**
 * 系统用户service
 * Created by zhouyou on 2017/7/4.
 */
public interface ISystemUserService {
    /**
     * 使用用户名查询
     * @param username
     * @return
     */
    SystemUser selectSystemUserByUsername(String username);

    /**
     * 使用用户名和密码查询
     * @param username
     * @param password  密码为加密后的密码
     * @return
     */
    SystemUser selectUserByUsernameAndPassword(String username, String password);

    /**
     * 更新用户登录时间
     * @param gid
     */
    void updateLoginTime(String gid);
}
