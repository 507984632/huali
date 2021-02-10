package com.huali.shiro.config;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Yang_my
 * @date 2021-02-07 18:00
 **/
@Configuration
public class ShiroConfig {

    /**
     * 无需认证即可访问
     */
    public static final String ANON = "anon";

    /**
     * 必须认证了才能访问
     */
    public static final String AUTHC = "authc";

    /**
     * 必须拥有 记住我 功能才能访问
     */
    public static final String USER = "user";

    /**
     * 拥有对某个资源的权限才能访问
     */
    public static final String PERMS = "perms";

    /**
     * 拥有某个角色权限才能访问
     */
    public static final String ROLE = "role";

    /**
     * 读取的 application 中关于 shiro 的配置信息
     */
    @Autowired
    private ShiroProperties shiroProperties;

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启 aop 注解支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * shiro 关于过滤 那些路径的方法
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(shiroProperties.getLoginUrl());
        shiroFilterFactoryBean.setUnauthorizedUrl(shiroProperties.getUnauthorizedUrl());

        /*
         * 注意点：如果用 LinkedHashMap 来存储 接口
         *---------如果 /** 这种在 第一次 put 进去的话 所有的 几口都是 /** 的权限，这是一个注意点
         *---------所以，都是在最后一次 将 /** 且是登录权限的放进去 这样就可以定制化的将一些接口开放出来
         */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        if (shiroProperties.getAuthcEnable()) {
            String[] anons = shiroProperties.getFilterChainDefinitions().split(",");
            for (String anon : anons) {
                filterChainDefinitionMap.put(anon, ANON);
            }
            filterChainDefinitionMap.put("/**", AUTHC);
        }

        // 将 所有的 url + 接口的权限 map 赋值进 Shiro 中
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 获得安全管理器
     */
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("sessionManager") SessionManager sessionManager,
                                                               @Qualifier("realm") Realm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 配置 session 信息
        securityManager.setSessionManager(sessionManager);
        // 配置 realm 信息
        securityManager.setRealm(realm);
        return securityManager;
    }

    /**
     * 获得 Realm 对象
     * 可以自定义这个对象， 需要继承 org.apache.shiro.realm.AuthorizingRealm 对象
     * 然后这里就 new 自定义对象即可
     * <p>
     * 添加 @ConditionalOnMissingBean 注解就是当这个是一个备用的类，
     * ---如果自己也创建了一个 Realm 的类继承 AuthorizingRealm 后重写方法，然后添加上
     * ---@Component 注解后，就可以直接注入到这里或者说直接注入你自己定义的 Realm Bean
     */
    @Bean
    @ConditionalOnMissingBean
    public Realm realm() {
        return new SimpleAccountRealm();
    }

    /**
     * 自定义的 Realm 对象
     */
//    @Bean(name = "realm")
//    public Realm realm() {
//        return new Realm();
//    }
}
