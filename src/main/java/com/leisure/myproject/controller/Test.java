package com.leisure.myproject.controller;

import com.leisure.myproject.entity.Club;
import com.leisure.myproject.utils.ProtostuffSerializer;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.Date;

/**
 * @author gonglei
 * @date 2020/3/30 16:08
 */
public class Test {

	public static void main(String... args){
		String key = "Club:1";
		operateJedis();
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

	public static void operateJedis(){

		Jedis jedis = new Jedis("47.98.192.149",6379);
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
