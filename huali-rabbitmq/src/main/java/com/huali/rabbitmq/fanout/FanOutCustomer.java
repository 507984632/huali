package com.huali.rabbitmq.fanout;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author myUser
 * @since 2021-02-15 16:32
 **/
@Component
public class FanOutCustomer {

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue, //绑定消息队列 如果不填写任何东西就是一个临时队列
                    exchange = @Exchange( // 绑定交换机
                            name = "logs", // 交换机名称
                            type = "fanout" //交换机类型
                    )
            )
    })
    public void getMessage(String message) {
        System.out.println("广播接受者1 ：mes=" + message);
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue, exchange = @Exchange(name = "logs", type = "fanout"))})
    public void getMessage2(String message) {
        System.out.println("广播接受者2 ：mes=" + message);
        System.out.println("也代表了会接受 work 消息队列中的消息");
    }
}
