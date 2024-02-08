package com.tang.rabbit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq.xml")
public class ConsumerTest {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Test
    public void readHello(){
        String o = (String) rabbitTemplate.receiveAndConvert("tangque");
        System.out.println("o = " + o);
    }
}
