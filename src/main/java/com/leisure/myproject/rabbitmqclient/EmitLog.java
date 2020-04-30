package com.leisure.myproject.rabbitmqclient;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author gonglei
 * @date 2020/4/30
 */
@Slf4j
public class EmitLog {

	public static final String EXCHANGE_NAME = "logs";

	public static void main(String[] args) throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("47.98.192.149");
		try(Connection connection = factory.newConnection();
		    Channel channel = connection.createChannel()){
			//声明交换方式
			channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
			String message = args.length < 1 ? "info : Hello World!" :
					String.join(" ",args);

			channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes("UTF-8"));
			log.info(" [x] Sent '" + message + "' ");
		}
	}
}
