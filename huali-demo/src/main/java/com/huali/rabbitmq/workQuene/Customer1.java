package com.huali.rabbitmq.workQuene;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;
import com.huali.rabbitmq.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 工作模型
 *
 * 如果 每次不是拿1消息， 且 自动确认消息消费 这样的话是 多个消费者之间轮询的方式消费消息，
 * 这样的话如果一个消费者需要的时间较长，会造成消息队列中的消息堆积，
 * 所以才采用 每次拿1条消息，消费完成后手动向 rabbitmq 确认消息被消费了，这样不会造成消息堆积
 * 但是每次消费完成消息都需要向 mq 确认消息消费，这样也会造成一定资源的浪费，
 * 看哪一中更适合
 *
 * @author myUser
 * @since 2021-02-14 18:02
 **/
public class Customer1 {
    @SneakyThrows
    public static void main(String[] args) {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();
        // 每次拿取1条消息
        channel.basicQos(1);
        channel.queueDeclare("work", true, false, false, null);

        /**
         * autoAck,是否会自动向 rabbitMQ 确认消息是否消费
         * true 是开启，false 是不会开启 false 之后需要手动的将 消息消费确认
         */
        channel.basicConsume("work", false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("Customer1-接受了消息：" + new String(body));
                // 消息消费需要3秒
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 参数1：确定消息队列中的那1条消息， 参数2：是否开启多个消息同时确实消费
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
