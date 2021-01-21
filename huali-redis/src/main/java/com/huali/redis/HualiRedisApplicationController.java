package com.huali.redis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author myUser
 * @date 2021-01-20 19:32
 **/
@SpringBootApplication
@MapperScan(basePackages = "com.huali.redis.mapper")
public class HualiRedisApplicationController {
    // 启动类x`
    public static void main(String[] args) {
        SpringApplication.run(HualiRedisApplicationController.class);
    }
}
