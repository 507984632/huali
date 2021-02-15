package com.huali.rabbitmq.direct;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;
import com.huali.rabbitmq.RabbitMQUtil;

import java.io.IOException;

/**
 * @author myUser
 * @since 2021-02-14 20:40
 **/
public class Customer1 {
    @SneakyThrows
    public static void main(String[] args) {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();
        // 声明交换机
        channel.exchangeDeclare("logs_direct", "direct");
        // 获得临时队列
        String tempQueue = channel.queueDeclare().getQueue();
        // 基于 route key 绑定交换机和队列
        channel.queueBind(tempQueue, "logs_direct", "error");

        channel.basicConsume(tempQueue, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者1：" + new String(body));
            }
        });
    }
}
