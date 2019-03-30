package com.mrathena.redis;

import redis.clients.jedis.Jedis;

/**
 * @author mrathena on 2019/3/29 17:53
 */
public class TypeZSetTest {

	public static Jedis preTest() {
		Jedis jedis = new Jedis(Common.HOST, Common.PORT);
		jedis.connect();

		jedis.zadd("test", 1, "3");
		jedis.zadd("test", 2, "1");
		jedis.zadd("test", 3, "2");
		jedis.zadd("test", 4, "4");
		jedis.zadd("test", 5, "4");
		jedis.zadd("test", 7, "5");

		Common.print(jedis);

		return jedis;
	}

	public static void main(String[] args) throws Exception {

		Jedis jedis = preTest();

		jedis.flushAll();
		Common.print(jedis);

		jedis.disconnect();

	}

}
