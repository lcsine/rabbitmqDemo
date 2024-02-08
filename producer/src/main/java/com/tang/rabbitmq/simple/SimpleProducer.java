package com.tang.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SimpleProducer {
    public static final String QUEUE = "tangqueue";
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
        channel.queueDeclare(QUEUE,true,false,false,null);
        //4发消息
        // String exchange,  交换机
        // String routingKey, 路由键
        // AMQP.BasicProperties props, 属性
        // byte[] body 消息      string byte[] char[]如何相互转换的？
        String msg = "hello rabbitmq";
        channel.basicPublish("",QUEUE,null,msg.getBytes());
        //5、关闭连接
        channel.close();
        connection.close();

    }
}
