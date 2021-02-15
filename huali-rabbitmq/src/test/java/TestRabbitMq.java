import com.huali.rabbitmq.TestRabbitMqApplication;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author myUser
 * @since 2021-02-15 18:44
 **/
@SpringBootTest(classes = TestRabbitMqApplication.class)
public class TestRabbitMq {

    @Test
    public void tset1() {
        System.out.println(1);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试hello
     */
    @Test
    void helloMessage() {
        rabbitTemplate.convertAndSend("hello", "Hello spring rabbitmq");
    }

    /**
     * 工作模型
     */
    @Test
    void workMessage() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("work", "Work spring rabbitmq");
        }
    }

    /**
     * fanout 广播模型
     */
    @org.junit.jupiter.api.Test
    void fanoutMessage() {
        rabbitTemplate.convertAndSend("logs", "", "广播内容，在一个交换机内所有的消息队列中消费");
    }

    /**
     * direct 订阅者模型(路由模型)
     */
    @Test
    public void routeMessage() {
        rabbitTemplate.convertAndSend("directs", "info", "基于 info 发送的订阅信息");
    }

    /**
     * topic 动态路由模型
     */
    @Test
    public void topicMessage() {
        rabbitTemplate.convertAndSend("topics", "user.save.info", "发送的是 基于key = [user.save.info] 的消息 ");
    }

}
