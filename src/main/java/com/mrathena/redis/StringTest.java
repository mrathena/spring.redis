package com.mrathena.redis;

import redis.clients.jedis.Jedis;

/**
 * @author mrathena on 2019/3/29 15:54
 */
public class StringTest {

	public static Jedis preTest() {
		Jedis jedis = new Jedis(Common.HOST, Common.PORT);
		jedis.connect();

		jedis.set("test", "test");
		jedis.set("test2", "1");

		Common.keys(jedis);

		return jedis;
	}

	public static void main(String[] args) throws Exception {

		Jedis jedis = preTest();

		jedis.set("key1", "value1");
		jedis.set("key2", "value2");
		Common.keys(jedis);

		//将值value关联到key，并将key的生存时间设为seconds(秒)。
		jedis.setex("foo", 1, "foo");
		Common.keys(jedis);
		Thread.sleep(1000);
		Common.keys(jedis);

		jedis.del("test");
		jedis.del("key1", "key2");
		Common.keys(jedis);

		jedis.incr("test2");
		Common.keys(jedis);
		jedis.incrBy("test2", 10);
		Common.keys(jedis);

		jedis.decr("test2");
		Common.keys(jedis);
		jedis.decrBy("test2", 1);
		Common.keys(jedis);

		String type = jedis.type("test2");
		System.out.println(type);

		jedis.flushAll();
		Common.keys(jedis);

		jedis.disconnect();

	}

}
