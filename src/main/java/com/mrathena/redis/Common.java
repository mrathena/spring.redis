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
	public static void print(Jedis jedis) {
		Set<String> keys = jedis.keys("*");
		keys.forEach(item -> {
			System.out.print("Key:" + item + ": ");
			String type = jedis.type(item);
			switch (type) {
				case "string":
					System.out.println("Value:" + jedis.get(item));
					break;
				case "list":
					System.out.print("Size:" + jedis.llen(item) + " ");
					System.out.println("Value:" + jedis.lrange(item, 0, jedis.llen(item) - 1));
					break;
				case "set":
					System.out.print("Size:" + jedis.scard(item) + " ");
					System.out.println("Value:" + jedis.smembers(item));
					break;
				default:
					System.out.println(type);
			}
		});
		System.out.println();
	}

}
