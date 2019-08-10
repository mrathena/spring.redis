package com.mrathena.jedis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author mrathena on 2019-08-11 01:22
 */
public class JedisSetTest {

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
	public void test() throws Exception {
		jedis.sadd("test", "3");
		jedis.sadd("test", "1");
		jedis.sadd("test", "2");
		jedis.sadd("test", "4");
		jedis.sadd("test", "4");
		jedis.sadd("test", "6", "6", "6");
		jedis.sadd("test", "5");

		Common.print(jedis);

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
