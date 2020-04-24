package com.leisure.myproject.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gonglei
 * @date 2020/4/23 20:46
 */
@Configuration
public class RabbitConfig {

	@Bean
	public Queue Queue(){
		return new Queue("hello");
	}
}
