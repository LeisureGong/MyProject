package com.leisure.myproject.controller;

import com.leisure.myproject.entity.Club;
import com.leisure.myproject.utils.ProtostuffSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.Date;
import java.util.List;

/**
 * @author gonglei
 * @date 2020/3/30 16:08
 */
@Component
public class RedisController {

	/**
	* 如果new RedisController，Value是不能自动注入的(autowired)
	 * 	 * spring不能管理new出来的对象
	*/
	@Value("${public.redis.host}")
	private String redisHost;

	@Value("${public.redis.host.port}")
	private String redisPort;

	public boolean mainRedis(String... args){
		Jedis jedis = new Jedis("47.98.192.149",6379);
		Pipeline pipeline = jedis.pipelined();
		pipeline.set("hello","redisworldworld");
		pipeline.incr("counter");
		List<Object> resultList = pipeline.syncAndReturnAll();
		System.out.println("hkojadsfhkjolafdshkjl");
		jedis.close();
		return true;
	}

	public boolean serializeRedis(String key){
		ProtostuffSerializer ps = new ProtostuffSerializer();
		Jedis jedis = new Jedis("47.98.192.149",6379);

		Club club = new Club(1,"AC","米兰",new Date(),1);
		byte[] clubBytes = ps.serialize(club);
		jedis.set(key.getBytes(),clubBytes);
		return true;
	}

	public static Club deSerializeRedis(String key){
		ProtostuffSerializer ps = new ProtostuffSerializer();
		Jedis jedis = new Jedis("47.98.192.149",6379);
		byte[] resultBytes = jedis.get(key.getBytes());
		Club resultClub = ps.deserialize(resultBytes);
		return resultClub;
	}

	public void operateJedis(){

		Jedis jedis = new Jedis(redisHost,Integer.parseInt(redisPort));
		//string
		jedis.incr("counter");
		//hash
		jedis.hset("myhash","f1","v1");
		jedis.hset("myhash","f2","v2");
		//list
		jedis.rpush("mylist","1");
		jedis.rpush("mylist","2");
		jedis.rpush("mylist","3");
		jedis.lpush("mylist","4");
		//set
		jedis.sadd("myset","a");
		jedis.sadd("myset","b");
		jedis.sadd("myset","c");
		//zset
		jedis.zadd("myzset",99,"tom");
		jedis.zadd("myzset",33,"james");
		jedis.zadd("myzset",66,"peter");

	}
}
