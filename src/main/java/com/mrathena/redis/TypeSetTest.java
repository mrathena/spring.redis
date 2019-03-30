package com.mrathena.redis;

import redis.clients.jedis.Jedis;

/**
 * @author mrathena on 2019/3/29 17:53
 */
public class TypeSetTest {

	public static Jedis preTest() {
		Jedis jedis = new Jedis(Common.HOST, Common.PORT);
		jedis.connect();

		jedis.sadd("test", "1");
		jedis.sadd("test", "2");
		jedis.sadd("test", "3");
		jedis.sadd("test", "4");
		jedis.sadd("test", "4");
		jedis.sadd("test", "6", "6", "6");
		jedis.sadd("test", "5");

		Common.print(jedis);

		return jedis;
	}

	public static void main(String[] args) throws Exception {

		Jedis jedis = preTest();

		System.out.println(jedis.sismember("test", "1"));
		System.out.println(jedis.sismember("test", "7"));
		Common.print(jedis);

		System.out.println(jedis.spop("test"));
		Common.print(jedis);

		System.out.println(jedis.spop("test"));
		Common.print(jedis);

		jedis.flushAll();
		Common.print(jedis);

		jedis.disconnect();

	}

}
