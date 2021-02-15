package com.huali.rabbitmq.workQuene;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;
import com.huali.rabbitmq.RabbitMQUtil;

/**
 * @author myUser
 * @since 2021-02-14 18:02
 **/
public class Customer2 {
    @SneakyThrows
    public static void main(String[] args) {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.basicQos(1);
        channel.queueDeclare("work", true, false, false, null);
        channel.basicConsume("work", false, new DefaultConsumer(channel) {
            @SneakyThrows
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                System.out.println("Customer2-接受了消息：" + new String(body));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });

    }
}
