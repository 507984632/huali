package com.huali.swagger.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


/**
 * swagger 的配置
 * 默认访问网址：localhost:80/swagger-ui/index.html
 * 如果 访问权限，则需要把
 * <p>\swagger**</p>,
 * <p>\swagger**\**</p>,
 * <p>\*\api-docs</p> 路径打开
 * 以上 所有的 \ 都替换成 / 即可
 * <p>
 * 启动类 和 配置类上 也不需要添加 @EnableSwagger2 注解了
 *
 * @author myUser
 * @since 2021-02-09 17:13
 **/
@Configuration
public class SwaggerConfig {

    @Autowired
    private SwaggerProperties swaggerProperties;

    @Bean
    public Docket platformApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("full-platform").apiInfo(apiInfo2())
                .forCodeGeneration(true);
    }

    private ApiInfo apiInfo2() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle()).description(swaggerProperties.getDescription())
                .contact(new Contact(swaggerProperties.getContactName(), swaggerProperties.getContactUrl(), swaggerProperties.getContactEmail()))
                .version(swaggerProperties.getVersion()).build();
    }
}
