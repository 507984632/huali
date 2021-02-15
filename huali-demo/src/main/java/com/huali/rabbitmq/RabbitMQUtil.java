package com.huali.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;

/**
 * @author myUser
 * @since 2021-02-13 19:34
 **/
public class RabbitMQUtil {
    // 链接mq的链接工厂对象
    private static ConnectionFactory connectionFactory;

    static {
        // 创建链接mq的链接工厂对象 并赋值
        connectionFactory = new ConnectionFactory();
        // rabbitmq 的 ip + port
        connectionFactory.setHost("192.168.2.13");
        connectionFactory.setPort(5672);
        // 虚拟主机的名称
        connectionFactory.setVirtualHost("ems");
        // 用户名 密码
        connectionFactory.setUsername("test");
        connectionFactory.setPassword("1");
    }

    @SneakyThrows
    public static Connection getConnection() {
        // 通过 链接工厂 创建新的链接
        return connectionFactory.newConnection();
    }

    @SneakyThrows
    public static void close(Channel channel, Connection connection) {
        // 通道
        if (channel != null) {
            channel.close();
        }
        // 关闭链接
        if (connection != null) {
            connection.close();
        }
    }

}
