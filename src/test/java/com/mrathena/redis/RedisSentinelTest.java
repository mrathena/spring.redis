package com.mrathena.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author mrathena on 2019/7/30 19:35
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.redis.sentinel.xml")
public class RedisSentinelTest {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Test
	public void test() {
		Data data = new Data("s", new O("u", "n"), null, (byte) 1, (short) 2, 3, 4L, 1.1F, 1.2D, 'a', true, (byte) 5, (short) 6, 7, 8L, 2.3F, 2.2D, 'B', true);
		redisTemplate.opsForValue().set("data", data);
		System.out.println(redisTemplate.opsForValue().get("data"));
	}

}
