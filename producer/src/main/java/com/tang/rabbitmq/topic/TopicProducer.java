package com.tang.rabbitmq.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TopicProducer {
    //交换机名称
    static final String TOPIC_EXCHANGE = "topic_exchange";
    //队列名称
    static final String TOPIC_QUEUE_1 = "topic_queue_1";
    //队列名称
    static final String TOPIC_QUEUE_2 = "topic_queue_2";
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
        channel.queueDeclare(TOPIC_QUEUE_1,true,false,false,null);
        channel.queueDeclare(TOPIC_QUEUE_2,true,false,false,null);
        //声明交换机
        channel.exchangeDeclare(TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC,true,false,null);

        channel.queueBind(TOPIC_QUEUE_1,TOPIC_EXCHANGE,"tang.beijing.*.*");
        channel.queueBind(TOPIC_QUEUE_2,TOPIC_EXCHANGE,"tang.*.caiwubu.*");
  
        //4发消息
        // String exchange,  交换机
        // String routingKey, 路由键
        // AMQP.BasicProperties props, 属性
        // byte[] body 消息      string byte[] char[]如何相互转换的？
        String msg = "大家工资张两千";
        channel.basicPublish(TOPIC_EXCHANGE,"tang.beijing.caiwubu.info",null,msg.getBytes());
        msg = "老板跑路了";
        channel.basicPublish(TOPIC_EXCHANGE,"tang.beijing.renshi.error",null,msg.getBytes());
        msg = "全国所有部门降薪两千";
        channel.basicPublish(TOPIC_EXCHANGE,"tang.nanjing.caiwubu.error",null,msg.getBytes());

        //5、关闭连接
        channel.close();
        connection.close();

    }
}
