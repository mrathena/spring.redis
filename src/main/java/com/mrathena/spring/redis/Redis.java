package com.mrathena.spring.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis 工具包(放到Biz下)
 *
 * @author mrathena on 2019/3/30 13:30
 */
@Component
public class Redis {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * string set
	 *
	 * @param key      .
	 * @param object   .
	 * @param timeUnit .
	 * @param timeout  .
	 */
	public void set(String key, Object object, TimeUnit timeUnit, long timeout) {
		redisTemplate.opsForValue().set(key, object, timeout, timeUnit);
	}

	/**
	 * string set
	 */
	public void set(String key, Object object) {
		redisTemplate.opsForValue().set(key, object);
	}

	/**
	 * string set
	 * 超时时间单位为毫秒
	 */
	public void setInMS(String key, Object object, long timeout) {
		set(key, object, TimeUnit.MILLISECONDS, timeout);
	}

	/**
	 * string set
	 * 超时时间单位为秒
	 */
	public void setInSeconds(String key, Object object, long timeout) {
		set(key, object, TimeUnit.SECONDS, timeout);
	}

	/**
	 * string set
	 * 超时时间单位为分
	 */
	public void setInMinutes(String key, Object object, long timeout) {
		set(key, object, TimeUnit.MINUTES, timeout);
	}

	/**
	 * string set
	 * 超时时间单位为时
	 */
	public void setInHours(String key, Object object, long timeout) {
		set(key, object, TimeUnit.HOURS, timeout);
	}


	/**
	 * string set
	 * 超时时间单位为日
	 */
	public void setInDays(String key, Object object, long timeout) {
		set(key, object, TimeUnit.DAYS, timeout);
	}

	/**
	 * string set key value NX
	 * key不存在则set
	 */
	public boolean setIfAbsent(String key, Object object, TimeUnit timeUnit, long timeout) {
		return redisTemplate.opsForValue().setIfAbsent(key, object, timeout, timeUnit);
	}

	/**
	 * string set key value NX
	 * key不存在则set
	 */
	public boolean setIfAbsent(String key, Object object) {
		return redisTemplate.opsForValue().setIfAbsent(key, object);
	}

	/**
	 * string set key value XX
	 * key存在则set
	 */
	public boolean setIfPresent(String key, Object object, TimeUnit timeUnit, long timeout) {
		return redisTemplate.opsForValue().setIfPresent(key, object, timeout, timeUnit);
	}

	/**
	 * string set key value XX
	 * key存在则set
	 */
	public boolean setIfPresent(String key, Object object) {
		return redisTemplate.opsForValue().setIfPresent(key, object);
	}

	/**
	 * string get
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<T> clazz) {
		return (T) redisTemplate.opsForValue().get(key);
	}

	/**
	 * string get
	 */
	public String get(String key) {
		return (String) redisTemplate.opsForValue().get(key);
	}



}
