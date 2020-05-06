package com.leisure.myproject.rabbitmqclient;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gonglei
 * @date 2020/5/6
 */
@Slf4j
public class ReceiveLogsTopic {

	private static final String EXCHANGE_NAME = "topic_logs";

	public static void main(String[] args) throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("47.98.192.149");
		Connection connection  = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME,"topic");
		String queueName = channel.queueDeclare().getQueue();

		if(args.length < 1){
			log.info("Usage : ReceiveLogsTopic [binding_key]...");
			System.exit(1);
		}

		for(String bindingKey : args){
			channel.queueBind(queueName,EXCHANGE_NAME,bindingKey);
		}

		log.info("[*] Waiting for messages.");

		DeliverCallback deliverCallback = (consumerTag,delivery) ->{
			String message = new String(delivery.getBody(),"UTF-8");
			log.info("[x] Received" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
		};
		channel.basicConsume(queueName,true,deliverCallback,consumerTag ->{});
	}
}
