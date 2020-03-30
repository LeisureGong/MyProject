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
		Club result = deSerializeRedis(key);
		System.out.println(result);
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
}
