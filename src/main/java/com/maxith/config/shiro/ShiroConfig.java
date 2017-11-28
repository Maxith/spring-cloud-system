package com.maxith.config.shiro;


import com.maxith.common.ApplicatonConstants;
import com.maxith.config.redis.shiro.ShiroRedisCacheManager;
import com.maxith.config.redis.shiro.ShiroRedisSessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;

import javax.annotation.Resource;
import java.util.LinkedHashMap;

/**
 * shiro安全配置
 * Created by zhouyou on 2017/5/11.
 */
@Configuration
public class ShiroConfig implements EnvironmentAware {

    private Environment env;

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    /**
     * Shiro 的核心Web过滤器
     * @param manager
     * @return
     */
    @Bean(name = "shiroFilter")
    public MyShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager manager) {
        MyShiroFilterFactoryBean bean = new MyShiroFilterFactoryBean();
        bean.setSecurityManager(manager);
        //配置登录的url和登录成功的url
        bean.setLoginUrl("/login/toLogin");

        //配置访问权限
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        filterChainDefinitionMap.put("/api/**", "anon");//内网API

        filterChainDefinitionMap.put("/login/toLogin", "anon");//可以匿名访问
        filterChainDefinitionMap.put("/login/passwordLogin", "anon");//可以匿名访问

        filterChainDefinitionMap.put("/login/logout", "logout"); //退出拦截
        filterChainDefinitionMap.put("/login/logout", "anon");

        filterChainDefinitionMap.put("/**", "authc");//表示需要认证才可以访问

        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    /**
     * 配置核心安全事务管理器
     * @param myShiroRealmConfig
     * @param shiroRedisCacheManager
     * @param shiroSessionManagerConfig
     * @return
     */
    @Bean(name = "securityManager")
    public SecurityManager securityManager(@Qualifier("myShiroRealmConfig") MyShiroRealmConfig myShiroRealmConfig,
                                           @Qualifier("shiroRedisCacheManager") ShiroRedisCacheManager shiroRedisCacheManager,
                                           @Qualifier("myShiroSessionManagerConfig") DefaultWebSessionManager shiroSessionManagerConfig) {
        System.err.println("--------------shiro已经加载----------------");
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();

        //自定义域管理器
        manager.setRealm(myShiroRealmConfig);

        //缓存管理器
        manager.setCacheManager(shiroRedisCacheManager);

        //session管理器
        manager.setSessionManager(shiroSessionManagerConfig);

        return manager;
    }

    @Bean("myShiroSessionManagerConfig")
    public DefaultWebSessionManager myShiroSessionManagerConfig(@Qualifier("shiroRedisSessionDAO") ShiroRedisSessionDAO myRedisSessionDAO) {
        DefaultWebSessionManager manager = new DefaultWebSessionManager();

        manager.setSessionDAO(myRedisSessionDAO);
        //设置cookie
        Cookie cookie = new SimpleCookie();
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setName("myjsid");
        cookie.setMaxAge(Integer.parseInt(env.getProperty(ApplicatonConstants.REDIS_TIME_OUT)));
        manager.setSessionIdCookie(cookie);

        //session过期时间
        long time = Long.parseLong(env.getProperty(ApplicatonConstants.REDIS_TIME_OUT));
        manager.setGlobalSessionTimeout(time);
        return manager;
    }


    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 支持Shiro对Controller的方法级AOP安全控制
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager")SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        System.out.println("--------------------shiro aop注解开启----------------");
        return authorizationAttributeSourceAdvisor;
    }
}
