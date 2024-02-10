package com.tang.rabbitmq.config;

import com.rabbitmq.client.BuiltinExchangeType;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    @Bean("boot_hello_queue")
    public Queue queue(){
        return new Queue("boot_hello_queue",true,false,false);
    }
    public static final String FANOUT_EXCHANGE = "boot_fanout_exchange";
    //队列名称
    public static final String FANOUT_QUEUE_1 = "boot_fanout_queue_1";
    //队列名称
    public static final String FANOUT_QUEUE_2 = "boot_fanout_queue_2";
    @Bean(FANOUT_QUEUE_1)
    public Queue p1_queue(){
        return new Queue(FANOUT_QUEUE_1,true,false,false);
    }
    @Bean(FANOUT_QUEUE_2)
    public Queue p2_queue(){
        return new Queue(FANOUT_QUEUE_2,true,false,false);
    }
    @Bean(FANOUT_EXCHANGE)
    public Exchange FANOUT_EXCHANGE(){
        return ExchangeBuilder.fanoutExchange(FANOUT_EXCHANGE).durable(true).build();
    }
    @Bean
    public Binding FANOUT_QUEUE_1_FANOUT_EXCHANGE(@Qualifier(FANOUT_QUEUE_1) Queue queue,
                                                 @Qualifier(FANOUT_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }

    @Bean
    public Binding FANOUT_QUEUE_2_FANOUT_EXCHANGE(@Qualifier(FANOUT_QUEUE_2) Queue queue,
                                                 @Qualifier(FANOUT_EXCHANGE) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }

}
