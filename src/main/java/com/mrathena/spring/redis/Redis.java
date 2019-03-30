package com.mrathena.spring.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author mrathena on 2019/3/30 13:30
 */
@Component
public class Redis {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

}
