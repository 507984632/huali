package com.huali.shiro.config;

import com.huali.shiro.util.LoginUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        return LoginUtil.getCurrentUserAuthInfo();
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

        // 将用户的 id 放入 loginUtil 中
        LoginUtil.saveCurrentUser(user.getId());
        LoginUtil.saveCurrentUserAccount(user.getUsername());

        // 简单授权对象
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 保证权限的唯一性
        Set<String> permissions = new HashSet<>();
        // 获得用户的所有 授权信息
        List<String> authoritys = user.getAuthoritys();
        // 检验授权信息的唯一性
        permissions.addAll(authoritys);
        // 将授权信息放入 简单授权对象中
        info.addStringPermissions(permissions);
        // 然后将这个简单授权对象放入 LoginUtil 中
        LoginUtil.saveCurrentUserAuthInfo(info);

        // shiro 的密码验证     参数是    存储在shiro中的数据，用户的密码， 用户的账号
        return new SimpleAuthenticationInfo(user, user.getPassword(), user.getUsername());
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
