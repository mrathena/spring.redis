package com.mrathena.jedis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author mrathena on 2019-08-11 01:22
 */
public class JedisSetTest {

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
	public void sadd() {
		// SADD key member [member ...]
		// 添加一个或多个指定的member元素到集合的 key中.指定的一个或者多个元素member 如果已经在集合key中存在则忽略.如果集合key 不存在，则新建集合key,并添加member元素到集合key中.
		// 如果key 的类型不是集合则返回错误.
		// 返回新成功添加到集合里元素的数量，不包括已经存在于集合中的元素.
		System.out.println(jedis.sadd("key", "value"));
		Common.print(jedis);
		System.out.println(jedis.sadd("key", "value"));
		Common.print(jedis);
		System.out.println(jedis.sadd("key", "value2", "value3"));
		Common.print(jedis);
	}

	@Test
	public void scard() {
		// SCARD key
		// 返回集合存储的key的基数 (集合元素的数量).
		// 集合的基数(元素的数量),如果key不存在,则返回 0.
		sadd();
		System.out.println(jedis.scard("key"));
		System.out.println(jedis.scard("key2"));
	}

	@Test
	public void sdiff() {
		// SDIFF key [key ...]
		// 返回一个集合与给定集合的差集的元素.
		// 不存在的key认为是空集.
		// sdiff(key1, key2), 返回key1中有但是key2中没有的元素
		// sdiff(key1, key2, key3), 返回key1中有但是key2和key3中没有的元素
		jedis.sadd("key", "a", "b", "c");
		jedis.sadd("key2", "b", "c", "d");
		jedis.sadd("key3", "c", "d", "a");
		System.out.println(jedis.sdiff("key", "key2"));
		System.out.println(jedis.sdiff("key2", "key"));
		System.out.println(jedis.sdiff("key", "key2", "key3"));
		System.out.println(jedis.sdiff("key", "key4"));
	}

	@Test
	public void sdiffstore() {
		// SDIFFSTORE destination key [key ...]
		// 该命令类似于 SDIFF, 不同之处在于该命令不返回结果集，而是将结果存放在destination集合中.
		// 如果destination已经存在, 则将其覆盖重写.
		// 返回结果集元素的个数.
		jedis.sadd("key", "a", "b", "c");
		jedis.sadd("key2", "b", "c", "d");
		System.out.println(jedis.sdiffstore("newKey", "key", "key2"));
		Common.print(jedis);
	}

	@Test
	public void sinter() {
		// SINTER key [key ...]
		// 返回指定所有的集合的成员的交集.
		// 如果key不存在则被认为是一个空的集合,当给定的集合为空的时候,结果也为空.(一个集合为空，结果一直为空).
		jedis.sadd("key", "a", "b", "c");
		jedis.sadd("key2", "b", "c", "d");
		System.out.println(jedis.sinter("key", "key2"));
		System.out.println(jedis.sinter("key", "key3"));
		System.out.println(jedis.sinter("key"));
	}

	@Test
	public void sinterstore() {
		// SINTERSTORE destination key [key ...]
		// 这个命令与SINTER命令类似, 但是它并不是直接返回结果集,而是将结果保存在 destination集合中.
		// 如果destination 集合存在, 则会被重写.
		// 返回结果集中成员的个数.
		jedis.sadd("key", "a", "b", "c");
		jedis.sadd("key2", "b", "c", "d");
		System.out.println(jedis.sinterstore("newKey", "key", "key2"));
		Common.print(jedis);
	}

	@Test
	public void sismember() {
		// SISMEMBER key member
		// 返回成员 member 是否是存储的集合 key的成员.
		// 如果member元素是集合key的成员，则返回1
		// 如果member元素不是key的成员，或者集合key不存在，则返回0
		jedis.sadd("key", "a", "b", "c");
		System.out.println(jedis.sismember("key", "a"));
		System.out.println(jedis.sismember("key", "b"));
		System.out.println(jedis.sismember("key2", "a"));
	}

	@Test
	public void smembers() {
		// SMEMBERS key
		// 返回key集合所有的元素.
		// 该命令的作用与使用一个参数的SINTER 命令作用相同.
		jedis.sadd("key", "a", "b", "c");
		System.out.println(jedis.smembers("key"));
		System.out.println(jedis.smembers("key2"));
	}

