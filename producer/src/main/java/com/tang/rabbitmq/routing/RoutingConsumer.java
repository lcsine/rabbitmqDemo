package com.tang.rabbitmq.routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;



public class RoutingConsumer {
    public static final String DIRECT_EXCHANGE = "direct_exchange";
    public static final String DIRECT_QUEUE_1= "direct_queue_1";
    public static final String DIRECT_QUEUE_2= "direct_queue_2";
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
        channel.queueDeclare(DIRECT_QUEUE_1,true,false,false,null);
        channel.queueDeclare(DIRECT_QUEUE_2,true,false,false,null);
        //声明交换机
        channel.exchangeDeclare(DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT,true,false,null);

        channel.queueBind(DIRECT_QUEUE_1,DIRECT_EXCHANGE,"error");
        channel.queueBind(DIRECT_QUEUE_2,DIRECT_EXCHANGE,"info");
        channel.queueBind(DIRECT_QUEUE_2,DIRECT_EXCHANGE,"error");
        channel.queueBind(DIRECT_QUEUE_2,DIRECT_EXCHANGE,"warning");
        /*
          参数1：队列名称
          参数2：是否自动确认，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动确认
          参数3：消息接收到后回调
         */
        Consumer consumer = new DefaultConsumer(channel){
            /**
             * @param consumerTag 消费者标签
             * @param envelope 信封 保存很多信息
             * @param properties 属性
             * @param body 消息字节数组
             * @throws IOException
             *///回调函数，收到消息我要干啥
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println(new String(body));
            }
        };

        channel.basicConsume(DIRECT_QUEUE_1,true,consumer);

//        channel.close();
//        connection.close();
    }
}
