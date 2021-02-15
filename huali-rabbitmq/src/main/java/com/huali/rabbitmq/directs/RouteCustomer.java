package com.huali.rabbitmq.directs;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author myUser
 * @since 2021-02-15 16:51
 **/
@Component
public class RouteCustomer {
    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue,
            exchange = @Exchange(name = "directs", type = "direct"),
            key = {"info", "error", "debug"}
    )})
    public void getMesInfoAndErrAndDeBgu(String mes) {
        System.out.println("info,err,debug 消费者消费消息, mes=" + mes);
    }

    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue,
            exchange = @Exchange(name = "directs", type = "direct"),
            key = {"info"}
    )})
    public void getMes(String mes) {
        System.out.println("info 消费者消费消息, mes=" + mes);
    }
}
