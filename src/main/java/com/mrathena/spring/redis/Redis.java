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
	 * String set
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
	 * String set
	 */
	public void set(String key, Object object) {
		redisTemplate.opsForValue().set(key, object);
	}

	/**
	 * String set
	 * 超时时间单位为毫秒
	 */
	public void setInMS(String key, Object object, long timeout) {
		set(key, object, TimeUnit.MILLISECONDS, timeout);
	}

	/**
	 * String set
	 * 超时时间单位为秒
	 */
	public void setInSeconds(String key, Object object, long timeout) {
		set(key, object, TimeUnit.SECONDS, timeout);
	}

	/**
	 * String set
	 * 超时时间单位为分
	 */
	public void setInMinutes(String key, Object object, long timeout) {
		set(key, object, TimeUnit.MINUTES, timeout);
	}

	/**
	 * String set
	 * 超时时间单位为时
	 */
	public void setInHours(String key, Object object, long timeout) {
		set(key, object, TimeUnit.HOURS, timeout);
	}


	/**
	 * String set
	 * 超时时间单位为日
	 */
	public void setInDays(String key, Object object, long timeout) {
		set(key, object, TimeUnit.DAYS, timeout);
	}

	/**
	 * String set key value NX
	 * key不存在则set
	 */
	public boolean setIfAbsent(String key, Object object, TimeUnit timeUnit, long timeout) {
		return redisTemplate.opsForValue().setIfAbsent(key, object, timeout, timeUnit);
	}

	/**
	 * String set key value NX
	 * key不存在则set
	 */
	public boolean setIfAbsent(String key, Object object) {
		return redisTemplate.opsForValue().setIfAbsent(key, object);
	}

	/**
	 * String set key value XX
	 * key存在则set
	 */
	public boolean setIfPresent(String key, Object object, TimeUnit timeUnit, long timeout) {
		return redisTemplate.opsForValue().setIfPresent(key, object, timeout, timeUnit);
	}

	/**
	 * String set key value XX
	 * key存在则set
	 */
	public boolean setIfPresent(String key, Object object) {
		return redisTemplate.opsForValue().setIfPresent(key, object);
	}

}
