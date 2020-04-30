package com.leisure.myproject.rabbitmqclient;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gonglei
 * @date 2020/4/29
 */
@Slf4j
public class Worker {

	private static final String TASK_QUEUE_NAME = "task_queue";

	public static void main(String[] args) throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("47.98.192.149");
		final Connection connection = factory.newConnection();
		final Channel channel = connection.createChannel();

		channel.queueDeclare(TASK_QUEUE_NAME,true,false,false,null);
		log.info("[*] Waiting for messages. To exit press CTRL+C");

		channel.basicQos(1);
		DeliverCallback deliverCallback = (consumerTag,delivery) -> {
			String message = new String(delivery.getBody(),"UTF-8");
			log.info(" [x] Received '" + message + "'");
			try {
				doWork(message);
			}finally {
				log.info(" [x] Done");
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
			}
		};
		channel.basicConsume(TASK_QUEUE_NAME,false,deliverCallback,consumerTag->{});
	}

	private static void doWork(String task){
		for(char ch : task.toCharArray()){
			if(ch == '.'){
				try{
					Thread.sleep(1000);
				}catch (InterruptedException _ignored){
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}
