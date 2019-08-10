package com.mrathena.jedis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author mrathena on 2019-08-11 01:22
 */
public class JedisListTest {

	private Jedis jedis;

	@Before
	public void before() {
		this.jedis = new Jedis(Common.HOST, Common.PORT);
		jedis.connect();
		jedis.auth(Common.PASSWORD);
	}

	@Test
	public void test() throws Exception {
		jedis.lpush("test", "1");
		jedis.rpush("test", "2");
		jedis.lpush("test", "3");
		jedis.rpush("test", "4");
		jedis.lpush("test", "6", "6", "6");


		System.out.println(jedis.lindex("test", 2));
		Common.print(jedis);

		System.out.println(jedis.lpop("test"));
		Common.print(jedis);

		jedis.flushAll();
		Common.print(jedis);
	}

}
