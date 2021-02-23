package com.huali.elasticsearch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author myUser
 * @since 2021-02-22 12:41
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "huali.elasticsearch")
public class ElasticsearchProperties {

    /**
     *  是否开启集群
     */
    private Boolean enable;
    /**
     * ip + port
     */
    private List<String> hosts;

}
