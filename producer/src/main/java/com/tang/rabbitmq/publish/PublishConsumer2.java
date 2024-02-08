package com.tang.rabbitmq.publish;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class PublishConsumer2 {
    public static final String FANOUT_EXCHANGE = "fanout_exchange";
    public static final String FANOUT_QUEUE_1= "fanout_queue_1";
    public static final String FANOUT_QUEUE_2= "fanout_queue_2";
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
        channel.queueDeclare(FANOUT_QUEUE_1,true,false,false,null);
        channel.queueDeclare(FANOUT_QUEUE_2,true,false,false,null);

        //声明交换机
        /*
         * 参数1：交换机名称
         * 参数2：交换机类型，fanout、topic、direct、headers
         * 参数3：是否定义持久化
         * 参数4：是否在不使用的时候自动删除
         * 参数5：属性
         */
        channel.exchangeDeclare(FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT,true,false,null);

        //队列绑定交换机
//        String queue,队列名称
//        String exchange,交换机名称
//        String routingKey，路由键

        channel.queueBind(FANOUT_QUEUE_2,FANOUT_EXCHANGE,"");
        channel.queueBind(FANOUT_QUEUE_1,FANOUT_EXCHANGE,"");
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
              //业务逻辑
//                System.out.println("consumerTag = " + consumerTag);
//                //经过那个交换机
//                System.out.println("envelope = " + envelope.getExchange());
//                //路由key
//                System.out.println("envelope = " + envelope.getRoutingKey());
//                //消息id
//                System.out.println("envelope = " + envelope.getDeliveryTag());
                System.out.println(new String(body));


            }
        };

        channel.basicConsume(FANOUT_QUEUE_2,true,consumer);

//        channel.close();
//        connection.close();
    }
}
