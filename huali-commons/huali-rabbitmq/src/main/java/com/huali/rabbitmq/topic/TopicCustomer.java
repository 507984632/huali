package com.huali.rabbitmq.topic;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author myUser
 * @since 2021-02-15 17:03
 **/
@Component
public class TopicCustomer {

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue, exchange = @Exchange(name = "topics", type = "topic"),
                    key = {"#.info",}
            )
    })
    public void getMes(String mes) {
        System.out.println("匹配\"user\", \"#.info\", \"user.*.info\", \"user.#\" , mes= " + mes);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue, exchange = @Exchange(name = "topics", type = "topic"),
                    key = {"user.#",}
            )
    })
    public void getMes2(String mes) {
        System.out.println("匹配\"user\", \"user.#\" , mes= " + mes);
    }
}
