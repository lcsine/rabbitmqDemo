package com.tang.rabbitmq.listener;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
public class SpringQueueListener implements MessageListener{
    @Override
    public void onMessage(Message message) {
        //业务逻辑
        byte[] body = message.getBody();
        System.out.println(new String(body));
    }
}
