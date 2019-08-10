package com.mrathena.jedis;

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
		this.jedis = new Jedis(Common.HOST, Common.PORT);
		jedis.connect();
		jedis.auth(Common.PASSWORD);
	}

	@Test
	public void test() throws Exception {
		jedis.zadd("test", 1, "3");
		jedis.zadd("test", 2, "1");
		jedis.zadd("test", 3, "2");
		jedis.zadd("test", 4, "4");
		jedis.zadd("test", 5, "4");
		jedis.zadd("test", 7, "5");

		Common.print(jedis);

		jedis.flushAll();
		Common.print(jedis);

		jedis.disconnect();
	}

}
