package com.huali.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * @author myUser
 * @since 2021-02-09 17:16
 **/
@Data
@Configuration
@ConfigurationProperties("huali.swagger")
public class SwaggerProperties {
    /**
     * 项目的大标题
     */
    private String title;
    /**
     * 项目的描述信息
     */
    private String description;
    /**
     * 项目的版本信息
     */
    private String version;
    /**
     * 联系人的姓名
     */
    private String contactName;
    /**
     * 联系网址
     */
    private String contactUrl;
    /**
     * 联系邮箱
     */
    private String contactEmail;
}
