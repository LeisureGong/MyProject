package com.leisure.myproject.rabbitmqclient;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

/**
 * 直连模式Receiver
 *
 * @author gonglei
 * @date 2020/4/29
 */
@Slf4j
public class Recv {

	public final static String QUEUE_NAME = "hellos";
	public static void main(String[] args) throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("47.98.192.149");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		log.info(" [*] Waiting for messages. To exit press CTRL+C");

		DeliverCallback deliverCallback = (consumerTag,delivery) ->{
			String message = new String(delivery.getBody(),"UTF-8");
			log.info("[x] Received '" + message + "'");
		};
		channel.basicConsume(QUEUE_NAME,true,deliverCallback,consumerTag ->{});
	}
}
