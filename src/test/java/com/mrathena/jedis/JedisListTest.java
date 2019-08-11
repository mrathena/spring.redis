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
		// LPUSH key value [value ...]
		// 将所有指定的值插入到存于 key 的列表的头部。如果 key 不存在，那么在进行 push 操作前会创建一个空列表。
		// 如果 key 对应的值不是一个 list 的话，那么会返回一个错误。
		// 可以使用一个命令把多个元素 push 进入列表，只需在命令末尾加上多个指定的参数。
		// 元素是从最左端的到最右端的、一个接一个被插入到 list 的头部。
		// 所以对于这个命令例子 LPUSH list a b c，返回的列表是 c 为第一个元素， b 为第二个元素， a 为第三个元素。
		// RPUSH key value [value ...]
		// 向存于 key 的列表的尾部插入所有指定的值。如果 key 不存在，那么会创建一个空的列表然后再进行 push 操作。
		// 当 key 保存的不是一个列表，那么会返回一个错误。
		// 可以使用一个命令把多个元素打入队列，只需要在命令后面指定多个参数。元素是从左到右一个接一个从列表尾部插入。
		// 比如命令 RPUSH list a b c 会返回一个列表，其第一个元素是 a ，第二个元素是 b ，第三个元素是 c。
		jedis.lpush("key", "1");
		jedis.rpush("key", "2");
		jedis.lpush("key", "3");
		jedis.rpush("key", "4");
		jedis.lpush("key", "6", "6");
		Common.print(jedis);
	}

	@Test
	public void pushx() {
		// LPUSHX key value
		// 只有当 key 已经存在并且存着一个 list 的时候，在这个 key 下面的 list 的头部插入 value。
		// 与 LPUSH 相反，当 key 不存在的时候不会进行任何操作。
		// RPUSHX key value
		// 将值 value 插入到列表 key 的表尾, 当且仅当 key 存在并且是一个列表。
		// 和 RPUSH 命令相反, 当 key 不存在时，RPUSHX 命令什么也不做。
		push();
		System.out.println(jedis.lpushx("key", "7", "7"));
		Common.print(jedis);
		jedis.del("key");
		System.out.println(jedis.rpushx("key", "7", "7"));
		Common.print(jedis);
	}

	@Test
	public void pop() {
		// LPOP key
		// 移除并且返回 key 对应的 list 的第一个元素。
		// 返回第一个元素的值，或者当 key 不存在时返回 nil。
		// RPOP key
		// 移除并返回存于 key 的 list 的最后一个元素。
		// 最后一个元素的值，或者当 key 不存在的时候返回 nil。
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
		// LINDEX key index
		// 返回列表里的元素的索引 index 存储在 key 里面。
		// 下标是从0开始索引的，所以 0 是表示第一个元素， 1 表示第二个元素，并以此类推。
		// 负数索引用于指定从列表尾部开始索引的元素。在这种方法下，-1 表示最后一个元素，-2 表示倒数第二个元素，并以此往前推。
		// 当 key 位置的值不是一个列表的时候，会返回一个error。
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
		// LSET key index value
		// 设置 index 位置的list元素的值为 value。 更多关于 index 参数的信息，详见 LINDEX。
		// 当index超出范围时会返回一个error。
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
		// LINSERT key BEFORE|AFTER pivot value
		// 把 value 插入存于 key 的列表中在基准值 pivot 的前面或后面。
		// 当 key 不存在时，这个list会被看作是空list，任何操作都不会发生。
		// 当 key 存在，但保存的不是一个list的时候，会返回error。
		// 返回经过插入操作后的list长度，或者当 pivot 值找不到的时候返回 -1。
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
		// LTRIM key start stop
		// 修剪(trim)一个已存在的 list，这样 list 就会只包含指定范围的指定元素。
		// start 和 stop 都是由0开始计数的， 这里的 0 是列表里的第一个元素（表头），1 是第二个元素，以此类推。
		// 例如： LTRIM foobar 0 2 将会对存储在 foobar 的列表进行修剪，只保留列表里的前3个元素。
		// start 和 end 也可以用负数来表示与表尾的偏移量，比如 -1 表示列表里的最后一个元素， -2 表示倒数第二个，等等。
		// 超过范围的下标并不会产生错误：如果 start 超过列表尾部，或者 start > end，结果会是列表变成空表（即该 key 会被移除）。
		// 如果 end 超过列表尾部，Redis 会将其当作列表的最后一个元素。
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
		// LREM key count value
		// 从存于 key 的列表里移除前 count 次出现的值为 value 的元素。 这个 count 参数通过下面几种方式影响这个操作：
		// count > 0: 从头往尾移除值为 value 的元素。
		// count < 0: 从尾往头移除值为 value 的元素。
		// count = 0: 移除所有值为 value 的元素。
		// 比如， LREM list -2 “hello” 会从存于 list 的列表里移除最后两个出现的 “hello”。
		// 需要注意的是，如果list里没有存在key就会被当作空list处理，所以当 key 不存在的时候，这个命令会返回 0。
		// 返回被移除的元素个数。
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
		// LLEN key
		// 返回存储在 key 里的list的长度。 如果 key 不存在，那么就被看作是空list，并且返回长度为 0。
		// 当存储在 key 里的值不是一个list的话，会返回error。
		push();
		System.out.println(jedis.llen("key"));
		jedis.lpop("key");
		jedis.rpop("key");
		System.out.println(jedis.llen("key"));
	}

	@Test
	public void lrange() {
		// LRANGE key start stop
		// 返回存储在 key 的列表里指定范围内的元素。
		// start 和 end 偏移量都是基于0的下标，即list的第一个元素下标是0（list的表头），第二个元素下标是1，以此类推。
		// 偏移量也可以是负数，表示偏移量是从list尾部开始计数。 例如， -1 表示列表的最后一个元素，-2 是倒数第二个，以此类推。
		// 需要注意的是，如果你有一个list，里面的元素是从0到100，那么 LRANGE list 0 10 这个命令会返回11个元素，即最右边的那个元素也会被包含在内。
		// 在你所使用的编程语言里，这一点可能是也可能不是跟那些求范围有关的函数都是一致的。（像Ruby的 Range.new，Array#slice 或者Python的 range() 函数。）
		// 当下标超过list范围的时候不会产生error。 如果start比list的尾部下标大的时候，会返回一个空列表。
		// 如果stop比list的实际尾部大的时候，Redis会当它是最后一个元素的下标。
		push();
		System.out.println(jedis.lrange("key", 1, 3));
		Common.print(jedis);
		System.out.println(jedis.lrange("key", 0, -1));
	}

	@Test
	public void rpoplpush() {
		// RPOPLPUSH source destination
		// 原子性地返回并移除存储在 source 的列表的最后一个元素（列表尾部元素）， 并把该元素放入存储在 destination 的列表的第一个元素位置（列表头部）。
		// 例如：假设 source 存储着列表 a,b,c， destination存储着列表 x,y,z。
		// 执行 RPOPLPUSH 得到的结果是 source 保存着列表 a,b ，而 destination 保存着列表 c,x,y,z。
		// 如果 source 不存在，那么会返回 nil 值，并且不会执行任何操作。
		// 如果 source 和 destination 是同样的，那么这个操作等同于移除列表最后一个元素并且把该元素放在列表头部， 所以这个命令也可以当作是一个旋转列表的命令。

	}

}
