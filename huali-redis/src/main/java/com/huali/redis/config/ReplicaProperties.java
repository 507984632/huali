package com.huali.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * @author myUser
 * @date 2021-01-23 14:59
 **/
@Data
@Configuration
/**
 *  @ConfigurationProperties
 *  从 application.yml 中读取 spring.redis.replica 下所有的信息 并注入到这个类中
 *  prefix：前缀
 *  ignoreInvalidFields：忽略无效字段
 *  ignoreUnknownFields：忽略未知字段
 *
 *  添加了这个注解的类应该在 SpringBoot 扫描包下
 */
@ConfigurationProperties(prefix = "spring.redis.replica")
public class ReplicaProperties {

    private Boolean enable = false;
    private Set<String> slaves = new HashSet<>();

}