package com.mrathena.jedis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

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

	@After
	public void after() {
		jedis.flushDB();
		jedis.disconnect();
	}

	@Test
	public void hset() {
		// HSET key field value
		// 设置 key 指定的哈希集中指定字段的值。/如果 key 指定的哈希集不存在，会创建一个新的哈希集并与 key 关联。
		// 如果字段在哈希集中存在，它将被重写。
		// 1：如果field是一个新的字段
		// 0：如果field原来在map里面已经存在
		System.out.println(jedis.hset("key", "field1", "value1"));
		System.out.println(jedis.hset("key", "field1", "value2"));
		System.out.println(jedis.hset("key", "field3", "value3"));
		System.out.println(jedis.hset("key", "field4", "value4"));
		System.out.println(jedis.hset("key", "field5", "value5"));
		Common.print(jedis);
	}

	@Test
	public void hsetnx() {
		// HSETNX key field value
		// 只在 key 指定的哈希集中不存在指定的字段时，设置字段的值。如果 key 指定的哈希集不存在，会创建一个新的哈希集并与 key 关联。如果字段已存在，该操作无效果。
		// 1：如果字段是个新的字段，并成功赋值
		// 0：如果哈希集中已存在该字段，没有操作被执行
		System.out.println(jedis.hsetnx("key", "field1", "value1"));
		System.out.println(jedis.hsetnx("key", "field1", "value2"));
		System.out.println(jedis.hsetnx("key", "field3", "value3"));
		Common.print(jedis);
	}

	@Test
	public void hmset() {
		// HMSET key field value [field value ...]
		// 设置 key 指定的哈希集中指定字段的值。该命令将重写所有在哈希集中存在的字段。如果 key 指定的哈希集不存在，会创建一个新的哈希集并与 key 关联
		Map<String, String> map = new HashMap<>(4);
		map.put("filed1", "value1");
		map.put("filed2", "value2");
		map.put("filed3", "value3");
		System.out.println(jedis.hmset("key", map));
		Common.print(jedis);
	}

	@Test
	public void hget() {
		// HGET key field
		// 返回 key 指定的哈希集中该字段所关联的值, 当字段不存在或者 key 不存在时返回nil。
		hset();
		System.out.println(jedis.hget("key", "field1"));
		System.out.println(jedis.hget("key", "field2"));
		System.out.println(jedis.hget("key", "field3"));
		Common.print(jedis);
	}

	@Test
	public void hmget() {
		// HMGET key field [field ...]
		// 返回 key 指定的哈希集中指定字段的值。并保持与请求相同的顺序。
		// 对于哈希集中不存在的每个字段，返回 nil 值。因为不存在的keys被认为是一个空的哈希集，对一个不存在的 key 执行 HMGET 将返回一个只含有 nil 值的列表
		hset();
		System.out.println(jedis.hmget("key", "field1", "field2", "field3"));
		System.out.println(jedis.hmget("key2", "field1", "field2", "field3"));
		Common.print(jedis);
	}

	@Test
	public void hgetall() {
		// HGETALL key
		// 返回 key 指定的哈希集中所有的字段和值。返回值中，每个字段名的下一个是它的值，所以返回值的长度是哈希集大小的两倍
		hset();
		System.out.println(jedis.hgetAll("key"));
		System.out.println(jedis.hgetAll("key2"));
		Common.print(jedis);
	}

	@Test
	public void hkeys() {
		// HKEYS key
		// 返回 key 指定的哈希集中所有字段的名字。
		// 返回哈希集中的字段列表，当 key 指定的哈希集不存在时返回空列表。
		hset();
		System.out.println(jedis.hkeys("key"));
		System.out.println(jedis.hkeys("key2"));
		Common.print(jedis);
	}

	@Test
	public void hvals() {
		// HVALS key
		// 返回 key 指定的哈希集中所有字段的值。
		// 返回哈希集中的值的列表，当 key 指定的哈希集不存在时返回空列表。
		hset();
		System.out.println(jedis.hvals("key"));
		System.out.println(jedis.hvals("key2"));
		Common.print(jedis);
	}

	@Test
	public void hlen() {
		// HLEN key
		// 返回 key 指定的哈希集包含的字段的数量。
		// 返回哈希集中字段的数量，当 key 指定的哈希集不存在时返回 0
		hset();
		System.out.println(jedis.hlen("key"));
		System.out.println(jedis.hlen("key2"));
		Common.print(jedis);
	}

	@Test
	public void hexists() {
		// HEXISTS key field
		// 返回hash里面field是否存在
		// 1 hash里面包含该field。
		// 0 hash里面不包含该field或者key不存在。
		hset();
		System.out.println(jedis.hexists("key", "field1"));
		System.out.println(jedis.hexists("key", "field2"));
		System.out.println(jedis.hexists("key", "field3"));
		System.out.println(jedis.hexists("key2", "field1"));
		Common.print(jedis);
	}

	@Test
	public void hdel() {
		// HDEL key field [field ...]
		// 从 key 指定的哈希集中移除指定的域。在哈希集中不存在的域将被忽略。
		// 如果 key 指定的哈希集不存在，它将被认为是一个空的哈希集，该命令将返回0。
		// 返回从哈希集中成功移除的域的数量，不包括指出但不存在的那些域
		hset();
		System.out.println(jedis.hdel("key", "field1"));
		System.out.println(jedis.hdel("key", "field2"));
		System.out.println(jedis.hdel("key", "field3"));
		System.out.println(jedis.hdel("key2", "field1"));
		Common.print(jedis);
	}

	@Test
	public void incr() {
		// HINCRBY key field increment
		// 增加 key 指定的哈希集中指定字段的数值。如果 key 不存在，会创建一个新的哈希集并与 key 关联。如果字段不存在，则字段的值在该操作执行前被设置为 0
		// 返回增值操作执行后的该字段的值。
		// HINCRBYFLOAT key field increment
		// 为指定key的hash的field字段值执行float类型的increment加。如果field不存在，则在执行该操作前设置为0.如果出现下列情况之一，则返回错误：
		// field的值包含的类型错误(不是字符串)。
		// 当前field或者increment不能解析为一个float类型。
		// 此命令的确切行为与INCRBYFLOAT命令相同，请参阅INCRBYFLOAT命令获取更多信息。
		// 可正可负,integer可以转double,反之报错,除非double的小数部分是0
		System.out.println(jedis.hincrBy("key", "field1", 2));
		System.out.println(jedis.hincrBy("key", "field1", -1));
		System.out.println(jedis.hincrByFloat("key", "field1", 3.5));
		System.out.println(jedis.hincrByFloat("key", "field1", -2.2));
		System.out.println(jedis.hincrByFloat("key", "field1", 0.7));
		System.out.println(jedis.hincrBy("key", "field1", 2));
	}

	@Test
	public void hscan() {
		// HSCAN key cursor [MATCH pattern] [COUNT count]
		System.out.println("懒得看了,这个命令的解释太长了");
	}

}
