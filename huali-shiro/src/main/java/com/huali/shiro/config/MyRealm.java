package com.huali.shiro.config;

import com.huali.redis.model.User;
import com.huali.redis.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自定义的 Realm 对象
 *
 * @author myUser
 * @date 2021-02-07 18:31
 **/
public class MyRealm extends AuthorizingRealm {

    /**
     * 注入 用户的service
     */
    @Autowired
    private UserService userService;

    /**
     * 授权相关的代码
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 通过 subject 获得当前登录用户
        Subject currentUser = SecurityUtils.getSubject();
        // 在通过当前用户获得 登录认证时，传进来的 user 对象
        User user = (User) currentUser.getPrincipal();
        //TODO  通过user 对象，可以将对象中有的权限赋值给当前用户 这里是不完整的，可以通过用户查询用户所有的权限，并赋值
        info.addStringPermission(user.getName());
        return info;
    }

    /**
     * 认证相关的代码
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 强转成 用户令牌
        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        String username = userToken.getUsername();

        // 判断用户是否存在
        User user = userService.getUserByUsername(username);
        if (user == null) {
            // 这里 返回 null 就会直接抛出 UnknownAccountException
            return null;
        }

        // 密码认证
        return new SimpleAuthenticationInfo(user, user.getPassword(), "");
    }
}
