package com.pan.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pan.common.util.UuidUtil;
import com.pan.sys.entity.User;
import com.pan.sys.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.UUID;

@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("授权=================================");
        Object principal = principalCollection.getPrimaryPrincipal();
        log.info(principal.toString());
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("认证=================================");
        String principal = authenticationToken.getPrincipal().toString();
        User user = userService.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, principal));
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), this.getName());
        return authenticationInfo;
    }

    public static final String md5(String password, String salt){
        //加密方式
        String hashAlgorithmName = "MD5";
        //盐：为了即使相同的密码不同的盐加密后的结果也不同
        ByteSource byteSalt = ByteSource.Util.bytes(salt);
        //密码
        Object source = password;
        //加密次数
        int hashIterations = 1024;
        SimpleHash result = new SimpleHash(hashAlgorithmName, source, byteSalt, hashIterations);
        return result.toString();
    }

    public static void main(String[] args) {
        String salt = UuidUtil.getUUID();
        System.out.println(salt);
        System.out.println(md5("123456", salt));
    }
}
