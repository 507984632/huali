package com.huali.redis.config;

import io.lettuce.core.ReadFrom;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * @author myUser
 * @date 2021-01-22 0:43
 **/
@Configuration
public class RedisConfig {

    @Autowired
    private RedisProperties redisProperties;
    @Autowired
    private ReplicaProperties replicaProperties;


    @Bean("keySerializer")
    public StringRedisSerializer keySerializer() {
        return (StringRedisSerializer) RedisSerializer.string();
    }

    @Bean("valueSerializer")
    public GenericJackson2JsonRedisSerializer valueSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        if (replicaProperties.getEnable()) {
            LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
                    .readFrom(ReadFrom.REPLICA_PREFERRED).build();

            RedisStaticMasterReplicaConfiguration configuration = new RedisStaticMasterReplicaConfiguration(redisProperties.getHost(), redisProperties.getPort());
            configuration.setDatabase(redisProperties.getDatabase());
            configuration.setPassword(redisProperties.getPassword());
            for (String slave : replicaProperties.getSlaves()) {
                String[] hostPort = slave.split(":");
                configuration.addNode(hostPort[0], Integer.parseInt(hostPort[1]));
            }

            return new LettuceConnectionFactory(configuration, clientConfiguration);
        } else {
            RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
            configuration.setDatabase(redisProperties.getDatabase());
            configuration.setHostName(redisProperties.getHost());
            configuration.setPort(redisProperties.getPort());
            configuration.setPassword(redisProperties.getPassword());

            RedisProperties.Pool pool = redisProperties.getLettuce().getPool();
            LettucePoolingClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.defaultConfiguration();
            GenericObjectPoolConfig poolConfig = clientConfiguration.getPoolConfig();
            poolConfig.setMaxIdle(pool.getMaxIdle());
            poolConfig.setMaxTotal(pool.getMaxActive());
            poolConfig.setMinIdle(pool.getMinIdle());

            return new LettuceConnectionFactory(configuration, clientConfiguration);
        }
    }

    @Bean
    public RedisTemplate redisTemplate(
            LettuceConnectionFactory lettuceConnectionFactory,
            StringRedisSerializer keySerializer, GenericJackson2JsonRedisSerializer valueSerializer) {

        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        return redisTemplate;
    }

}
