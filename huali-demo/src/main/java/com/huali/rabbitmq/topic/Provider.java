package com.huali.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.SneakyThrows;
import com.huali.rabbitmq.RabbitMQUtil;

/**
 * 动态路由(动态订阅者模型)
 */
public class Provider {
    @SneakyThrows
    public static void main(String[] args) {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明交换机
        channel.exchangeDeclare("topics", "topic");

        // 发布消息
//        String key = "user.save";
//        String key = "user.save.aasdfa";
//        String key = "user.aasdfa.save";
        String key = "save.user";
//        String key = "asdf.save";
        channel.basicPublish("topics", key, null, ("key:" + key + ",动态订阅").getBytes());
        RabbitMQUtil.close(channel, connection);
    }
}
