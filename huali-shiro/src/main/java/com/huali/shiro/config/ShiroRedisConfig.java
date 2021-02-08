package com.huali.shiro.config;

import com.huali.shiro.RedisSessionDAO;
import com.huali.shiro.ShiroRedisCacheManager;
import io.lettuce.core.ReadFrom;
import org.apache.shiro.session.mgt.SimpleSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * @author myUser
 * @date 2021-02-07 20:14
 **/
@Configuration
@ConfigurationProperties("huali.shiro.redis.session-enable")
public class ShiroRedisConfig {

    @Autowired
    private ServerProperties serverProperties;
    /**
     * 注入 shiro 配置 redis 的信息
     */
    @Autowired
    private ShiroRedisProperties shiroRedisProperties;

    public int expire() {
        long seconds = serverProperties.getServlet().getSession().getTimeout().getSeconds();
        return (int) seconds;
    }

    @Bean
    public RedisTemplate<String, Serializable> redisManager(LettuceConnectionFactory lettuceConnectionFactory,
                                                            StringRedisSerializer keySerializer) {
        if (shiroRedisProperties.getReplicaEnable()) {
            LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
                    .readFrom(ReadFrom.REPLICA_PREFERRED).build();

            RedisStaticMasterReplicaConfiguration configuration = new RedisStaticMasterReplicaConfiguration(
                    shiroRedisProperties.getMasterHost(), shiroRedisProperties.getMasterPort());
            configuration.setPassword(shiroRedisProperties.getMasterPassword());
            for (String slave : shiroRedisProperties.getSlaves()) {
                String[] hostPort = slave.split(":");
                configuration.addNode(hostPort[0], Integer.parseInt(hostPort[1]));
            }

            lettuceConnectionFactory = new LettuceConnectionFactory(configuration, clientConfiguration);
        }

        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(RedisSerializer.java(SimpleSession.class.getClassLoader()));
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO(RedisTemplate<String, Serializable> redisManager) {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager);
        redisSessionDAO.setExpire(expire());
        redisSessionDAO.setKeyPrefix(serverProperties.getServlet().getApplicationDisplayName() +
                ":" + redisSessionDAO.getKeyPrefix());
        return redisSessionDAO;
    }

    @Bean
    public ShiroRedisCacheManager shiroCacheManager(RedisTemplate<String, Serializable> redisManager) {
        ShiroRedisCacheManager shiroRedisCacheManager = new ShiroRedisCacheManager();
        shiroRedisCacheManager.setRedisManager(redisManager);
        shiroRedisCacheManager.setExpire(expire());
        shiroRedisCacheManager.setKeyPrefix(serverProperties.getServlet().getApplicationDisplayName()
                + ":" + shiroRedisCacheManager.getKeyPrefix());
        return shiroRedisCacheManager;
    }
}
