package com.huali.cloud.nacos.config.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


/**
 * Nacos 中关于 远程调用，负载均衡的配置
 *
 * @author Yang_my
 * @since 2021/3/8
 */
@Configuration
public class ApplicationConfig {
    /**
     * -@LoadBalanced：OpenFen 中关于负载均衡 的配置
     *
     * @return .
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}