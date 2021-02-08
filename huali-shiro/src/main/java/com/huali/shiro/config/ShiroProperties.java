package com.huali.shiro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yang_my
 * @date 2021-02-07 18:13
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "huali.shiro")
public class ShiroProperties {

    /**
     * Cookie 的名字
     */
    private String cookieName;

    /**
     * 登录的
     */
    private String loginUrl;

    /**
     * 未授权的
     */
    private String unauthorizedUrl;

    /**
     * 不用授权就能访问的URL 多个以 , 分割
     */
    private String filterChainDefinitions;

    /**
     * 是否存在 无需认证的 请求路径
     */
    private Boolean authcEnable;

}
