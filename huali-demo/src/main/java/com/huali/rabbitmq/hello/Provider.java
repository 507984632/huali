package com.huali.rabbitmq.hello;

import com.huali.rabbitmq.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import lombok.SneakyThrows;

/**
 * @author myUser
 * @since 2021-02-13 14:45
 **/
public class Provider {

    @SneakyThrows
    public static void main(String[] args) {
        // 获得链接对象
        Connection connection = RabbitMQUtil.getConnection();
        // 通过链接对象获得链接通道
        Channel channel = connection.createChannel();
        /** 通道绑定消息队列
         *  1. queue：消息队列的名称，没有该消息队列会自动创建
         *  2. durable：用来定义队列是否要持久化
         *  3. exclusive：是否独占队列，意思是是否只能当前的 connection 能用
         *  4. autoDelete：是否在消费完成后删除队列
         *  5. arguments：附加参数
         */
        channel.queueDeclare("hello", true, false, false, null);

        /** 发送消息
         * 1. exchange：交换机名称
         * 2. routingKey：队列名称
         * 3. props：额外参数
         * 3.1 MessageProperties.PERSISTENT_TEXT_PLAIN：让消息队列中的消息持久化
         * 4. body：具体消息内容 (需要的是byte[] 的数据)
         */
        channel.basicPublish("", "hello", MessageProperties.PERSISTENT_TEXT_PLAIN, "hello rabbitmq4".getBytes());

        // 关闭链接
        RabbitMQUtil.close(channel, connection);
        System.out.println("完成！");
    }

}
