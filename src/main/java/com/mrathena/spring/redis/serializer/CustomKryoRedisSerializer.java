package com.mrathena.spring.redis.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayOutputStream;

/**
 * @author mrathena on 2019/8/3 18:06
 */
public class CustomKryoRedisSerializer<T> implements RedisSerializer<T> {
	private Logger logger = LoggerFactory.getLogger(CustomKryoRedisSerializer.class);

	private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

	private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(Kryo::new);

	private Class<T> clazz;

	public CustomKryoRedisSerializer(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}

	@Override
	public byte[] serialize(Object object) throws SerializationException {
		if (object == null) {
			return EMPTY_BYTE_ARRAY;
		}

		Kryo kryo = KRYO_THREAD_LOCAL.get();
		kryo.setReferences(false);
		kryo.register(clazz);

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 Output output = new Output(baos)) {
			kryo.writeClassAndObject(output, object);
			output.flush();
			return baos.toByteArray();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return EMPTY_BYTE_ARRAY;
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null || bytes.length <= 0) {
			return null;
		}

		Kryo kryo = KRYO_THREAD_LOCAL.get();
		kryo.setReferences(false);
		kryo.register(clazz);

		try (Input input = new Input(bytes)) {
			return (T) kryo.readClassAndObject(input);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

}
