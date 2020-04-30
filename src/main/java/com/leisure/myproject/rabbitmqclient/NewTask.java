package com.leisure.myproject.rabbitmqclient;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gonglei
 * @date 2020/4/29
 */
@Slf4j
public class NewTask {

	private static final String TASK_QUEUE_NAME = "task_queue";

	public static void main(String[] args) throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("47.98.192.149");
		try(Connection connection = factory.newConnection();
			Channel channel = connection.createChannel()){
			channel.queueDeclare(TASK_QUEUE_NAME,true,false,false,null);
			String message = String.join(" ",args);
			channel.basicPublish("", TASK_QUEUE_NAME,
					MessageProperties.PERSISTENT_TEXT_PLAIN,
					message.getBytes("UTF-8"));
			log.info("[x] Sent '" + message + "'");
		}
	}
}
