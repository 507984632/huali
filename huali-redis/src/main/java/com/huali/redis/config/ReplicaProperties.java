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
@ConfigurationProperties(prefix = "spring.redis.replica")
public class ReplicaProperties {

    private Boolean enable = false;
    private Set<String> slaves = new HashSet<>();

}