package com.leisure.myproject.rabbitmqclient;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gonglei
 * @date 2020/4/30
 */
@Slf4j
public class EmitLogDirect {

	private static final String EXCHANGE_NAME = "direct_logs";

	public static void main(String[] args) throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("47.98.192.149");
		try(Connection connection = factory.newConnection();
		    Channel channel = connection.createChannel()){
			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

			String severity = getSeverity(args);
			String message = getMessage(args);

			channel.basicPublish(EXCHANGE_NAME,severity,null,message.getBytes("UTF-8"));
			log.info(" [x] Sent '" + severity + "'" + message + "'");
		}
	}

	private static String getSeverity(String[] strings){
		if(strings.length < 1){
			return "info";
		}
		return strings[0];
	}

	public static String getMessage(String[] strings){
		if(strings.length < 2){
			return "Hello World!";
		}
		return joinStrings(strings,"",1);
	}

	public static String joinStrings(String[] strings,String delimiter,int startIndex){
		int length = strings.length;
		if(length == 0) return "";
		if(length <= startIndex) return "";
		StringBuilder words = new StringBuilder(strings[startIndex]);
		for(int i = startIndex + 1;i < length;i++){
			words.append(delimiter).append(strings[i]);
		}
		return words.toString();
	}
}
