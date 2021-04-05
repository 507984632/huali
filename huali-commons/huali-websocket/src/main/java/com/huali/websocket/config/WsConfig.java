package com.huali.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * springboot 整合 WebSocket配置类
 * <p>
 *
 * @author Yang_my
 * @see com.huali.websocket.template 这个报下存在这模板，只需要将两个注解打开，copy 到项目中即可使用
 * @since 2021/3/6
 */
@Configuration
public class WsConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
