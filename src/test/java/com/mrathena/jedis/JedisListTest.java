package com.mrathena.jedis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;

/**
 * @author mrathena on 2019-08-11 01:22
 */
public class JedisListTest {

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
	public void push() {
		jedis.lpush("key", "1");
		jedis.rpush("key", "2");
		jedis.lpush("key", "3");
		jedis.rpush("key", "4");
		jedis.lpush("key", "6", "6");
		Common.print(jedis);
	}

	@Test
	public void pushx() {
		// 当key不存在时,不会初始化key
		push();
		System.out.println(jedis.lpushx("key", "7", "7"));
		Common.print(jedis);
		jedis.del("key");
		System.out.println(jedis.rpushx("key", "7", "7"));
		Common.print(jedis);
	}

	@Test
	public void pop() {
		push();
		System.out.println(jedis.lpop("key"));
		Common.print(jedis);
		System.out.println(jedis.rpop("key"));
		Common.print(jedis);
		System.out.println(jedis.lpop("key"));
		Common.print(jedis);
		System.out.println(jedis.rpop("key"));
		Common.print(jedis);
		System.out.println(jedis.lpop("key"));
		Common.print(jedis);
		System.out.println(jedis.rpop("key"));
		Common.print(jedis);
		System.out.println(jedis.lpop("key"));
		Common.print(jedis);
		System.out.println(jedis.rpop("key"));
		Common.print(jedis);
		System.out.println(jedis.rpop("key"));
		Common.print(jedis);
	}

	@Test
	public void lindex() {
		// 0到正无穷:从左往右,-1到负无穷:从右往左
		jedis.rpush("key", "1");
		jedis.rpush("key", "2");
		jedis.rpush("key", "3");
		jedis.rpush("key", "4");
		Common.print(jedis);
		System.out.println(jedis.lindex("key", 0));
		System.out.println(jedis.lindex("key", 1));
		System.out.println(jedis.lindex("key", 2));
		System.out.println(jedis.lindex("key", 3));
		System.out.println(jedis.lindex("key", 4));
		Common.print(jedis);
		System.out.println(jedis.lindex("key", -1));
		System.out.println(jedis.lindex("key", -2));
		System.out.println(jedis.lindex("key", -3));
		System.out.println(jedis.lindex("key", -4));
		System.out.println(jedis.lindex("key", -5));
	}

	@Test
	public void lset() {
		// 替换
		jedis.rpush("key", "1");
		jedis.rpush("key", "2");
		jedis.rpush("key", "3");
		Common.print(jedis);
		jedis.lset("key", 0, "111");
		Common.print(jedis);
		jedis.lset("key", -1, "222");
		Common.print(jedis);
	}

	@Test
	public void linsert() {
		// 在key的元素[1]后面添加元素[666]
		push();
		System.out.println(jedis.linsert("key", BinaryClient.LIST_POSITION.AFTER, "1", "666"));
		Common.print(jedis);
		System.out.println(jedis.linsert("key", BinaryClient.LIST_POSITION.BEFORE, "7", "777"));
		Common.print(jedis);
		System.out.println(jedis.linsert("key", BinaryClient.LIST_POSITION.AFTER, "7", "111"));
		Common.print(jedis);
	}

	@Test
	public void ltrim() {
		// ltrim key start stop
		// 移除list中除给定index范围内的元素,不会移除start和stop坐标的元素
		// 正数:从左往右数,0开始
		// 负数:从右往左数,-1开始
		jedis.rpush("key", "1");
		jedis.rpush("key", "2");
		jedis.rpush("key", "3");
		jedis.rpush("key", "4");
		jedis.rpush("key", "5");
		Common.print(jedis);
		jedis.ltrim("key", 1, -2);
		Common.print(jedis);
	}

	@Test
	public void lrem() {
		// lrem key n value
		// n:正数:移除list中前n次出现的value元素
		// n:负数:移除list中后n此数显的value元素
		// n:0:移除list中所有的value元素
		jedis.rpush("key", "1");
		jedis.rpush("key", "2");
		jedis.rpush("key", "2");
		jedis.rpush("key", "3");
		jedis.rpush("key", "4");
		jedis.rpush("key", "4");
		Common.print(jedis);
		jedis.lrem("key", 2, "2");
		Common.print(jedis);
	}

	@Test
	public void llen() {
		push();
		System.out.println(jedis.llen("key"));
		jedis.lpop("key");
		jedis.rpop("key");
		System.out.println(jedis.llen("key"));
	}

	@Test
	public void lrange() {
		push();
		System.out.println(jedis.lrange("key", 1, 3));
		Common.print(jedis);
		System.out.println(jedis.lrange("key", 0, -1));
	}

	@Test
	public void rpoplpush() {
		System.out.println("懒得看了,这个命令的解释太长了");
	}

}
