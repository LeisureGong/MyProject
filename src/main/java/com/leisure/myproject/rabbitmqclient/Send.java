package com.leisure.myproject.rabbitmqclient;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 *
 * @author gonglei
 * @date 2020/4/29
 */
@Slf4j
@Configuration
public class Send {

	@Value("${spring.rabbitmq.host}")
	private static String RABBIT_HOST;

	private final static String QUEUE_NAME = "hellos";

	public static void main(String[] args) throws Exception{
		RABBIT_HOST = "47.98.192.149";
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RABBIT_HOST);
		try(Connection connection = factory.newConnection();
		    Channel channel = connection.createChannel()){
			channel.queueDeclare(QUEUE_NAME,false,false,false,null);
			String message = "Hello GongLei!";
			channel.basicPublish("",QUEUE_NAME,null,message.getBytes(StandardCharsets.UTF_8));
			System.out.println("[x] Sent '" + message +"'");
		}

	}
}
