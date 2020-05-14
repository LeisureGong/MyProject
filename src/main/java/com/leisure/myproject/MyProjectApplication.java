package com.leisure.myproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;
import tk.mybatis.spring.annotation.MapperScan;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
//@EnableDiscoveryClient
@EnableAsync
@MapperScan(value = "com.leisure.myproject.dao",annotationClass = Repository.class)
public class MyProjectApplication {

	/**
	* 统一设置时区
	*/
	@PostConstruct
	void setDefaultTimezone(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
	}
	public static void main(String[] args) {
		SpringApplication.run(MyProjectApplication.class, args);
	}

}
