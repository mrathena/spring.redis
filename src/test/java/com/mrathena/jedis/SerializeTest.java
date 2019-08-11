package com.mrathena.jedis;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrathena.spring.redis.serializer.CustomFastJsonRedisSerializer;
import com.mrathena.spring.redis.serializer.CustomKryoRedisSerializer;
import com.mrathena.toolkit.IdKit;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.*;

/**
 * @author mrathena
 */
public class SerializeTest {

	private Jedis jedis;

	private String key = "test";
	private byte[] k, v1, v2, v3, v4, v5;

	private int times = 100;
	private long start, interval;

	private Base base;
	private Complex complex;
	private List<Complex> complexList = new LinkedList<>();
	private Set<Complex> complexSet = new LinkedHashSet<>();
	private Map<String, Object> complexMap = new LinkedHashMap<>();

	private static JdkSerializationRedisSerializer jdkSerializationRedisSerializer;
	private static GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer;
	private static Jackson2JsonRedisSerializer jackson2JsonRedisSerializer;
	private static FastJsonRedisSerializer fastJsonRedisSerializer;
	private static CustomFastJsonRedisSerializer customFastJsonRedisSerializer;
	private static CustomKryoRedisSerializer customKryoRedisSerializer;

	private void initTestData() {
		base = getBase();

		complex = getComplex();

		complexList.add(getComplex());
		complexList.add(getComplex());
		complexList.add(getComplex());
		complexList.add(getComplex());

		complexSet.add(getComplex());
		complexSet.add(getComplex());
		complexSet.add(getComplex());
		complexSet.add(getComplex());

		complexMap.put(IdKit.generateUUID(), base);
		complexMap.put(IdKit.generateUUID(), complex);
		complexMap.put(IdKit.generateUUID(), complexSet);
		complexMap.put(IdKit.generateUUID(), complexList);
		List<Complex> complexList2 = new LinkedList<>();
		complexList2.add(getComplex());
		complexList2.add(getComplex());
		complexList2.add(getComplex());
		complexList2.add(getComplex());
		complexMap.put(IdKit.generateUUID(), complexList2);
		List<Complex> complexList3 = new LinkedList<>();
		complexList3.add(getComplex());
		complexList3.add(getComplex());
		complexList3.add(getComplex());
		complexList3.add(getComplex());
		complexMap.put(IdKit.generateUUID(), complexList3);
		List<Complex> complexList4 = new LinkedList<>();
		complexList4.add(getComplex());
		complexList4.add(getComplex());
		complexList4.add(getComplex());
		complexList4.add(getComplex());
		complexMap.put(IdKit.generateUUID(), complexList4);
	}

