package com.mrathena.redis;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrathena.spring.redis.serializer.CustomFastJsonRedisSerializer;
import com.mrathena.spring.redis.serializer.CustomKryoRedisSerializer;
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
public class RedisStandaloneTest {

	private static final String K = "MRATHENA:SPRING.REDIS:TEST";

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	private ValueOperations<String, Object> stringObjectValueOperations;
	private ValueOperations<String, String> stringStringValueOperations;

	private Data data;
	private List<Data> dataList;
	private Set<Data> dataSet;
	private Map<String, Data> dataMap;

	@Before
	public void beforeSerialize() {
		// ValueOperations
		stringObjectValueOperations = redisTemplate.opsForValue();
		stringStringValueOperations = stringRedisTemplate.opsForValue();
		// 数据
		Data data = new Data("s", new O("u", "n"), null, (byte) 1, (short) 2, 3, 4L, 1.1F, 1.2D, 'a', true, (byte) 5, (short) 6, 7, 8L, 2.3F, 2.2D, 'B', true);
		Data data2 =
				new Data("s", new O("u2", "n2"), null, (byte) 2, (short) 3, 4, 5L, 2.2F, 3.3D, 'b', true, (byte) 6, (short) 7, 8, 9L, 4.4F, 5.5D, 'C', false);
		Data data3 =
				new Data("s", new O("u3", "n3"), null, (byte) 3, (short) 4, 5, 6L, 3.3F, 4.4D, 'c', true, (byte) 7, (short) 8, 9, 10L, 5.5F, 6.6D, 'D', false);
		this.data = data;
		List<Data> dataList = new LinkedList<>();
		dataList.add(data);
		dataList.add(data3);
		this.dataList = dataList;
		Set<Data> dataSet = new HashSet<>();
		dataSet.add(data2);
		dataSet.add(data3);
		this.dataSet = dataSet;
		Map<String, Data> dataMap = new HashMap<>(4);
		dataMap.put("我是谁", data);
		dataMap.put("我管你", data2);
		this.dataMap = dataMap;
	}

	@Test
	public void test() {
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
	}

