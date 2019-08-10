package com.mrathena.jedis;

import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @author mrathena on 2019-08-11 01:19
 */
public class Common {

	public static final String HOST = "116.62.162.47";
	public static final int PORT = 6379;
	public static final String PASSWORD = "Hhsrv587..";
	public static final int DATABASE = 15;

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
				case "zset":
					System.out.print("Size:" + jedis.zcard(item) + " ");
					System.out.println("Value:" + jedis.zrange(item, 0, 100));
					break;
				case "hash":
					System.out.print("Size:" + jedis.hlen(item) + " ");
					System.out.println("Value:" + jedis.hgetAll(item));
					break;
				default:
					System.out.println(type);
			}
		});
		if (keys.size() == 0) {
			System.out.println("empty");
		}
		System.out.println();
	}

}
