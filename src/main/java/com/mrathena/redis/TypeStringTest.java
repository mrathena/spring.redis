package com.mrathena.redis;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

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

		// setnx,setex,psetex三个命令可能后面被移除,使用下面4种方式替代
		// 存在才set
		jedis.set("key", "value", SetParams.setParams().xx());
		// 不存在才set
		jedis.set("key", "value", SetParams.setParams().nx());
		jedis.set("key", "value", SetParams.setParams().ex(1).nx());
		jedis.set("key", "value", SetParams.setParams().px(1000).nx());
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


		// jedis操作Object(序列化/反序列化)
		@Getter
		@Setter
		@ToString
		@Accessors(chain = true)
		class User implements Serializable {
			private static final long serialVersionUID = 1L;
			private String username;
			private String nickname;
			private String password;
		}

		User user = new User().setUsername("胡诗瑞").setNickname("mrathena").setPassword("胡诗瑞胡诗瑞胡诗瑞胡诗瑞胡诗瑞胡诗瑞胡诗瑞胡诗瑞");

		byte[] key = serialize("user");

		System.out.println(user);
		jedis.set(key, serialize(user));
		System.out.println(deSerialize(jedis.get(key), User.class));
		System.out.println();

		List<User> users = new LinkedList<>();
		for (int i = 0; i < 1000; i++) {
			users.add(clone(user));
		}
		users.add(user);

		System.out.println(users);
		jedis.set(key, serialize(users));
		System.out.println(deSerialize(jedis.get(key), List.class));
		List<User> list = deSerialize(jedis.get(key), List.class);
		System.out.println(list.get(0));
		System.out.println();


		// 序列化性能对比
		// 序列化后长度对比
		System.out.println(serialize(users).length);
		System.out.println(JSON.toJSONString(users).getBytes().length);
		System.out.println();
		// 序列化时间对比,10次取平均时间
		long total = 0;
		for (int i = 0; i < 10; i++) {
			long start = System.nanoTime();
			serialize(users);
			long interval = System.nanoTime() - start;
			total = total + interval;
		}
		System.out.println(total / 10);
		total = 0;
		for (int i = 0; i < 10; i++) {
			long start = System.nanoTime();
			JSON.toJSONString(users).getBytes();
			long interval = System.nanoTime() - start;
			total = total + interval;
		}
		System.out.println(total / 10);


		jedis.flushAll();
		Common.print(jedis);

		jedis.disconnect();

	}

	/**
	 * 序列化
	 */
	public static byte[] serialize(Object object) throws Exception {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			 ObjectOutputStream oos = new ObjectOutputStream(baos)
		) {
			oos.writeObject(object);
			oos.flush();
			return baos.toByteArray();
		}
	}

	/**
	 * 反序列化
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deSerialize(byte[] bytes, Class<T> clazz) throws Exception {
		T object = null;
		try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
			object = (T) ois.readObject();
			return object;
		} catch (EOFException e) {
			// EOFException仅仅是流到达最末尾的标记,捕获该异常后,直接处理之前的数据就好了,无需处理该异常
			return object;
		}
	}

	/**
	 * 克隆对象(深复制)
	 * 要求被克隆对象必须实现Serializable
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T clone(T object) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(object);
			try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
				 ObjectInputStream ois = new ObjectInputStream(bais)) {
				return (T) ois.readObject();
			} catch (IOException | ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
