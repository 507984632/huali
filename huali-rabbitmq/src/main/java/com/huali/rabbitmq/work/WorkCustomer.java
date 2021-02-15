package com.huali.rabbitmq.work;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author myUser
 * @since 2021-02-15 16:18
 **/
@Component
public class WorkCustomer {

    @RabbitListener(queuesToDeclare = @Queue("work"))
    public void accept(String mes) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.err.println("工作队列1：mes=" + mes);
    }

    @RabbitListener(queuesToDeclare = @Queue("work"))
    public void accept2(String mes) {
        System.err.println("工作队列2：mes=" + mes);
    }
}
