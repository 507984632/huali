package com.huali.rabbitmq.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.SneakyThrows;
import com.huali.rabbitmq.RabbitMQUtil;

/**
 * 广播模型
 * 广播就是在同一个广播的交换机中的所有队列，都会消费消息
 *
 * @author myUser
 * @since 2021-02-14 18:50
 **/
public class Provider {
    @SneakyThrows
    public static void main(String[] args) {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        /**交换机 如果该交换机不存在，则会创建
         * exchange：交换机的名称
         * type： 交换机的类型
         *      fanout：广播的交换机， 这里如果想用广播 这里必须定义 fanout
         */
        channel.exchangeDeclare("logs", "fanout");
        /** 发送消息
         * exchange：交换机名称
         * routingKey：
         * props：其他参数
         * 消息内容
         */
        channel.basicPublish("logs", "", null, "fanout type message".getBytes());

        RabbitMQUtil.close(channel, connection);
    }
}
