package com.huali.rabbitmq.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.SneakyThrows;
import com.huali.rabbitmq.RabbitMQUtil;

/**
 * 路由（订阅者模型）
 */
public class Provider {
    @SneakyThrows
    public static void main(String[] args) {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();
        // 创建 路由的交换机
        channel.exchangeDeclare("logs_direct", "direct");
        // 发送消息
//        String routingKey = "info";
//        String routingKey = "error";
        String routingKey = "debug";
        channel.basicPublish("logs_direct", routingKey,
                null, ("这是 direct 模型基于 routingKey：[" + routingKey + "]").getBytes());
        RabbitMQUtil.close(channel, connection);
    }
}
