package com.huali.elasticsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author myUser
 * @since 2021-02-22 12:35
 **/
@Configuration
public class ElasticsearchConfig {

    @Autowired
    private ElasticsearchProperties elasticsearchProperties;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        // 配置文件中elasticsearch hosts 的个数
        int size = elasticsearchProperties.getHosts().size();
        if (size == 0) {
            throw new RuntimeException("Elasticsearch Hosts is not null");
        }
        // 实际用于数组存储
        HttpHost[] hosts = new HttpHost[size];

        // 是否开启集群
        if (elasticsearchProperties.getEnable()) {
            for (int i = 0; i < size; i++) {
                getHosts(hosts, i);
            }
        } else {
            getHosts(hosts, 0);
        }

        return new RestHighLevelClient(RestClient.builder(hosts));
    }

    /**
     * 将hosts 内容补全
     *
     * @param hosts 用于存放 elasticsearch 的 host
     * @param i     第几个
     */
    private void getHosts(HttpHost[] hosts, Integer i) {
        String host = elasticsearchProperties.getHosts().get(i);
        String[] split = host.split(":");
        if (split.length < 2) {
            throw new RuntimeException("Elasticsearch IP or Port is not null");
        }

        // 单个节点的ip
        String address = split[0];
        // 单个节点的port
        int port = Integer.parseInt(split[1]);
        if (StringUtils.isEmpty(address)) {
            throw new RuntimeException("Elasticsearch IP or Port is not null");
        }
        hosts[i] = new HttpHost(address, port, "http");
    }
}
