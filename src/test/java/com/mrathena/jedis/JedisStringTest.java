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
		this.jedis = new Jedis(Common.HOST, Common.PORT);
		jedis.connect();
		jedis.auth(Common.PASSWORD);
		// 使用DB15号库来测试,不影响DB0的数据
		jedis.select(Common.DATABASE);
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
		jedis.flushDB();
		jedis.set("key1", "value1");
		jedis.set("key1", "value2");
		jedis.set("key2", "value2");
		Common.print(jedis);
	}

	@Test
	public void setnx() {
		// NX -- Only set the key if it does not already exist.
		jedis.flushDB();
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
		jedis.flushDB();
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
		jedis.flushDB();
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
		jedis.flushDB();
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
		jedis.flushDB();
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
		jedis.flushDB();
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
		jedis.flushDB();
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
		jedis.flushDB();
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
		jedis.flushDB();
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
	public void del() {
		set();
		jedis.del("key1");
		Common.print(jedis);
		set();
		jedis.del("key1", "key2");
		Common.print(jedis);
	}

	@Test
	public void incr() {
		jedis.flushDB();
		jedis.incr("key");
		Common.print(jedis);
		jedis.incrBy("key", 10);
		Common.print(jedis);
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
	public void type() {
		// none(key不存在),string(字符串),list(列表),set(集合),zset(有序集),hash(哈希表)
		incr();
		Common.print(jedis);
		System.out.println(jedis.type("key"));
		System.out.println(jedis.type("unknown"));
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