	@Test
	public void JdkSerializationRedisSerializer() {
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		stringObjectValueOperations.set(K, 123);
//		stringObjectValueOperations.increment(K);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, "你好啊");
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
		stringObjectValueOperations.set(K, data);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, dataList);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, dataSet);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, dataMap);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
		// org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
		// 反序列化时不需要提供类型信息
		// 强制需要实现序列化,直接看redis不太方便,据说占的空间会大一点,反序列化为String后,乱七八糟的,还挺长 TODO 尝试看redis中能否获取size
		// 不支持自增自减操作
	}

	@Test
	public void StringRedisSerializer() {
		// RedisTemplate默认序列化器为org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
		// Bean配置了默认序列化器为org.springframework.data.redis.serializer.StringRedisSerializer
		stringObjectValueOperations.set(K, "你好啊");
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringStringValueOperations.set(K, "0.0,明显我不好啊!!!");
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, "1");
		stringObjectValueOperations.increment(K);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringStringValueOperations.get(K));
		// org.springframework.data.redis.serializer.StringRedisSerializer
		// 支持自增自减操作
		// 只支持String类型,用起来并不方便,需要手动把value转成string,不然set和get都会类转换异常
		// RedisTemplate和StringRedisTemplate数据是互通的,想想也是通的,网上都是胡说八道
	}

	@Test
	public void GenericToStringSerializer() {
		// GenericToStringSerializer<Data> DataGenericToStringSerializer = new GenericToStringSerializer<>(Data.class);
		// DataGenericToStringSerializer.setTypeConverter(new TypeConverter(););
		// 需要实现org.springframework.beans.TypeConverter类型的类型转换器,用于把Data和String互相转换
		// redisTemplate.setValueSerializer(DataGenericToStringSerializer);
		// stringObjectValueOperations.set(K, data);
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
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, "你好啊");
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, data);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, dataList);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, dataSet);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, dataMap);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		// org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
		// 支持自增自减操作
		// 序列化为json,而且包含数据类型,可直接强制转换类型,非常方便,强烈推荐
		// {"@class":"com.mrathena.redis.Data","name":{"@class":"com.mrathena.redis.Name","Dataname":"u","nickname":"n"},"age":20,"sex":true}
	}

	@Test
	public void Jackson2JsonRedisSerializer() {
		Jackson2JsonRedisSerializer<Object> objectJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		objectJackson2JsonRedisSerializer.setObjectMapper(objectMapper);
		// 如果没有ObjectMapper,反序列化的时候会报错,LinkedHashMap不能转化成Data
		redisTemplate.setValueSerializer(objectJackson2JsonRedisSerializer);
		stringObjectValueOperations.set(K, 123);
		stringObjectValueOperations.increment(K);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, "你好啊");
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, data);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, dataList);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, dataSet);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, dataMap);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		// org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
		// 支持自增自减操作
		// 序列化为json,但是感觉不太合理,包含数据类型,可直接强制转换类型,也算方便
		// ["com.mrathena.redis.Data",{"name":["com.mrathena.redis.Name",{"Dataname":"u","nickname":"n"}],"age":20,"sex":true}]
	}

	@Test
	public void FastJsonRedisSerializer() {
		FastJsonRedisSerializer<Object> objectFastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
		ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
		// 开启FastJson的AutoType,可以给Json中写入class信息
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue, SerializerFeature.WriteClassName);
		objectFastJsonRedisSerializer.setFastJsonConfig(fastJsonConfig);
		// 如果没有SerializerFeature.WriteMapNullValue,序列化后的json中没有值为null的字段
		redisTemplate.setValueSerializer(objectFastJsonRedisSerializer);
		stringObjectValueOperations.set(K, 123);
		stringObjectValueOperations.increment(K);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, "你好啊");
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, data);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, dataList);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, dataSet);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, dataMap);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		// com.alibaba.fastjson.support.spring.FastJsonRedisSerializer
		// 支持自增自减操作
		// 默认会去掉值为null的字段(可以设置支持),默认不支持AutoType(可以设置支持)
		// 默认反序列化后的类型为JsonObject和JsonArray,不能直接到JavaBean(开启AutoType后可以)
		// 序列化结果不是标准JSON
	}

	@Test
	public void CustomerFastJsonRedisSerializer() {
		redisTemplate.setValueSerializer(new CustomFastJsonRedisSerializer<>(Object.class));
		stringObjectValueOperations.set(K, 123);
		stringObjectValueOperations.increment(K);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, "你好啊");
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, data);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, dataList);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		List<Data> dDataList = (List<Data>) stringObjectValueOperations.get(K);
		System.out.println(dDataList);
		System.out.println();
		stringObjectValueOperations.set(K, dataSet);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, dataMap);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		// com.mrathena.spring.redis.serializer.CustomFastJsonRedisSerializer
		// 支持自增自减操作
		// 默认会去掉值为null的字段(可以设置支持),默认不支持AutoType(可以设置支持)
		// 默认反序列化后的类型为JsonObject和JsonArray,不能直接到JavaBean(开启AutoType后可以)
		// 序列化结果不是标准JSON
	}

	@Test
	public void KryoRedisSerializer() {
		//
	}

	@Test
	public void CustomKryoRedisSerializer() {
		redisTemplate.setValueSerializer(new CustomKryoRedisSerializer<>(Object.class));
		stringObjectValueOperations.set(K, "你好啊");
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, data);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, dataList);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, dataSet);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		System.out.println();
		stringObjectValueOperations.set(K, dataMap);
		System.out.println(stringObjectValueOperations.get(K));
		System.out.println(stringObjectValueOperations.get(K).getClass());
		System.out.println(stringStringValueOperations.get(K));
		// com.mrathena.spring.redis.serializer.CustomKryoRedisSerializer
		// 不支持自增自减操作
		// 序列化后不便查看,但据说占用空间很小
		// list等集合不支持
	}

}
