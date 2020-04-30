package com.leisure.myproject.rabbitmqclient;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gonglei
 * @date 2020/4/30
 */
@Slf4j
public class ReceiveLogsDirect {

	private static final String EXCHANGE_NAME = "direct_logs";

	public static void main(String[] args) throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("47.98.192.149");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
		String queueName = channel.queueDeclare().getQueue();

		if(args.length < 1){
			log.info("Usage: ReceiveLogsDirect [info] [warning] [error]");
			System.exit(1);
		}

		for(String severity : args){
			channel.queueBind(queueName,EXCHANGE_NAME,severity);
		}

		log.info(" [*] Waiting for messages. To exit press CTRL+C");

		DeliverCallback deliverCallback = (consumerTag,delivery) ->{
			String message = new String(delivery.getBody(),"UTF-8");
			log.info("[x] Received '" +
					delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
		};
		channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});
	}
}
