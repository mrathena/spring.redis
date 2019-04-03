package com.mrathena.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

/**
 * @author mrathena on 2019/3/29 15:54
 */
public class TypeStringTest {

	public static Jedis preTest() {
		Jedis jedis = new Jedis(Common.HOST, Common.PORT);
		jedis.connect();

		jedis.set("test", "test");
		jedis.set("test2", "1");

		Common.print(jedis);

		return jedis;
	}

	public static void main(String[] args) throws Exception {

		Jedis jedis = preTest();

		// 存在才set
		jedis.set("key", "value", SetParams.setParams().xx());
		// 不存在才set
		jedis.set("key", "value", SetParams.setParams().nx());
		// set同时1秒过期
		jedis.set("key", "value", SetParams.setParams().ex(1));
		// set同时1000毫秒过期
		jedis.set("key", "value", SetParams.setParams().px(1000));
		jedis.set("key1", "value1");
		jedis.set("key1", "value2");
		jedis.set("key2", "value2");
		Common.print(jedis);

		//将值value关联到key，并将key的生存时间设为seconds(秒)。
		jedis.setex("foo", 1, "foo");
		Common.print(jedis);
		Thread.sleep(1000);
		Common.print(jedis);

		jedis.del("test");
		jedis.del("key1", "key2");
		Common.print(jedis);

		jedis.incr("test2");
		Common.print(jedis);
		jedis.incrBy("test2", 10);
		Common.print(jedis);

		jedis.decr("test2");
		Common.print(jedis);
		jedis.decrBy("test2", 1);
		Common.print(jedis);

		// none(key不存在),string(字符串),list(列表),set(集合),zset(有序集),hash(哈希表)
		String type = jedis.type("test2");
		System.out.println(type);

		jedis.flushAll();
		Common.print(jedis);

		jedis.disconnect();

	}

}
