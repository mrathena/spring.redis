package com.mrathena.redis;

import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @author mrathena on 2019/3/29 15:51
 */
public class Common {

	public static final String HOST = "192.168.1.69";
	public static final int PORT = 6379;

	/**
	 * //列出所有的key
	 */
	public static void keys(Jedis jedis) {
		Set<String> keys = jedis.keys("*");
		keys.forEach(item -> System.out.println(item + ": " + jedis.get(item)));
		System.out.println();
	}

}
