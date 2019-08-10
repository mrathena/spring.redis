package com.mrathena.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author mrathena on 2019/7/30 19:35
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.redis.cluster.xml")
public class RedisClusterTest {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Test
	public void test() {
		System.out.println(stringRedisTemplate.opsForValue().get("test"));
		System.out.println(redisTemplate.opsForValue().get("test"));
	}

}
