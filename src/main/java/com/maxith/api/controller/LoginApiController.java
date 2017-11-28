package com.maxith.api.controller;

import com.maxith.common.entity.BaseComponent;
import com.maxith.system.entity.SystemUser;
import com.maxith.system.service.ISystemUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 登录api 控制器
 * Created by zhouyou on 2017/10/17.
 */
@RestController
@RequestMapping("/api/login")
public class LoginApiController extends BaseComponent{

    @Resource
    private ISystemUserService iSystemUserService;

    /**
     * 按账号名,密码查询
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/selectUserByUsernameAndPassword")
    public SystemUser selectUserByUsernameAndPassword(String username, String password){
        return iSystemUserService.selectUserByUsernameAndPassword(username,password);
    }

    /**
     * 按用户名查询
     * @param username
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/selectSystemUserByUsername")
    public SystemUser selectSystemUserByUsername(String username){
        return iSystemUserService.selectSystemUserByUsername(username);
    }

    /**
     * 更新登录时间
     * @param gid
     */
    @RequestMapping(method = RequestMethod.POST,value = "/updateLoginTime")
    public void updateLoginTime(String gid){
        iSystemUserService.updateLoginTime(gid);
    }
}