	@Test
	public void smove() {
		// SMOVE source destination member
		// 将member从source集合移动到destination集合中.
		// 对于其他的客户端,在特定的时间元素将会作为source或者destination集合的成员出现.
		// 如果source 集合不存在或者不包含指定的元素,这smove命令不执行任何操作并且返回0.
		// 否则对象将会从source集合中移除，并添加到destination集合中去，
		// 如果destination集合已经存在该元素，则smove命令仅将该元素充source集合中移除.
		// 如果source 和destination不是集合类型,则返回错误.
		// 如果该元素成功移除,返回1
		// 如果该元素不是 source集合成员,无任何操作,则返回0.
		jedis.sadd("key", "a", "b", "c");
		System.out.println(jedis.smove("key", "key2", "a"));
		Common.print(jedis);
	}

	@Test
	public void spop() {
		// SPOP key [count]
		// 从存储在key的集合中移除并返回一个或多个随机元素。
		// 此操作与SRANDMEMBER类似，它从一个集合中返回一个或多个随机元素，但不删除元素。
		// count参数将在更高版本中提供，但是在2.6、2.8、3.0中不可用。
		jedis.sadd("key", "a", "b", "c", "d", "e");
		System.out.println(jedis.spop("key"));
		Common.print(jedis);
		System.out.println(jedis.spop("key", 2));
		Common.print(jedis);
	}

	@Test
	public void srandmember() {
		// SRANDMEMBER key [count]
		// 仅提供key参数，那么随机返回key集合中的一个元素.
		// Redis 2.6开始，可以接受 count 参数，如果count是整数且小于元素的个数，返回含有 count 个不同的元素的数组，
		// 如果count是个整数且大于集合中元素的个数时，仅返回整个集合的所有元素，
		// 当count是负数，则会返回一个包含count的绝对值的个数元素的数组，
		// 如果count的绝对值大于元素的个数，则返回的结果集里会出现一个元素出现多次的情况.
		// 仅提供key参数时，该命令作用类似于SPOP命令，不同的是SPOP命令会将被选择的随机元素从集合中移除，而SRANDMEMBER仅仅是返回该随记元素，而不做任何操作.
		// 不使用count 参数的情况下该命令返回随机的元素，如果key不存在则返回nil。
		// 使用count参数,则返回一个随机的元素数组，如果key不存在则返回一个空的数组。
		jedis.sadd("key", "a", "b", "c", "d", "e");
		System.out.println(jedis.srandmember("key"));
		Common.print(jedis);
		System.out.println(jedis.srandmember("key", 2));
		Common.print(jedis);
	}

	@Test
	public void srem() {
		// SREM key member [member ...]
		// 在key集合中移除指定的元素. 如果指定的元素不是key集合中的元素则忽略 如果key集合不存在则被视为一个空的集合，该命令返回0.
		// 如果key的类型不是一个集合,则返回错误.
		// 从集合中移除元素的个数，不包括不存在的成员.
		jedis.sadd("key", "a", "b", "c", "d", "e");
		System.out.println(jedis.srem("key", "b"));
		Common.print(jedis);
		System.out.println(jedis.srem("key", "c"));
		Common.print(jedis);
		System.out.println(jedis.srem("key", "f"));
		Common.print(jedis);
	}

	@Test
	public void sscan() {
		System.out.println("懒得看了,这个命令的解释太长了");
	}

	@Test
	public void sunion() {
		// SUNION key [key ...]
		// 返回给定的多个集合的并集中的所有成员.
		// 不存在的key可以认为是空的集合.
		jedis.sadd("key", "a", "b", "c");
		jedis.sadd("key2", "b", "d");
		System.out.println(jedis.sunion("key", "key2"));
	}

	@Test
	public void sunionstore() {
		// SUNIONSTORE destination key [key ...]
		// 该命令作用类似于SUNION命令,不同的是它并不返回结果集,而是将结果存储在destination集合中.
		// 如果destination 已经存在,则将其覆盖.
		// 返回结果集中元素的个数.
		jedis.sadd("key", "a", "b", "c");
		jedis.sadd("key2", "b", "d");
		System.out.println(jedis.sunionstore("newKey", "key", "key2"));
		Common.print(jedis);
	}

}
