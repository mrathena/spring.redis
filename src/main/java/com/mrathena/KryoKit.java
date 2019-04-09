package com.mrathena;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;

/**
 * @author mrathena
 */
public class KryoKit {

	private static final String DEFAULT_ENCODING = "UTF-8";

	//每个线程的 Kryo 实例
	private static final ThreadLocal<Kryo> kryoLocal = ThreadLocal.withInitial(() -> {
		/**
		 * 不要轻易改变这里的配置！更改之后，序列化的格式就会发生变化，
		 * 上线的同时就必须清除 Redis 里的所有缓存，
		 * 否则那些缓存再回来反序列化的时候，就会报错
		 */
		Kryo kryo = new Kryo();
		//支持对象循环引用（否则会栈溢出）
		//默认值就是 true，添加此行的目的是为了提醒维护者，不要改变这个配置
//		kryo.setReferences(false);
		kryo.setMaxDepth(10);
		//不强制要求注册类（注册行为无法保证多个 JVM 内同一个类的注册编号相同；而且业务系统中大量的 Class 也难以一一注册）
		//默认值就是 false，添加此行的目的是为了提醒维护者，不要改变这个配置
		kryo.setRegistrationRequired(false);
		//Fix the NPE bug when deserializing Collections.
		kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
		return kryo;
	});

	/**
	 * 获得当前线程的 Kryo 实例
	 *
	 * @return 当前线程的 Kryo 实例
	 */
	public static Kryo getInstance() {
		return kryoLocal.get();
	}

	// -----------------------------------------------
	// 序列化/反序列化对象，及类型信息
	// 序列化的结果里，包含类型的信息
	// 反序列化时不再需要提供类型
	// -----------------------------------------------

	/**
	 * 将对象【及类型】序列化为字节数组
	 *
	 */
	public static byte[] writeToByteArray(Object object) {
		Kryo kryo = getInstance();
		Output output = new ByteBufferOutput(4096, 1024 * 1024);
		kryo.writeClassAndObject(output, object);
		return output.toBytes();
	}


	/**
	 * 将字节数组反序列化为原对象
	 *
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readFromByteArray(byte[] bytes) {
		Kryo kryo = getInstance();
		return (T) kryo.readClassAndObject(new ByteBufferInput(bytes));
	}

}
