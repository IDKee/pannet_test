package com.pan.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSentinelManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    //不需要在此处配置权限页面,因为上面的ShiroFilterFactoryBean已经配置过,但是此处必须存在,因为shiro-spring-boot-web-starter或查找此Bean,没有会报错
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
        filterChainDefinitionMap.put("/static/**", "anon");
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
        webSecurityManager.setCacheManager(redisCacheManager());
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
     * Session Manager
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager() {

        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        redisCacheManager.setExpire(3000);
        //指定存入Redis的主键
        redisCacheManager.setPrincipalIdFieldName("id");
        return redisCacheManager;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     */
    @Bean
    @ConfigurationProperties("spring.redis.sentinel")
    public RedisSentinelManager redisManager() {
        RedisSentinelManager redisManager = new RedisSentinelManager();
        redisManager.setDatabase(1);
        return redisManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        //配置session失效时间单位秒
        redisSessionDAO.setExpire(3000);
        return redisSessionDAO;
    }



}

