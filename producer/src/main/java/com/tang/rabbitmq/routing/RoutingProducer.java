package com.tang.rabbitmq.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RoutingProducer {
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
        //声明队列
        //String queue,  队列名
        // boolean durable, 持久化
        // boolean exclusive, 排他的
        // boolean autoDelete, 自动删除
        // Map<String, Object> arguments 属性
        channel.queueDeclare(DIRECT_QUEUE_1,true,false,false,null);
        channel.queueDeclare(DIRECT_QUEUE_2,true,false,false,null);
        //声明交换机
        channel.exchangeDeclare(DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT,true,false,null);

        channel.queueBind(DIRECT_QUEUE_1,DIRECT_EXCHANGE,"error");
        channel.queueBind(DIRECT_QUEUE_2,DIRECT_EXCHANGE,"info");
        channel.queueBind(DIRECT_QUEUE_2,DIRECT_EXCHANGE,"error");
        channel.queueBind(DIRECT_QUEUE_2,DIRECT_EXCHANGE,"warning");
        //4发消息
        // String exchange,  交换机
        // String routingKey, 路由键
        // AMQP.BasicProperties props, 属性
        // byte[] body 消息      string byte[] char[]如何相互转换的？
        String msg = "hello rabbitmq error";
        channel.basicPublish(DIRECT_EXCHANGE,"error",null,msg.getBytes());
        msg = "hello rabbitmq info";
        channel.basicPublish(DIRECT_EXCHANGE,"info",null,msg.getBytes());
        msg = "hello rabbitmq warning";
        channel.basicPublish(DIRECT_EXCHANGE,"warning",null,msg.getBytes());

        //5、关闭连接
        channel.close();
        connection.close();

    }
}
