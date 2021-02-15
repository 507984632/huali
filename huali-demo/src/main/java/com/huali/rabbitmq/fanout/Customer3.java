package com.huali.rabbitmq.fanout;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;
import com.huali.rabbitmq.RabbitMQUtil;

import java.io.IOException;

/**
 * @author myUser
 * @since 2021-02-14 19:06
 **/
public class Customer3 {
    @SneakyThrows
    public static void main(String[] args) {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();
        // 通道绑定交换机
        channel.exchangeDeclare("logs", "fanout");

        // 获得临时队列
        String tempQueue = channel.queueDeclare().getQueue();
        // 绑定交换机 和队列
        channel.queueBind(tempQueue, "logs", "");
        //消息消费
        channel.basicConsume(tempQueue, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("Customer3消费者消费消息:" + new String(body));
            }
        });
    }
}
