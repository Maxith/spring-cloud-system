package com.maxith.config.shiro;

import com.maxith.common.ApplicatonConstants;
import com.maxith.common.tools.CryptUtils;
import com.maxith.common.tools.WebRequestUtils;
import com.maxith.system.entity.SystemUser;
import com.maxith.system.service.ISystemUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 自定义shiro验证器
 * Created by zhouyou on 2017/10/11.
 */
@Configuration
public class MyShiroRealmConfig extends AuthorizingRealm{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ISystemUserService iSystemUserService;

    public MyShiroRealmConfig() {
        this.setCredentialsMatcher(new SimpleCredentialsMatcher());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //null usernames are invalid
        if (principalCollection == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        String username = (String) getAvailablePrincipal(principalCollection);

        SystemUser user = iSystemUserService.selectSystemUserByUsername(username);
        if (user == null){
            final String message = "没找到用户名为 [" + username + "] 的用户!";
            if (logger.isErrorEnabled()) {
                logger.error(message);
            }

            // Rethrow any SQL errors as an authorization exception
            throw new AuthorizationException(message);
        }
        //通过用户查询权限
        List<String> permissions = new ArrayList<>();
        permissions.add("admin");

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;

        String username = upToken.getUsername();
        String password = new String(upToken.getPassword());
        password = CryptUtils.generateDigestWithMD5(password);

        SystemUser user = iSystemUserService.selectUserByUsernameAndPassword(username,password);

        if (user == null) {
            WebRequestUtils.setAttribute4Request(ApplicatonConstants.LOGIN_ERROR,
                    ApplicatonConstants.LOGIN_ERRER_MSG);
            throw new AuthenticationException(ApplicatonConstants.LOGIN_ERRER_MSG);
        }
        //将用户放入session
        SystemUser tempUser = WebRequestUtils.getAttributeFromSession(SystemUser.class,ApplicatonConstants.SESSION_USER);
        if (tempUser == null || user.getGid().equals(tempUser.getGid())){
            WebRequestUtils.setAttribute4Session(ApplicatonConstants.SESSION_USER,user);
        }

        iSystemUserService.updateLoginTime(user.getGid());
        return new SimpleAuthenticationInfo(user, password, this.getName());
    }
}
