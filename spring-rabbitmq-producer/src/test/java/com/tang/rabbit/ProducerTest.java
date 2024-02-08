package com.tang.rabbit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq.xml")
public class ProducerTest {
    @Autowired
    RabbitTemplate rabbitTemplate;
    //简单模式
   @Test
    public void helloTest(){
       String msg="hello rabbitmq!";
       rabbitTemplate.convertAndSend("","tangque",msg);
   }
   //发布订阅模式
    @Test
    public void publishTest(){
       String msg="hello publish";
       rabbitTemplate.convertAndSend("spring_fanout_exchange","",msg);
    }
    @Test
    public void routingTest(){
       String msg="hello routing error";
       String info="hello routing info";
       rabbitTemplate.convertAndSend("spring_direct_exchange","error",msg);
       rabbitTemplate.convertAndSend("spring_direct_exchange","info",info);
    }
    @Test
    public void topicTest(){
       String msg = "老板跑路了";
       String msg1 = "经理跑路了";
       rabbitTemplate.convertAndSend("spring_topic_exchange","ydlclass.taiyuan.caiwubu.info",msg1);
       rabbitTemplate.convertAndSend("spring_topic_exchange","ydlclass.beijing.caiwubu.error",msg1);

    }
}
