package com.mrathena.jedis;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author mrathena on 2019-08-11 01:13
 */
@Slf4j
public class JedisStringTest {

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
	public void print() {
		Common.print(jedis);
	}

	@Test
	public void set() {
		jedis.set("key1", "value1");
		jedis.set("key1", "value2");
		jedis.set("key2", "value2");
		Common.print(jedis);
	}

	@Test
	public void get() {
		jedis.set("key", "value");
		System.out.println(jedis.get("key"));
	}

	@Test
	public void setnx() {
		// NX -- Only set the key if it does not already exist.
		System.out.println(jedis.setnx("key", "value"));
		Common.print(jedis);
		System.out.println(jedis.setnx("key", "value2"));
		Common.print(jedis);
	}

	@Test
	public void setnx2() {
		// jedis 2.9.3
		// NX -- Only set the key if it does not already exist.
		// XX -- Only set the key if it already exist.
		jedis.set("key", "value", "nx");
		Common.print(jedis);
		jedis.set("key", "value2", "nx");
		Common.print(jedis);
	}

	@Test
	public void setxx() {
		// jedis 2.9.3
		// NX -- Only set the key if it does not already exist.
		// XX -- Only set the key if it already exist.
		jedis.set("key", "value", "xx");
		Common.print(jedis);
		jedis.set("key", "value");
		Common.print(jedis);
		jedis.set("key", "value2", "xx");
		Common.print(jedis);
	}

	@Test
	public void setex() throws Exception {
		// EX seconds -- Set the specified expire time, in seconds.
		jedis.setex("key", 1, "value");
		Common.print(jedis);
		Thread.sleep(500);
		Common.print(jedis);
		Thread.sleep(500);
		Common.print(jedis);
	}

	@Test
	public void psetex() throws Exception {
		// PX milliseconds -- Set the specified expire time, in milliseconds.
		jedis.psetex("key", 1000L, "value");
		Common.print(jedis);
		Thread.sleep(500);
		Common.print(jedis);
		Thread.sleep(500);
		Common.print(jedis);
	}

	@Test
	public void setnxex() throws Exception {
		// jedis 2.9.3
		// NX -- Only set the key if it does not already exist.
		// XX -- Only set the key if it already exist.
		// EX seconds -- Set the specified expire time, in seconds.
		// PX milliseconds -- Set the specified expire time, in milliseconds.
		jedis.set("key", "value");
		Common.print(jedis);
		jedis.set("key", "value2", "nx", "ex", 1L);
		Common.print(jedis);
		jedis.del("key");
		Common.print(jedis);
		jedis.set("key", "value2", "nx", "ex", 1);
		Common.print(jedis);
		Thread.sleep(500);
		Common.print(jedis);
		Thread.sleep(500);
		Common.print(jedis);
	}

	@Test
	public void setnxpx() throws Exception {
		// jedis 2.9.3
		// NX -- Only set the key if it does not already exist.
		// XX -- Only set the key if it already exist.
		// EX seconds -- Set the specified expire time, in seconds.
		// PX milliseconds -- Set the specified expire time, in milliseconds.
		jedis.set("key", "value");
		Common.print(jedis);
		jedis.set("key", "value2", "nx", "px", 1000L);
		Common.print(jedis);
		jedis.del("key");
		Common.print(jedis);
		jedis.set("key", "value2", "nx", "px", 1000);
		Common.print(jedis);
		Thread.sleep(500);
		Common.print(jedis);
		Thread.sleep(500);
		Common.print(jedis);
	}

	@Test
	public void setxxex() throws Exception {
		// jedis 2.9.3
		// NX -- Only set the key if it does not already exist.
		// XX -- Only set the key if it already exist.
		// EX seconds -- Set the specified expire time, in seconds.
		// PX milliseconds -- Set the specified expire time, in milliseconds.
		jedis.set("key", "value", "xx", "ex", 1);
		Common.print(jedis);
		jedis.set("key", "value");
		Common.print(jedis);
		jedis.set("key", "value2", "xx", "ex", 1);
		Common.print(jedis);
		Thread.sleep(500);
		Common.print(jedis);
		Thread.sleep(500);
		Common.print(jedis);
	}

	@Test
	public void setxxpx() throws Exception {
		// jedis 2.9.3
		// NX -- Only set the key if it does not already exist.
		// XX -- Only set the key if it already exist.
		// EX seconds -- Set the specified expire time, in seconds.
		// PX milliseconds -- Set the specified expire time, in milliseconds.
		jedis.set("key", "value", "xx", "px", 1000);
		Common.print(jedis);
		jedis.set("key", "value");
		Common.print(jedis);
		jedis.set("key", "value2", "xx", "px", 1000);
		Common.print(jedis);
		Thread.sleep(500);
		Common.print(jedis);
		Thread.sleep(500);
		Common.print(jedis);
	}

	@Test
	public void incr() {
		// 可正可负,integer可以转double,反之报错,除非double的小数部分是0
		System.out.println(jedis.incr("key"));
		System.out.println(jedis.incrBy("key", 3));
		System.out.println(jedis.incrBy("key", -2));
		System.out.println(jedis.incrByFloat("key", 2.2D));
		System.out.println(jedis.incrByFloat("key", -0.2D));
		System.out.println(jedis.incrBy("key", 1));
	}

	@Test
	public void decr() {
		incr();
		jedis.decr("key");
		Common.print(jedis);
		jedis.decrBy("key", 8);
		Common.print(jedis);
	}

	@Test
	public void notice() {
		System.out.println();
		System.out.println("----------");
		System.out.println("setnx,setex,psetex 三个命令可能后面被移除,使用下面方式替代");
		System.out.println("jedis 2.9.3");
		System.out.println("jedis.set(\"key\", \"value\", \"nx\");");
		System.out.println("jedis.set(\"key\", \"value\", \"xx\");");
		System.out.println("jedis.set(\"key\", \"value\", \"nx\", \"ex\", 1L);");
		System.out.println("jedis.set(\"key\", \"value\", \"nx\", \"px\", 1000L);");
		System.out.println("jedis.set(\"key\", \"value\", \"xx\", \"ex\", 1L);");
		System.out.println("jedis.set(\"key\", \"value\", \"xx\", \"px\", 1000L);");
		System.out.println("jedis 3.1.0");
		System.out.println("jedis.set(\"key\", \"value\", SetParams.setParams().nx());");
		System.out.println("jedis.set(\"key\", \"value\", SetParams.setParams().xx());");
		System.out.println("jedis.set(\"key\", \"value\", SetParams.setParams().ex(1L));");
		System.out.println("jedis.set(\"key\", \"value\", SetParams.setParams().px(1000L));");
		System.out.println("jedis.set(\"key\", \"value\", SetParams.setParams().nx().ex(1L));");
		System.out.println("jedis.set(\"key\", \"value\", SetParams.setParams().nx().px(1000L));");
		System.out.println("jedis.set(\"key\", \"value\", SetParams.setParams().xx().ex(1L));");
		System.out.println("jedis.set(\"key\", \"value\", SetParams.setParams().xx().px(1000L));");
	}

}