	private void initTestSerializer() {
		jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
		genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
		Jackson2JsonRedisSerializer<Object> objectJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		objectJackson2JsonRedisSerializer.setObjectMapper(objectMapper);
		jackson2JsonRedisSerializer = objectJackson2JsonRedisSerializer;
		FastJsonRedisSerializer<Object> objectFastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
		ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
		// 开启FastJson的AutoType,可以给Json中写入class信息
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue, SerializerFeature.WriteClassName);
		objectFastJsonRedisSerializer.setFastJsonConfig(fastJsonConfig);
		// 如果没有SerializerFeature.WriteMapNullValue,序列化后的json中没有值为null的字段
		fastJsonRedisSerializer = objectFastJsonRedisSerializer;
		customFastJsonRedisSerializer = new CustomFastJsonRedisSerializer<>(Object.class);
		customKryoRedisSerializer = new CustomKryoRedisSerializer<>(Object.class);
	}

	@Before
	public void before() {
		this.jedis = Common.getJedis();
		initTestData();
		initTestSerializer();
	}

	@After
	public void after() {
		jedis.flushDB();
		jedis.disconnect();
	}

	@Test
	public void jdkUsabilityTest() throws Exception {
		// 易用性测试
		k = jdkSerialize(key);
		System.out.println(jdkDeserialize(k));
		v1 = jdkSerialize(base);
		System.out.println(jdkDeserialize(v1));
		v2 = jdkSerialize(complex);
		System.out.println(jdkDeserialize(v2));
		v3 = jdkSerialize(complexList);
		System.out.println(jdkDeserialize(v3));
		v4 = jdkSerialize(complexSet);
		System.out.println(jdkDeserialize(v4));
		v5 = jdkSerialize(complexMap);
		System.out.println(jdkDeserialize(v5));
	}

	@Test
	public void genericJackson2JsonUsabilityTest() {
		// 易用性测试
		k = genericJackson2JsonSerialize(key);
		System.out.println(genericJackson2JsonDeserialize(k));
		v1 = genericJackson2JsonSerialize(base);
		System.out.println(genericJackson2JsonDeserialize(v1));
		v2 = genericJackson2JsonSerialize(complex);
		System.out.println(genericJackson2JsonDeserialize(v2));
		v3 = genericJackson2JsonSerialize(complexList);
		System.out.println(genericJackson2JsonDeserialize(v3));
		v4 = genericJackson2JsonSerialize(complexSet);
		System.out.println(genericJackson2JsonDeserialize(v4));
		v5 = genericJackson2JsonSerialize(complexMap);
		System.out.println(genericJackson2JsonDeserialize(v5));
	}

	@Test
	public void customKryoUsabilityTest() {
		// 易用性测试
		k = customKryoSerialize(key);
		System.out.println(customKryoDeserialize(k));
		v1 = customKryoSerialize(base);
		System.out.println(customKryoDeserialize(v1));
		v2 = customKryoSerialize(complex);
		System.out.println(customKryoDeserialize(v2));
		v3 = customKryoSerialize(complexList);
		System.out.println(customKryoDeserialize(v3));
		v4 = customKryoSerialize(complexSet);
		System.out.println(customKryoDeserialize(v4));
		v5 = customKryoSerialize(complexMap);
		System.out.println(customKryoDeserialize(v5));
	}

	@Test
	public void jdkSizeTest() throws Exception {
		// 空间测试
		// 11
		// 239
		// 6477
		// 21853
		// 21861
		// 109312
		k = jdkSerialize(key);
		System.out.println(k.length);
		v1 = jdkSerialize(base);
		System.out.println(v1.length);
		v2 = jdkSerialize(complex);
		System.out.println(v2.length);
		v3 = jdkSerialize(complexList);
		System.out.println(v3.length);
		v4 = jdkSerialize(complexSet);
		System.out.println(v4.length);
		v5 = jdkSerialize(complexMap);
		System.out.println(v5.length);
	}

	@Test
	public void genericJackson2JsonSizeTest() {
		// 空间测试
		// 6
		// 261
		// 15842
		// 63394
		// 63315
		// 333127
		k = genericJackson2JsonSerialize(key);
		System.out.println(k.length);
		v1 = genericJackson2JsonSerialize(base);
		System.out.println(v1.length);
		v2 = genericJackson2JsonSerialize(complex);
		System.out.println(v2.length);
		v3 = genericJackson2JsonSerialize(complexList);
		System.out.println(v3.length);
		v4 = genericJackson2JsonSerialize(complexSet);
		System.out.println(v4.length);
		v5 = genericJackson2JsonSerialize(complexMap);
		System.out.println(v5.length);
	}

	@Test
	public void customKryoSizeTest() {
		// 空间测试
		// 5
		// 89
		// 4939
		// 18722
		// 18723
		// 97095
		k = customKryoSerialize(key);
		System.out.println(k.length);
		v1 = customKryoSerialize(base);
		System.out.println(v1.length);
		v2 = customKryoSerialize(complex);
		System.out.println(v2.length);
		v3 = customKryoSerialize(complexList);
		System.out.println(v3.length);
		v4 = customKryoSerialize(complexSet);
		System.out.println(v4.length);
		v5 = customKryoSerialize(complexMap);
		System.out.println(v5.length);
	}

	@Test
	public void jdkTimeTest() throws Exception {
		// 时间测试
		// 3953100
		// 10429800
		// 91513200
		// 130352300
		// 91800500
		// 274185500
		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			k = jdkSerialize(key);
			jdkDeserialize(k);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);

		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			v1 = jdkSerialize(base);
			jdkDeserialize(v1);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);

		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			v2 = jdkSerialize(complex);
			jdkDeserialize(v2);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);

		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			v3 = jdkSerialize(complexList);
			jdkDeserialize(v3);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);

		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			v4 = jdkSerialize(complexSet);
			jdkDeserialize(v4);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);

		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			v5 = jdkSerialize(complexMap);
			jdkDeserialize(v5);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);
	}

	@Test
	public void genericJackson2JsonTimeTest() {
		// 时间测试
		// 39623700
		// 82173500
		// 139210700
		// 142158700
		// 98928500
		// 418888700
		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			k = genericJackson2JsonSerialize(key);
			genericJackson2JsonDeserialize(k);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);

		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			v1 = genericJackson2JsonSerialize(base);
			genericJackson2JsonDeserialize(v1);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);

		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			v2 = genericJackson2JsonSerialize(complex);
			genericJackson2JsonDeserialize(v2);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);

		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			v3 = genericJackson2JsonSerialize(complexList);
			genericJackson2JsonDeserialize(v3);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);

		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			v4 = genericJackson2JsonSerialize(complexSet);
			genericJackson2JsonDeserialize(v4);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);

		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			v5 = genericJackson2JsonSerialize(complexMap);
			genericJackson2JsonDeserialize(v5);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);
	}

	@Test
	public void customKryoTimeTest() {
		// 时间测试
		// 各种报错还测个屁
		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			k = customKryoSerialize(key);
			customKryoDeserialize(k);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);

		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			v1 = customKryoSerialize(base);
			customKryoDeserialize(v1);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);

		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			v2 = customKryoSerialize(complex);
			customKryoDeserialize(v2);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);

		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			v3 = customKryoSerialize(complexList);
			customKryoDeserialize(v3);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);

		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			v4 = customKryoSerialize(complexSet);
			customKryoDeserialize(v4);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);

		start = System.nanoTime();
		for (int i = 0; i < times; i++) {
			v5 = customKryoSerialize(complexMap);
			customKryoDeserialize(v5);
		}
		interval = System.nanoTime() - start;
		System.out.println(interval);
	}

	/**
	 * 从spring的JdkSerializationRedisSerializer里抄出来的
	 */
	private static byte[] jdkSerialize(Object object) throws Exception {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		     ObjectOutputStream oos = new ObjectOutputStream(baos)
		) {
			oos.writeObject(object);
			oos.flush();
			return baos.toByteArray();
		}
	}

	/**
	 * 可以从spring的JdkSerializationRedisSerializer里抄,但是没有
	 */
	private static Object jdkDeserialize(byte[] bytes) throws Exception {
		Object object = null;
		try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
			object = ois.readObject();
			return object;
		} catch (EOFException e) {
			// EOFException仅仅是流到达最末尾的标记,捕获该异常后,直接处理之前的数据就好了,无需处理该异常
			return object;
		}
	}

	private static byte[] genericJackson2JsonSerialize(Object object) {
		return genericJackson2JsonRedisSerializer.serialize(object);
	}

	private static Object genericJackson2JsonDeserialize(byte[] bytes) {
		return genericJackson2JsonRedisSerializer.deserialize(bytes);
	}

	private static byte[] customKryoSerialize(Object object) {
		return customKryoRedisSerializer.serialize(object);
	}

	private static Object customKryoDeserialize(byte[] bytes) {
		return customKryoRedisSerializer.deserialize(bytes);
	}

	private static Base getBase() {
		Random random = new Random();
		Base base = new Base().setId(random.nextLong()).setIntFiled(random.nextInt())
				.setShortFiled((short) random.nextInt()).setByteFiled((byte) random.nextInt())
				.setDoubleFiled(random.nextDouble()).setFloatFiled(random.nextFloat())
				.setBooleanFiled(random.nextBoolean()).setCharFiled(IdKit.generateUUID().charAt(0))
				.setStringFiled(IdKit.generateUUID());
		return base;
	}

	private static Complex getComplex() {
		Complex complex = new Complex().setName(IdKit.generateUUID());
		List<String> stringList = Collections.singletonList(IdKit.generateUUID());
		complex.setStringList(stringList);
		Set<String> stringSet = Collections.singleton(IdKit.generateUUID());
		complex.setStringSet(stringSet);
		Map<String, String> stringMap = Collections.singletonMap(IdKit.generateUUID(), IdKit.generateUUID());
		complex.setStringMap(stringMap);
		complex.setBase(getBase());
		List<Base> list = Arrays.asList(getBase(), getBase(), getBase(), getBase(), getBase());
		complex.setList(list);
		LinkedList<Base> linkedList = new LinkedList<>();
		linkedList.add(getBase());
		linkedList.add(getBase());
		linkedList.add(getBase());
		complex.setLinkedList(linkedList);
		ArrayList<Base> arrayList = new ArrayList<>(8);
		arrayList.add(getBase());
		arrayList.add(getBase());
		arrayList.add(getBase());
		complex.setArrayList(arrayList);
		Set<Base> set = new HashSet<>();
		set.add(getBase());
		set.add(getBase());
		set.add(getBase());
		complex.setSet(set);
		HashSet<Base> hashSet = new HashSet<>();
		hashSet.add(getBase());
		hashSet.add(getBase());
		hashSet.add(getBase());
		hashSet.add(getBase());
		complex.setHashSet(hashSet);
		TreeSet<Base> treeSet = new TreeSet<>();
		treeSet.add(getBase());
		treeSet.add(getBase());
		treeSet.add(getBase());
		treeSet.add(getBase());
		complex.setTreeSet(treeSet);
		LinkedHashSet<Base> linkedHashSet = new LinkedHashSet<>();
		linkedHashSet.add(getBase());
		linkedHashSet.add(getBase());
		linkedHashSet.add(getBase());
		linkedHashSet.add(getBase());
		linkedHashSet.add(getBase());
		linkedHashSet.add(getBase());
		complex.setLinkedHashSet(linkedHashSet);
		Map<String, Base> map = new Hashtable<>();
		map.put(IdKit.generateUUID(), getBase());
		map.put(IdKit.generateUUID(), getBase());
		map.put(IdKit.generateUUID(), getBase());
		map.put(IdKit.generateUUID(), getBase());
		map.put(IdKit.generateUUID(), getBase());
		map.put(IdKit.generateUUID(), getBase());
		map.put(IdKit.generateUUID(), getBase());
		complex.setMap(map);
		HashMap<String, Base> hashMap = new HashMap<>();
		hashMap.put(IdKit.generateUUID(), getBase());
		hashMap.put(IdKit.generateUUID(), getBase());
		hashMap.put(IdKit.generateUUID(), getBase());
		hashMap.put(IdKit.generateUUID(), getBase());
		hashMap.put(IdKit.generateUUID(), getBase());
		hashMap.put(IdKit.generateUUID(), getBase());
		hashMap.put(IdKit.generateUUID(), getBase());
		complex.setHashMap(hashMap);
		TreeMap<String, Base> treeMap = new TreeMap<>();
		treeMap.put(IdKit.generateUUID(), getBase());
		treeMap.put(IdKit.generateUUID(), getBase());
		treeMap.put(IdKit.generateUUID(), getBase());
		treeMap.put(IdKit.generateUUID(), getBase());
		treeMap.put(IdKit.generateUUID(), getBase());
		complex.setTreeMap(treeMap);
		LinkedHashMap<String, Base> linkedHashMap = new LinkedHashMap<>();
		linkedHashMap.put(IdKit.generateUUID(), getBase());
		linkedHashMap.put(IdKit.generateUUID(), getBase());
		linkedHashMap.put(IdKit.generateUUID(), getBase());
		linkedHashMap.put(IdKit.generateUUID(), getBase());
		linkedHashMap.put(IdKit.generateUUID(), getBase());
		linkedHashMap.put(IdKit.generateUUID(), getBase());
		complex.setLinkedHashMap(linkedHashMap);
		return complex;
	}


}

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
class Base implements Serializable, Comparable<Base> {
	private long id;
	private int intFiled;
	private short shortFiled;
	private byte byteFiled;
	private double doubleFiled;
	private float floatFiled;
	private boolean booleanFiled;
	private char charFiled;
	private String stringFiled;

	@Override
	public int compareTo(Base o) {
		return this.getStringFiled().compareTo(o.getStringFiled());
	}
}

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
class Complex implements Serializable, Comparable<Complex> {
	private String name;
	private List<String> stringList;
	private Set<String> stringSet;
	private Map<String, String> stringMap;
	private Base base;
	private List<Base> list;
	private LinkedList<Base> linkedList;
	private ArrayList<Base> arrayList;
	private Set<Base> set;
	private HashSet<Base> hashSet;
	private TreeSet<Base> treeSet;
	private LinkedHashSet<Base> linkedHashSet;
	private Map<String, Base> map;
	private HashMap<String, Base> hashMap;
	private TreeMap<String, Base> treeMap;
	private LinkedHashMap<String, Base> linkedHashMap;

	@Override
	public int compareTo(Complex o) {
		return this.getName().compareTo(o.getName());
	}
}