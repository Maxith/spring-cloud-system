package com.maxith.system.controller;

import com.maxith.common.ApplicatonConstants;
import com.maxith.common.entity.BaseComponent;
import com.maxith.common.entity.JsonResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 登录控制器
 * Created by zhouyou on 2017/10/11.
 */
@RestController
@RequestMapping("/login")
public class LoginController extends BaseComponent{

    /**
     * 检查登录
     * @return
     */
    @RequestMapping("/checkLogin")
    public JsonResult checkLogin(){
        JsonResult result = new JsonResult();
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            result.setMsg(ApplicatonConstants.APP_EXPIRE_CODE);
            result.setCode(ApplicatonConstants.APP_EXPIRE_RESULT);
        }
        result.setData(subject.getSession().getAttribute(ApplicatonConstants.SESSION_USER));
        return result;
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/passwordLogin")
    public JsonResult login(String username,String password) {
        JsonResult result = new JsonResult();
        try {
            //设置登录信息
            UsernamePasswordToken token = new UsernamePasswordToken(username,password);

            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
        }catch (Exception ex){
            ex.printStackTrace();
            result.setCode(ApplicatonConstants.APP_ERROR_CODE);
            result.setMsg(ex.getMessage());
        }
        return result;
    }

    /**
     * 登出
     * @return
     */
    @RequestMapping("/logout")
    public JsonResult logout() {
        JsonResult result = new JsonResult();

        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            result.setData(subject.getSession().getAttribute(ApplicatonConstants.SESSION_USER));
            result.setMsg(ApplicatonConstants.APP_EXPIRE_RESULT);
            result.setCode(ApplicatonConstants.APP_EXPIRE_CODE);
        } else {
            subject.logout();
            result.setMsg("用户登出");
        }
        return result;
    }
}
