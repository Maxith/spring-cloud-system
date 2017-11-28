package com.maxith.config.handler;


import com.maxith.common.ApplicatonConstants;
import com.maxith.common.entity.JsonResult;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理handler
 * Created by zhouyou on 2017/6/1.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger;

    public GlobalExceptionHandler() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * shiro 未授权访问
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public JsonResult shiroUnauthenticatedException(Exception ex) {
        //打印错误
        ex.printStackTrace();
        logger.error(ex.getMessage(), ex);

        JsonResult result = new JsonResult();
        result.setCode(ApplicatonConstants.APP_UNAUTHORIZED_CODE);
        result.setMsg(ApplicatonConstants.APP_UNAUTHORIZED_RESULT);
        return result;
    }

    /**
     * 登录失败
     *
     * @param request
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler({AuthenticationException.class})
    public JsonResult shiroLoginErrerException(HttpServletRequest request, AuthenticationException ex) {
        //打印错误
        logger.error("登录错误!用户名:" + request.getParameter("username") + " ;密码: " + request.getParameter("password"));

        JsonResult result = new JsonResult();
        result.setCode(ApplicatonConstants.APP_ERROR_CODE);
        result.setMsg(ApplicatonConstants.LOGIN_SUCCESS_MSG);
        return result;
    }

}
