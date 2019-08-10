package com.mrathena.jedis;

import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @author mrathena on 2019-08-11 01:19
 */
public class Common {

	private static final String HOST = "116.62.162.47";
	private static final int PORT = 6379;
	private static final String PASSWORD = "Hhsrv587..";
	private static final int DATABASE = 15;

	/**
	 * 获取测试用Jedis
	 */
	public static Jedis getJedis() {
		Jedis jedis = new Jedis(HOST, PORT);
		jedis.connect();
		jedis.auth(PASSWORD);
		// 使用DB15号库来测试,不影响DB0的数据
		jedis.select(DATABASE);
		jedis.flushDB();
		return jedis;
	}

	/**
	 * 列出所有的key
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
					System.out.println("Size:" + jedis.llen(item) + ",Value:" + jedis.lrange(item, 0, -1));
					break;
				case "set":
					System.out.println("Size:" + jedis.scard(item) + ",Value:" + jedis.smembers(item));
					break;
				case "zset":
					System.out.println("Size:" + jedis.zcard(item) + ",Value:" + jedis.zrange(item, 0, 100));
					break;
				case "hash":
					System.out.println("Size:" + jedis.hlen(item) + ",Value:" + jedis.hgetAll(item));
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
