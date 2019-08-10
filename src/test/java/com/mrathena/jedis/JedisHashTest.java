package com.mrathena.jedis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mrathena on 2019-08-11 01:22
 */
public class JedisHashTest {

	private Jedis jedis;

	@Before
	public void before() {
		this.jedis = Common.getJedis();
	}

	@After
	public void after() {
		jedis.flushDB();
		jedis.disconnect();
	}

	@Test
	public void hset() {
		System.out.println(jedis.hset("key", "field1", "value1"));
		System.out.println(jedis.hset("key", "field1", "value2"));
		System.out.println(jedis.hset("key", "field3", "value3"));
		System.out.println(jedis.hset("key", "field4", "value4"));
		System.out.println(jedis.hset("key", "field5", "value5"));
		Common.print(jedis);
	}

	@Test
	public void hsetnx() {
		System.out.println(jedis.hsetnx("key", "field1", "value1"));
		System.out.println(jedis.hsetnx("key", "field1", "value2"));
		System.out.println(jedis.hsetnx("key", "field3", "value3"));
		Common.print(jedis);
	}

	@Test
	public void hmset() {
		Map<String, String> map = new HashMap<>(4);
		map.put("filed1", "value1");
		map.put("filed2", "value2");
		map.put("filed3", "value3");
		System.out.println(jedis.hmset("key", map));
		Common.print(jedis);
	}

	@Test
	public void hget() {
		hset();
		System.out.println(jedis.hget("key", "field1"));
		System.out.println(jedis.hget("key", "field2"));
		System.out.println(jedis.hget("key", "field3"));
		Common.print(jedis);
	}

	@Test
	public void hmget() {
		hset();
		System.out.println(jedis.hmget("key", "field1", "field2", "field3"));
		System.out.println(jedis.hmget("key2", "field1", "field2", "field3"));
		Common.print(jedis);
	}

	@Test
	public void hgetall() {
		hset();
		System.out.println(jedis.hgetAll("key"));
		System.out.println(jedis.hgetAll("key2"));
		Common.print(jedis);
	}

	@Test
	public void hkeys() {
		hset();
		System.out.println(jedis.hkeys("key"));
		System.out.println(jedis.hkeys("key2"));
		Common.print(jedis);
	}

	@Test
	public void hvals() {
		hset();
		System.out.println(jedis.hvals("key"));
		System.out.println(jedis.hvals("key2"));
		Common.print(jedis);
	}

	@Test
	public void hlen() {
		hset();
		System.out.println(jedis.hlen("key"));
		System.out.println(jedis.hlen("key2"));
		Common.print(jedis);
	}

	@Test
	public void hexists() {
		hset();
		System.out.println(jedis.hexists("key", "field1"));
		System.out.println(jedis.hexists("key", "field2"));
		System.out.println(jedis.hexists("key", "field3"));
		System.out.println(jedis.hexists("key2", "field1"));
		Common.print(jedis);
	}

	@Test
	public void hdel() {
		hset();
		System.out.println(jedis.hdel("key", "field1"));
		System.out.println(jedis.hdel("key", "field2"));
		System.out.println(jedis.hdel("key", "field3"));
		System.out.println(jedis.hdel("key2", "field1"));
		Common.print(jedis);
	}

	@Test
	public void incr() {
		// 可正可负,integer可以转double,反之报错,除非double的小数部分是0
		System.out.println(jedis.hincrBy("key", "field1", 2));
		System.out.println(jedis.hincrBy("key", "field1", -1));
		System.out.println(jedis.hincrByFloat("key", "field1", 3.5));
		System.out.println(jedis.hincrByFloat("key", "field1", -2.2));
		System.out.println(jedis.hincrByFloat("key", "field1", 0.7));
		System.out.println(jedis.hincrBy("key", "field1", 2));
	}

	@Test
	public void hscan() {
		System.out.println("懒得看了,这个命令的解释太长了");
	}

}
