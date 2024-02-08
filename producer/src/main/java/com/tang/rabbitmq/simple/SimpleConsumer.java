package com.tang.rabbitmq.simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.tang.rabbitmq.simple.SimpleProducer.QUEUE;

public class SimpleConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1、创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置ip
        connectionFactory.setHost("localhost");
        //设置端口
        connectionFactory.setPort(5672);
        //设置虚拟主机
        connectionFactory.setVirtualHost("/");
        //设置用户名和密码
        connectionFactory.setUsername("tyb");
        connectionFactory.setPassword("tyb");
        //2、创建长连接
        Connection connection = connectionFactory.newConnection();
        //3、创建channel
        Channel channel = connection.createChannel();
        //4、监听队列 防止运维先起消费者，没有队列，报错

        /*
          参数1：队列名称
          参数2：是否自动确认，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动确认
          参数3：消息接收到后回调
         */
        com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel){
            /**
             * @param consumerTag 消费者标签
             * @param envelope 信封 保存很多信息
             * @param properties 属性
             * @param body 消息字节数组
             * @throws IOException
             *///回调函数，收到消息我要干啥
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
              //业务逻辑
                System.out.println("consumerTag = " + consumerTag);
                //经过那个交换机
                System.out.println("envelope = " + envelope.getExchange());
                //路由key
                System.out.println("envelope = " + envelope.getRoutingKey());
                //消息id
                System.out.println("envelope = " + envelope.getDeliveryTag());
                System.out.println(new String(body));
            }
        };

        channel.basicConsume(QUEUE,true,consumer);

//        channel.close();
//        connection.close();
    }
}
