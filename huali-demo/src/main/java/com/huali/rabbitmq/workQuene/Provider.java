package com.huali.rabbitmq.workQuene;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import lombok.SneakyThrows;
import com.huali.rabbitmq.RabbitMQUtil;

/**
 * @author myUser
 * @since 2021-02-14 17:55
 **/
public class Provider {
    @SneakyThrows
    public static void main(String[] args) {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("work", true, false, false, null);
        for (int i = 0; i < 200; i++) {
            channel.basicPublish("", "work", MessageProperties.PERSISTENT_TEXT_PLAIN, (i + "hello workQueue").getBytes());
        }
        RabbitMQUtil.close(channel, connection);
    }
}
