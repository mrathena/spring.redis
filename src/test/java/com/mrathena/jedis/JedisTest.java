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
	}

	@After
	public void after() {
		// 刷新DB15
		jedis.flushDB();
		jedis.disconnect();
		notice();
	}

	@Test
	public void expire() throws Exception {
		// EXPIRE key seconds
		// 设置key的过期时间，超过时间后，将会自动删除该key。在Redis的术语中一个key的相关超时是不确定的。
		// 过期时间可能会被del,set,getset等操作清楚掉
		System.out.println(jedis.set("key", "value"));
		System.out.println(jedis.expire("key", 1));
		Common.print(jedis);
		Thread.sleep(1000L);
		Common.print(jedis);
		System.out.println(jedis.set("key", "value"));
		System.out.println(jedis.expire("key", 1));
		Thread.sleep(500L);
		System.out.println(jedis.set("key", "newValue"));
		Thread.sleep(1000L);
		Common.print(jedis);
	}

	@Test
	public void persist() throws Exception {
		// PERSIST key
		// 移除给定key的生存时间，将这个 key 从『易失的』(带生存时间 key )转换成『持久的』(一个不带生存时间、永不过期的 key )。
		System.out.println(jedis.set("key", "value"));
		System.out.println(jedis.expire("key", 1));
		System.out.println(jedis.persist("key"));
		Thread.sleep(1500L);
		Common.print(jedis);
	}

	@Test
	public void exist() {
		// EXISTS key [key ...]
		// 返回key是否存在。
		// Since Redis 3.0.3 it is possible to specify multiple keys instead of a single one.
		// In such a case, it returns the total number of keys existing
		jedis.set("string", "value");
		jedis.rpush("list", "value");
		jedis.sadd("set", "value");
		jedis.hset("hash", "key", "value");
		jedis.zadd("zset", 0.1, "value");
		System.out.println(jedis.exists("string"));
		System.out.println(jedis.exists("string", "key2"));
		System.out.println(jedis.exists("list"));
		System.out.println(jedis.exists("set"));
		System.out.println(jedis.exists("hash"));
		System.out.println(jedis.exists("zset"));
	}

	@Test
	public void type() {
		// TYPE key
		// 返回key所存储的value的数据结构类型，它可以返回string, list, set, zset 和 hash等不同的类型。
		// none(key不存在),string(字符串),list(列表),set(集合),zset(有序集),hash(哈希表)
		jedis.set("string", "value");
		jedis.rpush("list", "value");
		jedis.sadd("set", "value");
		jedis.hset("hash", "key", "value");
		jedis.zadd("zset", 0.1, "value");
		System.out.println(jedis.type("unknown"));
		System.out.println(jedis.type("string"));
		System.out.println(jedis.type("list"));
		System.out.println(jedis.type("set"));
		System.out.println(jedis.type("hash"));
		System.out.println(jedis.type("zset"));
	}

	@Test
	public void del() {
		// DEL key [key ...]
		// 删除指定的一批keys，如果删除中的某些key不存在，则直接忽略。
		Common.print(jedis);
		jedis.del("string");
		Common.print(jedis);
		jedis.del("list", "set");
		Common.print(jedis);
	}

	@Test
	public void flushDB() {
		// FLUSHDB
		// 删除当前数据库里面的所有数据。这个命令永远不会出现失败。这个操作的时间复杂度是O(N),N是当前数据库的keys数量。
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
		// FLUSHALL
		// 删除所有数据库里面的所有数据，注意不是当前数据库，而是所有数据库。这个命令永远不会出现失败。这个操作的时间复杂度是O(N),N是数据库的数量。
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
