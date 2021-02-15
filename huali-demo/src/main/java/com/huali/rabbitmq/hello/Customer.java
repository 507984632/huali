package com.huali.rabbitmq.hello;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;
import com.huali.rabbitmq.RabbitMQUtil;

/**
 * @author myUser
 * @since 2021-02-13 15:09
 **/
public class Customer {

    @SneakyThrows
    public static void main(String[] args) {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("hello", false, false, false, null);

        /** 消费消息
         * 1. queue：消息队列名称
         * 2. autoAck：开始消息的自动确认机制
         * 3. callback：消费时的回调接口
         */
        channel.basicConsume("hello", true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                System.out.println("接受了消息：" + new String(body));
            }
        });
        // 这里不关闭可以达到一直监听的效果
    }
}
