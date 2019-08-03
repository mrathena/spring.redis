package com.mrathena.redis;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * @author mrathena on 2019/8/2 16:34
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.redis.standalone.xml")
public class StandaloneTest {

	private static final String K = "MRATHENA:SPRING.REDIS:TEST";

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	private ValueOperations<String, Object> stringObjectValueOperations;
	private ValueOperations<String, String> stringStringValueOperations;

	private User user;
	private List<User> userList;
	private Set<User> userSet;
	private Map<String, User> userMap;

	@Before
	public void beforeSerialize() {
		// ValueOperations
		stringObjectValueOperations = redisTemplate.opsForValue();
		stringStringValueOperations = stringRedisTemplate.opsForValue();
		// 数据
		User user = new User(new Name("u", "n"), 20, true);
		User user2 = new User(new Name("u2", "n2"), 22, true);
		User user3 = new User(new Name("u3", "n3"), 23, false);
		this.user = user;
		List<User> userList = new LinkedList<>();
		userList.add(user);
		userList.add(user3);
		this.userList = userList;
		Set<User> userSet = new HashSet<>();
		userSet.add(user2);
		userSet.add(user3);
		this.userSet = userSet;
		Map<String, User> userMap = new HashMap<>(4);
		userMap.put("我是谁", user);
		userMap.put("我管你", user2);
		this.userMap = userMap;
	}

	@Test
	public void test() {
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
	}

	@Test
	public void JdkSerializationRedisSerializer() {
		System.out.println("----------------------------------------------------------------------------------------------------");
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		stringObjectValueOperations.set(K, user);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		User dUser = (User) stringObjectValueOperations.get(K);
		System.out.println(dUser);
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, userList);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		List<User> dUserList = (List<User>) stringObjectValueOperations.get(K);
		System.out.println(dUserList);
		System.out.println();
		stringObjectValueOperations.set(K, userSet);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		Set<User> dUserSet = (Set<User>) stringObjectValueOperations.get(K);
		System.out.println(dUserSet);
		System.out.println();
		stringObjectValueOperations.set(K, userMap);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		Map<String, User> dUserMap = (Map<String, User>) stringObjectValueOperations.get(K);
		System.out.println(dUserMap);
		System.out.println();
		stringObjectValueOperations.set(K, "你好啊");
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
		// org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
		// 强制需要实现序列化,直接看redis不太方便,据说占的空间会大一点 TODO 尝试看redis中能否获取size
		// 用StringRedisTemplate取RedisTemplate存的序列化为非String的数据,也能取出来,但是乱七八糟的,还挺长
		// 不支持自增自减操作
	}

	@Test
	public void StringRedisSerializer() {
		// RedisTemplate默认序列化器为org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
		// Bean配置了默认序列化器为org.springframework.data.redis.serializer.StringRedisSerializer
		stringObjectValueOperations.set(K, "你好啊");
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
		stringStringValueOperations.set(K, "0.0,明显我不好啊!!!");
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, "1");
		stringObjectValueOperations.increment(K);
		System.out.println(stringObjectValueOperations.get(K));
		// org.springframework.data.redis.serializer.StringRedisSerializer
		// 只支持String类型,用起来并不方便,需要手动把value转成string,不然set和get都会类转换异常
		// RedisTemplate和StringRedisTemplate数据是互通的,想想也是通的,网上都是胡说八道
		// 支持自增自减操作
	}

	@Test
	public void GenericToStringSerializer() {
		// GenericToStringSerializer<User> userGenericToStringSerializer = new GenericToStringSerializer<>(User.class);
		// userGenericToStringSerializer.setTypeConverter(new TypeConverter(););
		// 需要实现org.springframework.beans.TypeConverter类型的类型转换器,用于把User和String互相转换
		// redisTemplate.setValueSerializer(userGenericToStringSerializer);
		// stringObjectValueOperations.set(K, user);
		// System.out.println(stringObjectValueOperations.get(K));
		// org.springframework.data.redis.serializer.GenericToStringSerializer
		// GenericToStringSerializer,可自行实现普通类型和String的互相转换,一般几乎不会使用把
	}

	@Test
	public void GenericJackson2JsonRedisSerializer() {
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		stringObjectValueOperations.set(K, 123);
		stringObjectValueOperations.increment(K);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, user);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		User dUser = (User) stringObjectValueOperations.get(K);
		System.out.println(dUser);
		System.out.println();
		stringObjectValueOperations.set(K, userList);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		List<User> dUserList = (List<User>) stringObjectValueOperations.get(K);
		System.out.println(dUserList);
		System.out.println();
		stringObjectValueOperations.set(K, userSet);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		Set<User> dUserSet = (Set<User>) stringObjectValueOperations.get(K);
		System.out.println(dUserSet);
		System.out.println();
		stringObjectValueOperations.set(K, userMap);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		Map<String, User> dUserMap = (Map<String, User>) stringObjectValueOperations.get(K);
		System.out.println(dUserMap);
		System.out.println();
		stringObjectValueOperations.set(K, "你好啊");
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		String string = (String) stringObjectValueOperations.get(K);
		System.out.println(string);
		// org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
		// 支持自增自减操作
		// 序列化为json,而且包含数据类型,取出来的直接就是对象,不需要自己反序列化,非常方便,强烈推荐
		// {"@class":"com.mrathena.redis.User","name":{"@class":"com.mrathena.redis.Name","username":"u","nickname":"n"},"age":20,"sex":true}
	}

	@Test
	public void Jackson2JsonRedisSerializer() {
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
		stringObjectValueOperations.set(K, 123);
		stringObjectValueOperations.increment(K);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, user);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		User dUser = (User) stringObjectValueOperations.get(K);
		System.out.println(dUser);
		System.out.println();
		stringObjectValueOperations.set(K, userList);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		List<User> dUserList = (List<User>) stringObjectValueOperations.get(K);
		System.out.println(dUserList);
		System.out.println();
		stringObjectValueOperations.set(K, userSet);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		Set<User> dUserSet = (Set<User>) stringObjectValueOperations.get(K);
		System.out.println(dUserSet);
		System.out.println();
		stringObjectValueOperations.set(K, userMap);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		Map<String, User> dUserMap = (Map<String, User>) stringObjectValueOperations.get(K);
		System.out.println(dUserMap);
		System.out.println();
		stringObjectValueOperations.set(K, "你好啊");
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		String string = (String) stringObjectValueOperations.get(K);
		System.out.println(string);
	}

	@Test
	public void 测试两者数据是否通用() {
		// 是通用的,默认都是0号数据库
		stringStringValueOperations.set(K, "test");
		System.out.println(stringObjectValueOperations.get(K));
		stringObjectValueOperations.set(K, "test");
		System.out.println(stringStringValueOperations.get(K));
	}

	@Test
	public void 测试序列化() {

	}

}
