package com.mrathena.jedis;

import redis.clients.jedis.Jedis;

/**
 * @author mrathena on 2019/3/29 17:52
 */
public class TypeListTest {

	public static Jedis preTest() {
		Jedis jedis = new Jedis(Common.HOST, Common.PORT);
		jedis.connect();

		jedis.lpush("test", "1");
		jedis.rpush("test", "2");
		jedis.lpush("test", "3");
		jedis.rpush("test", "4");
		jedis.lpush("test", "6", "6", "6");

		Common.print(jedis);

		return jedis;
	}

	public static void main(String[] args) throws Exception {

		Jedis jedis = preTest();

		System.out.println(jedis.lindex("test", 2));
		Common.print(jedis);

		System.out.println(jedis.lpop("test"));
		Common.print(jedis);

		jedis.flushAll();
		Common.print(jedis);

		jedis.disconnect();

	}

}
