package com.mrathena.jedis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author mrathena on 2019-08-11 01:23
 */
public class JedisZSetTest {

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
	public void zadd() {
		// ZADD key [NX|XX] [CH] [INCR] score member [score member ...]

	}

	@Test
	public void test() {
		jedis.zadd("test", 1, "3");
		jedis.zadd("test", 2, "1");
		jedis.zadd("test", 3, "2");
		jedis.zadd("test", 4, "4");
		jedis.zadd("test", 5, "4");
		jedis.zadd("test", 7, "5");

		Common.print(jedis);

	}

}
