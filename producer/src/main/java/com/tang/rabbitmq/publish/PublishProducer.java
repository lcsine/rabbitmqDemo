package com.tang.rabbitmq.publish;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PublishProducer {
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
        //声明队列
        //String queue,  队列名
        // boolean durable, 持久化
        // boolean exclusive, 排他的
        // boolean autoDelete, 自动删除
        // Map<String, Object> arguments 属性
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
        channel.queueBind(FANOUT_QUEUE_1,FANOUT_EXCHANGE,"");
        channel.queueBind(FANOUT_QUEUE_2,FANOUT_EXCHANGE,"");
        //4发消息
        // String exchange,  交换机
        // String routingKey, 路由键
        // AMQP.BasicProperties props, 属性
        // byte[] body 消息      string byte[] char[]如何相互转换的？
        String msg = "hello rabbitmq!publishQueue";
        channel.basicPublish(FANOUT_EXCHANGE,"",null,msg.getBytes());

        //5、关闭连接
        channel.close();
        connection.close();

    }
}
