package com.huali.shiro.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 模拟调用框架中重写的这个类
 * 需要添加 @Component 注解 且 继承 AuthorizingRealm 类
 *
 * @author myUser
 * @date 2021-02-07 18:31
 **/
//@Component
public class Realm extends AuthorizingRealm {

    /**
     * 需要自动注入两个 service ，1. 用户 2. 用户权限，
     * 或者1个，用户中自己调用 用户权限，然后封装到用户中
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
        // 将用户所有的权限赋值进 shiro 中
        info.addStringPermissions(user.getAuthoritys());
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
        User user = userService.getUserByUserName(username);
        if (user == null) {
            // 这里 返回 null 就会直接抛出 UnknownAccountException
            return null;
        }

        // 密码认证
        return new SimpleAuthenticationInfo(user, user.getPassword(), "");
    }
}

class UserService {
    /**
     * 这里应该 调用数据库中获取，不过本示例之作后续步骤，不做数据库交互
     *
     * @param username 用户名
     * @return 根用户有关的所有数据
     */
    public User getUserByUserName(String username) {
        return new User(1L, "root", "123", Collections.singletonList("[学校表:读取]"));
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class User {
    Long id;
    String username;
    String password;
    /**
     * 用户权限
     */
    List<String> authoritys;
}
