package com.mrathena.jedis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mrathena.KryoKit;
import com.mrathena.toolkit.IdKit;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.*;

/**
 * @author mrathena
 */
public class SerializeTest {

	public static void main(String[] args) throws Exception {

		Jedis jedis = new Jedis(Common.HOST, Common.PORT);
		jedis.connect();

		// ----------------------------------------------------------------
		// 序列化测试
		Base base = getBase();
		System.out.println(base);
		System.out.println();

		// 序列化key
		byte[] key, bytes;
		long start, interval;
		Base newBase;
		Complex newComplex;

		key = jdkSerialize("base");

		// java默认序列化
		start = System.nanoTime();
		bytes = jdkSerialize(base);
		interval = System.nanoTime() - start;
		System.out.println("jdkSerialize: interval:" + interval + ", size:" + bytes.length);
		// set
		jedis.set(key, bytes);
		// java默认反序列化
		start = System.nanoTime();
		newBase = jdkDeserialize(jedis.get(key), Base.class);
		interval = System.nanoTime() - start;
		System.out.println("jdkDeserialize: interval:" + interval);
		System.out.println(newBase);
		System.out.println();

		// json默认序列化
		start = System.nanoTime();
		bytes = jsonSerialize(base);
		interval = System.nanoTime() - start;
		System.out.println("jsonSerialize: interval:" + interval + ", size:" + bytes.length);
		// set
		jedis.set(key, bytes);
		// json默认反序列化
		start = System.nanoTime();
		newBase = jsonDeserialize(jedis.get(key), Base.class);
		interval = System.nanoTime() - start;
		System.out.println("jsonDeserialize: interval:" + interval);
		System.out.println(newBase);
		System.out.println();

		// kryo默认序列化
		start = System.nanoTime();
		bytes = kryoSerialize(base);
		interval = System.nanoTime() - start;
		System.out.println("kryoSerialize: interval:" + interval + ", size:" + bytes.length);
		// set
		jedis.set(key, bytes);
		// kryo默认反序列化
		start = System.nanoTime();
		newBase = kryoDeserialize(jedis.get(key));
		interval = System.nanoTime() - start;
		System.out.println("kryoDeserialize: interval:" + interval);
		System.out.println(newBase);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();

		// ----------------------------------------------------------------
		// 组合对象测试
		Complex complex = getComplex();

		System.out.println(complex);
		System.out.println();

		key = jdkSerialize("complex");

		// java默认序列化
		start = System.nanoTime();
		bytes = jdkSerialize(complex);
		interval = System.nanoTime() - start;
		System.out.println("jdkSerialize: interval:" + interval + ", size:" + bytes.length);
		// set
		jedis.set(key, bytes);
		// java默认反序列化
		start = System.nanoTime();
		newComplex = jdkDeserialize(jedis.get(key), Complex.class);
		interval = System.nanoTime() - start;
		System.out.println("jdkDeserialize: interval:" + interval);
		System.out.println(newComplex);
		System.out.println();

		// json默认序列化
		start = System.nanoTime();
		bytes = jsonSerialize(complex);
		interval = System.nanoTime() - start;
		System.out.println("jsonSerialize: interval:" + interval + ", size:" + bytes.length);
		// set
		jedis.set(key, bytes);
		// json默认反序列化
		start = System.nanoTime();
		newComplex = jsonDeserialize(jedis.get(key), Complex.class);
		interval = System.nanoTime() - start;
		System.out.println("jsonDeserialize: interval:" + interval);
		System.out.println(newComplex);
		System.out.println();

		// kryo默认序列化
		start = System.nanoTime();
		bytes = kryoSerialize(complex);
		interval = System.nanoTime() - start;
		System.out.println("kryoSerialize: interval:" + interval + ", size:" + bytes.length);
		// set
		jedis.set(key, bytes);
		// kryo默认反序列化
		start = System.nanoTime();
		newComplex = kryoDeserialize(jedis.get(key));
		interval = System.nanoTime() - start;
		System.out.println("kryoDeserialize: interval:" + interval);
		System.out.println(newComplex);
		System.out.println();

		// ----------------------------------------------------------------
		List<Complex> complexList = new LinkedList<>();
		complexList.add(getComplex());
		complexList.add(getComplex());
		complexList.add(getComplex());
		complexList.add(getComplex());
		System.out.println(jdkSerialize(complexList).length);
		System.out.println(JSON.toJSONBytes(complexList).length);
		System.out.println(KryoKit.writeToByteArray(complexList).length);
		System.out.println(JSON.parseArray(JSON.toJSONString(complexList), Complex.class));
		System.out.println(JSON.parseObject(JSON.toJSONString(complexList), new TypeReference<List<Complex>>() {}));

		Set<Complex> complexSet = new HashSet<>();
		complexSet.add(getComplex());
		complexSet.add(getComplex());
		complexSet.add(getComplex());
		complexSet.add(getComplex());
		System.out.println(jdkSerialize(complexSet).length);
		System.out.println(JSON.toJSONBytes(complexSet).length);
		System.out.println(KryoKit.writeToByteArray(complexSet).length);
		System.out.println(JSON.parseArray(JSON.toJSONString(complexSet), Complex.class));
		System.out.println(JSON.parseObject(JSON.toJSONString(complexSet), new TypeReference<Set<Complex>>() {}));

		Map<String, List<Complex>> complexMap = new HashMap<>(4);
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
		System.out.println(jdkSerialize(complexMap).length);
		System.out.println(JSON.toJSONBytes(complexMap).length);
		System.out.println(KryoKit.writeToByteArray(complexMap).length);
		System.out.println(JSON.parseObject(JSON.toJSONString(complexMap), Map.Entry.class));
		System.out.println(JSON.parseObject(JSON.toJSONString(complexMap), new TypeReference<Map<String, List<Complex>>>() {}));

		jedis.flushAll();
		Common.print(jedis);

		jedis.disconnect();

	}

	private static byte[] jdkSerialize(Object object) throws Exception {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		     ObjectOutputStream oos = new ObjectOutputStream(baos)
		) {
			oos.writeObject(object);
			oos.flush();
			return baos.toByteArray();
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T jdkDeserialize(byte[] bytes, Class<T> clazz) throws Exception {
		T object = null;
		try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
			object = (T) ois.readObject();
			return object;
		} catch (EOFException e) {
			// EOFException仅仅是流到达最末尾的标记,捕获该异常后,直接处理之前的数据就好了,无需处理该异常
			return object;
		}
	}

	private static byte[] jsonSerialize(Object object) {
		return JSON.toJSONBytes(object);
	}

	private static <T> T jsonDeserialize(byte[] bytes, Class<T> clazz) {
		return JSON.parseObject(bytes, clazz);
	}

	private static byte[] kryoSerialize(Object object) throws IOException {
		return KryoKit.writeToByteArray(object);
	}

	private static <T> T kryoDeserialize(byte[] bytes) {
		return KryoKit.readFromByteArray(bytes);
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