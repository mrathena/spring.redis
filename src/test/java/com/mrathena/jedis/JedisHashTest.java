package com.mrathena.jedis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanResult;

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

	@Test
	public void test() throws Exception {
		jedis.hset("test", "username", "胡诗瑞");
		jedis.hset("test", "password", "nicai");
		jedis.hset("test", "nickname", "mrathena");
		jedis.hset("test", "sex", "male");
		jedis.hset("test", "age", "27");
		Map<String, String> map = new HashMap<>(4);
		map.put("filed", "value");
		map.put("filed2", "value");
		map.put("filed3", "value");
		jedis.hset("test", "filed3", "000");
		jedis.hmset("test", map);

		Common.print(jedis);

		System.out.println(jedis.hget("test", "nickname"));
		Common.print(jedis);

		System.out.println(jedis.exists("test", "nickname"));
		Common.print(jedis);

		System.out.println(jedis.hincrBy("test", "age", 1));
		Common.print(jedis);

		System.out.println(jedis.hkeys("test"));
		Common.print(jedis);

		System.out.println(jedis.hmget("test", "username", "nickname"));
		Common.print(jedis);

		System.out.println(jedis.hvals("test"));
		Common.print(jedis);

		ScanResult<String> result = jedis.scan("0");
		System.out.println(result.getCursor());
		System.out.println(result.getResult());
		Common.print(jedis);

		jedis.flushAll();
		Common.print(jedis);

		jedis.disconnect();
	}

}
