package com.leisure.myproject.controller; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;

/** 
* RedisController Tester. 
* 
* @author <Authors name> 
* @since <pre>3?? 31, 2020</pre> 
* @version 1.0 
*/
public class RedisControllerTest {

	@Autowired
	RedisController redisController = new RedisController();

	@Before
	public void before() throws Exception {
		System.out.println("--------测试开始-----------");
	}

	@After
	public void after() throws Exception {
		System.out.println("--------测试结束----------");
	}

	/**
	*
	* Method: mainRedis(String... args)
	*
	*/
	@Test
	public void testMainRedis() throws Exception {
		redisController.mainRedis();
	}

	/**
	*
	* Method: serializeRedis(String key)
	*
	*/
	@Test
	public void testSerializeRedis() throws Exception {
	//TODO: Test goes here...
	}

	/**
	*
	* Method: deSerializeRedis(String key)
	*
	*/
	@Test
	public void testDeSerializeRedis() throws Exception {
	//TODO: Test goes here...
	}

	/**
	*
	* Method: operateJedis()
	*
	*/
	@Test
	public void testOperateJedis() throws Exception {
	//TODO: Test goes here...
} 


} 
