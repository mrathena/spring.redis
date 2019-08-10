package com.mrathena.jedis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author mrathena on 2019-08-11 04:00
 */
public class JedisTest {

	private Jedis jedis;

	@Before
	public void before() {
		this.jedis = Common.getJedis();
		jedis.set("string", "value");
		jedis.rpush("list", "value");
		jedis.sadd("set", "value");
		jedis.hset("hash", "key", "value");
		jedis.zadd("zset", 0.1, "value");
	}

	@After
	public void after() {
		// 刷新DB15
		jedis.flushDB();
		jedis.disconnect();
		notice();
	}

	@Test
	public void exist() {
		System.out.println(jedis.exists("string"));
		System.out.println(jedis.exists("string", "key2"));
		System.out.println(jedis.exists("list"));
		System.out.println(jedis.exists("set"));
		System.out.println(jedis.exists("hash"));
		System.out.println(jedis.exists("zset"));
	}

	@Test
	public void type() {
		// none(key不存在),string(字符串),list(列表),set(集合),zset(有序集),hash(哈希表)
		System.out.println(jedis.type("unknown"));
		System.out.println(jedis.type("string"));
		System.out.println(jedis.type("list"));
		System.out.println(jedis.type("set"));
		System.out.println(jedis.type("hash"));
		System.out.println(jedis.type("zset"));
	}

	@Test
	public void del() {
		Common.print(jedis);
		jedis.del("string");
		Common.print(jedis);
		jedis.del("list", "set");
		Common.print(jedis);
	}

	@Test
	public void flushDB() {
		// 刷新当前DB
		jedis.select(14);
		jedis.set("key", "value");
		Common.print(jedis);
		jedis.select(15);
		jedis.set("key", "value");
		Common.print(jedis);
		jedis.flushDB();
		jedis.select(14);
		Common.print(jedis);
		jedis.select(15);
		Common.print(jedis);
	}

	@Test
	public void flushAll() {
		// 刷新所有DB,谨慎操作
		jedis.select(14);
		jedis.set("key", "value");
		Common.print(jedis);
		jedis.select(15);
		jedis.set("key", "value");
		Common.print(jedis);
//		jedis.flushAll();
		jedis.select(14);
		Common.print(jedis);
		jedis.select(15);
		Common.print(jedis);
	}

	@Test
	public void notice() {
		System.out.println();
		System.out.println("----------");
		System.out.println("jedis.flushDB(); 是刷新当前库, 当前库所有key都会被清空");
		System.out.println("jedis.flushALL(); 是刷新所有库, 所有库所有key都会被清空, 需谨慎使用");
	}

}
