package com.huali.rabbitmq.topic;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;
import com.huali.rabbitmq.RabbitMQUtil;

import java.io.IOException;

/**
 * @author myUser
 * @since 2021-02-14 21:03
 **/
public class Customer2 {
    @SneakyThrows
    public static void main(String[] args) {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        // 通道绑定交换机
        channel.exchangeDeclare("topics", "topic");
        // 获得临时队列
        String tempQueue = channel.queueDeclare().getQueue();
        // 通道绑定 队列和交换机， 和 key
        channel.queueBind(tempQueue, "topics", "user.*.save");
        // 消费消息
        channel.basicConsume(tempQueue, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者2:" + new String(body));
            }
        });
    }
}
