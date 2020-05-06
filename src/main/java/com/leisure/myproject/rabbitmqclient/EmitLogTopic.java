package com.leisure.myproject.rabbitmqclient;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Topic模式
 * routing key为一个'.'分隔开的字符串
 * binding key与routing key一样也是'.'分隔的字符串
 * @author gonglei
 * @date 2020/5/6
 */
@Slf4j
public class EmitLogTopic {

	private static final String EXCHANGE_NAME = "topic_logs";
	public static void main(String[] args) throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("47.98.192.149");
		try(Connection connection = factory.newConnection();
			Channel channel = connection.createChannel()){
			channel.exchangeDeclare(EXCHANGE_NAME,"topic");
			String routingKey = getRouting(args);
			String message = getMessage(args);

			channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes("UTF-8"));
			log.info(" [x] Sent '" + routingKey + "' : '" + message + "'");
		}
	}

	//设置Routing Key
	private static String getRouting(String[] strings){
		if(strings.length < 1){
			return "anonymous.info";
		}
		return strings[0];
	}

	private static String getMessage(String[] strings){
		if(strings.length < 2){
			return "Hello World!";
		}
		return joinStrings(strings," ",1);
	}

	private static String joinStrings(String[] strings,String delimiter,int startIndex){
		int length = strings.length;
		if(length == 0) return "";
		if(length < startIndex) return "";
		StringBuilder  words = new StringBuilder(strings[startIndex]);
		for(int i = startIndex + 1;i < length;i++){
			words.append(delimiter).append(strings[i]);
		}
		return words.toString();
	}
}
