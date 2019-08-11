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
		// SET key value [EX seconds] [PX milliseconds] [NX|XX]
		// 将键key设定为指定的“字符串”值。
		// 如果 key 已经保存了一个值，那么这个操作会直接覆盖原来的值，并且忽略原始类型。
		// 当set命令执行成功之后，之前设置的过期时间都将失效
		// 从2.6.12版本开始，redis为SET命令增加了一系列选项:
		// EX seconds – 设置键key的过期时间，单位时秒
		// PX milliseconds – 设置键key的过期时间，单位时毫秒
		// NX – 只有键key不存在的时候才会设置key的值
		// XX – 只有键key存在的时候才会设置key的值
		// 注意: 由于SET命令加上选项已经可以完全取代SETNX, SETEX, PSETEX的功能，所以在将来的版本中，redis可能会不推荐使用并且最终抛弃这几个命令。
		jedis.set("key1", "value1");
		jedis.set("key1", "value2");
		jedis.set("key2", "value2");
		Common.print(jedis);
	}

	@Test
	public void get() {
		// GET key
		// 返回key的value。如果key不存在，返回特殊值nil。如果key的value不是string，就返回错误，因为GET只处理string类型的values。
		jedis.set("key", "value");
		System.out.println(jedis.get("key"));
	}

	@Test
	public void append() {
		// APPEND key value
		// 如果 key 已经存在，并且值为字符串，那么这个命令会把 value 追加到原来值（value）的结尾。
		// 如果 key 不存在，那么它将首先创建一个空字符串的key，再执行追加操作，这种情况 APPEND 将类似于 SET 操作。
		jedis.set("key", "value");
		System.out.println(jedis.append("key", "111"));
		System.out.println(jedis.append("key2", "value"));
		Common.print(jedis);
	}

	@Test
	public void mset() {
		// MSET key value [key value ...]
		// 对应给定的keys到他们相应的values上。MSET会用新的value替换已经存在的value，就像普通的SET命令一样。
		// 如果你不想覆盖已经存在的values，请参看命令MSETNX。
		// MSET是原子的，所以所有给定的keys是一次性set的。客户端不可能看到这种一部分keys被更新而另外的没有改变的情况。
		System.out.println(jedis.mset("key", "value", "key2", "value2", "key3", "value3"));
		Common.print(jedis);
		System.out.println(jedis.mset("key", "value5", "key4", "value4"));
		Common.print(jedis);
	}

	@Test
	public void mget() {
		// MGET key [key ...]
		// 返回所有指定的key的value。对于每个不对应string或者不存在的key，都返回特殊值nil。正因为此，这个操作从来不会失败。
		System.out.println(jedis.mset("key", "value", "key2", "value2", "key3", "value3"));
		Common.print(jedis);
		System.out.println(jedis.mget("key", "key2", "key4"));
	}

	@Test
	public void getset() {
		// GETSET key value
		// 自动将key对应到value并且返回原来key对应的value。如果key存在但是对应的value不是字符串，就返回错误。
		System.out.println(jedis.set("key", "value"));
		System.out.println(jedis.getSet("key", "value2"));
		Common.print(jedis);
	}

	@Test
	public void setnx() {
		// SETNX key value
		// 将key设置值为value，如果key不存在，这种情况下等同SET命令。 当key存在时，什么也不做。SETNX是”SET if Not eXists”的简写。
		// 1 如果key被设置了
		// 0 如果key没有被设置
		System.out.println(jedis.setnx("key", "value"));
		Common.print(jedis);
		System.out.println(jedis.setnx("key", "value2"));
		Common.print(jedis);
	}

	@Test
	public void msetnx() {
		// MSETNX key value [key value ...]
		// 对应给定的keys到他们相应的values上。只要有一个key已经存在，MSETNX一个操作都不会执行。
		// 由于这种特性，MSETNX可以实现要么所有的操作都成功，要么一个都不执行，这样可以用来设置不同的key，来表示一个唯一的对象的不同字段。
		// MSETNX是原子的，所以所有给定的keys是一次性set的。客户端不可能看到这种一部分keys被更新而另外的没有改变的情况。
		System.out.println(jedis.msetnx("key", "value", "key2", "value2", "key3", "value3"));
		Common.print(jedis);
		System.out.println(jedis.msetnx("key", "value5", "key4", "value4"));
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
		// SETEX key seconds value
		// 设置key对应字符串value，并且设置key在给定的seconds时间之后超时过期。这个命令等效于执行下面的命令：
		// SET key value
		// EXPIRE key seconds
		// SETEX是原子的，也可以通过把上面两个命令放到MULTI/EXEC块中执行的方式重现。相比连续执行上面两个命令，它更快，因为当Redis当做缓存使用时，这个操作更加常用。
		jedis.setex("key", 1, "value");
		Common.print(jedis);
		Thread.sleep(500);
		Common.print(jedis);
		Thread.sleep(500);
		Common.print(jedis);
	}

	@Test
	public void psetex() throws Exception {
		// PSETEX和SETEX一样，唯一的区别是到期时间以毫秒为单位,而不是秒。
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
	public void strlen() {
		// STRLEN key
		// 返回key的string类型value的长度。如果key对应的非string类型，就返回错误。
		// key对应的字符串value的长度，或者0（key不存在）
		jedis.set("key", "value");
		System.out.println(jedis.strlen("key"));
		System.out.println(jedis.strlen("key2"));
	}

	@Test
	public void setrange() {
		// SETRANGE key offset value
		// 这个命令的作用是覆盖key对应的string的一部分，从指定的offset处开始，覆盖value的长度。
		// 如果offset比当前key对应string还要长，那这个string后面就补0以达到offset。
		// 不存在的keys被认为是空字符串，所以这个命令可以确保key有一个足够大的字符串，能在offset处设置value。
		// 注意，offset最大可以是2的29次方-1(536870911),因为redis字符串限制在512M大小。如果你需要超过这个大小，你可以用多个keys。
		// 返回该命令修改后的字符串长度
		System.out.println(jedis.set("key", "Hello World"));
		System.out.println(jedis.setrange("key", 6 , "Redis"));
		Common.print(jedis);
		System.out.println(jedis.setrange("key2", 6 , "Redis"));
		Common.print(jedis);
	}

	@Test
	public void getrange() {
		// GETRANGE key start end
		// 这个命令是被改成GETRANGE的，在小于2.0的Redis版本中叫SUBSTR。
		// 返回key对应的字符串value的子串，这个子串是由start和end位移决定的（两者都在string内）。
		// 可以用负的位移来表示从string尾部开始数的下标。所以-1就是最后一个字符，-2就是倒数第二个，以此类推。
		// 这个函数处理超出范围的请求时，都把结果限制在string内。
		System.out.println(jedis.set("key", "Hello World"));
		System.out.println(jedis.getrange("key", 0, 5));
		System.out.println(jedis.getrange("key", 3, 8));
		System.out.println(jedis.getrange("key", 0, -1));
		System.out.println(jedis.getrange("key", 2, -2));
		System.out.println(jedis.getrange("key", 6, 100));
	}

	@Test
	public void incr() {
		// INCR key
		// 对存储在指定key的数值执行原子的加1操作。
		// 如果指定的key不存在，那么在执行incr操作之前，会先将它的值设定为0。
		// 如果指定的key中存储的值不是字符串类型（fix：）或者存储的字符串类型不能表示为一个整数，
		// 那么执行这个命令时服务器会返回一个错误(eq:(error) ERR value is not an integer or out of range)。
		// INCRBY key increment
		// 将key对应的数字加decrement。如果key不存在，操作之前，key就会被置为0。
		// 如果key的value类型错误或者是个不能表示成数字的字符串，就返回错误。这个操作最多支持64位有符号的正型数字。
		// INCRBYFLOAT key increment
		// 通过指定浮点数key来增长浮点数(存放于string中)的值. 当键不存在时,先将其值设为0再操作.下面任一情况都会返回错误:
		// key 包含非法值(不是一个string).
		// 当前的key或者相加后的值不能解析为一个双精度的浮点值.(超出精度范围了)
		// 如果操作命令成功, 相加后的值将替换原值存储在对应的键值上, 并以string的类型返回.
		// string中已存的值或者相加参数可以任意选用指数符号,但相加计算的结果会以科学计数法的格式存储.
		// 无论各计算的内部精度如何, 输出精度都固定为小数点后17位.

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
		// DECR key
		// 对key对应的数字做减1操作。如果key不存在，那么在操作之前，这个key对应的值会被置为0。
		// 如果key有一个错误类型的value或者是一个不能表示成数字的字符串，就返回错误。
		// DECRBY key decrement
		// 将key对应的数字减decrement。如果key不存在，操作之前，key就会被置为0。
		// 如果key的value类型错误或者是个不能表示成数字的字符串，就返回错误。
		incr();
		jedis.decr("key");
		Common.print(jedis);
		jedis.decrBy("key", 8);
		Common.print(jedis);
	}

	@Test
	public void bit() {
		// BITCOUNT key [start end]
		// BITFIELD key [GET type offset] [SET type offset value] [INCRBY type offset increment] [OVERFLOW WRAP|SAT|FAIL]
		// BITOP operation destkey key [key ...]
		// BITPOS key bit [start] [end]
		// GETBIT key offset
		// SETBIT key offset value
		System.out.println("与BIT相关的懒得看了");
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
