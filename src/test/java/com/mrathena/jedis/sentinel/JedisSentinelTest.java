package com.mrathena.jedis.sentinel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.*;

/**
 * @author mrathena on 2019-08-11 22:34
 */
public class JedisSentinelTest {

	@Test
	public void t() {
		List<String> configTagList = Arrays.asList("".split(","));
		System.out.println(configTagList.size());
		System.out.println(CollectionUtils.isEmpty(configTagList));
	}

	@Test
	public void test() {
		Set<String> hostPortSet = new HashSet<>();
		hostPortSet.add("116.62.162.47:26379");
		hostPortSet.add("116.62.162.47:26380");
		hostPortSet.add("116.62.162.47:26381");

		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMinIdle(1);
		poolConfig.setTestOnBorrow(true);

		// String masterName, Set<String> sentinels, GenericObjectPoolConfig poolConfig, int connectionTimeout, int soTimeout, String password, int database, String clientName
		JedisSentinelPool jedisSentinelPool = new JedisSentinelPool("mymaster", hostPortSet, poolConfig, 2000, 2000, "Hhsrv587..", 15, "JedisSentinelTest");

		Jedis jedis = jedisSentinelPool.getResource();
		Jedis jedis2 = jedisSentinelPool.getResource();
		System.out.println(jedis);
		System.out.println(jedis2);
		jedis.set("key", "value");
		System.out.println(jedis2.get("key"));

	}

}
