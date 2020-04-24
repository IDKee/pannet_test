package com.pan.common.config;

import com.pan.common.constants.ServiceConstants;
import com.pan.common.redis.RedisCacheManager;
import com.pan.common.redis.RedisSessionDAO;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    /**
     * 不需要在此处配置权限页面,因为上面的ShiroFilterFactoryBean已经配置过,
     * 是此处必须存在,因为shiro-spring-boot-web-starter或查找此Bean,没有会报错
     * @return
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        return new DefaultShiroFilterChainDefinition();
    }

    /**
     * 配置shiroFilter过滤器
     *
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        //权限配置
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 配置不会被拦截的链接 顺序判断，因为前端模板采用了thymeleaf，这里不能直接使用 ("/static/**", "anon")来配置匿名访问，必须配置到每个静态目录
        filterChainDefinitionMap.put("/plugins/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/logout", "logout");
        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/**", "authc");
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 配置securityManager 安全管理器
     *
     * @return
     */
    @Bean
    public SessionsSecurityManager securityManager() {
        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();
        //配置认证器
        webSecurityManager.setRealm(userRealm());
        webSecurityManager.setSessionManager(sessionManager());
        return webSecurityManager;
    }

    /**
     * 配置自定义认证器
     *
     * @return
     */
    @Bean
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }

    /**
     * 配置加密方式
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        //盐
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //配置散列算法，，使用MD5加密算法
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //设置散列次数
        hashedCredentialsMatcher.setHashIterations(1024);
        return hashedCredentialsMatcher;
    }


    /**
     * redis缓存管理
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager() {
        return new RedisCacheManager();
    }

    @Bean
    public RedisSessionDAO getRedisSessionDao() {
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        return sessionDAO;
    }

    /**
     * shiro的session管理
     * 从配置文件注入globalSessionTimeout属性
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix="shiro")
    public SessionManager sessionManager() {
        //因为@Bean执行比@Value快，为了先注入@Value，只能把@Value作为函数的参数声明了
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //自定义redis的sessionDao
        sessionManager.setSessionDAO(getRedisSessionDao());
        //sessionManager.setGlobalSessionTimeout(getSessionTimeout(1800)*1000);//设置全局session超时时间 ms
        //sessionManager.setCacheManager(redisCacheManager());
        //启用自定义的SessionIdCookie
        sessionManager.setSessionIdCookieEnabled(true);
        //自定义SessionIdCookie
        sessionManager.setSessionIdCookie(sessionIdCookie());
        //关闭URL中带上JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        //定时检查失效的session
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //启用删除无效sessioin
        sessionManager.setDeleteInvalidSessions(true);
        return sessionManager;
    }

    /**
     * sessionIdCookie的实现,用于重写覆盖容器默认的JSESSIONID
     * @return
     */
    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        //cookie的name,对应的默认是 JSESSIONID
        simpleCookie.setName(ServiceConstants.SHIRO_SESSION_COOKIES);
        //设置浏览器关闭才删除cookie
        simpleCookie.setMaxAge(-1);
        simpleCookie.setPath("/");
        //只支持http
        simpleCookie.setHttpOnly(true);
        return simpleCookie;
    }


}

