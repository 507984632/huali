package com.huali.shiro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * 配置 shiro 关于 redis 部分的配置信息
 *
 * @author myUser
 * @date 2021-02-07 20:14
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "huali.shiro.redis")
public class ShiroRedisProperties {
    private Boolean sessionEnable;
    private Boolean replicaEnable;

    private String masterHost;
    private Integer masterPort;
    private String masterPassword;
    private Set<String> slaves = new HashSet<>();
}
