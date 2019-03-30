import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

/**
 * <p>Title: RedisComponent</br>
 * <p>Description: (redis基础操作类) </br>
 * @author zsy
 * @version 2016-11-30
 */
@Component
public class RedisComponent {

		@Autowired
		@Qualifier("redisTemplate")
		private RedisTemplate<String, String> redisTemplate;
		
		@Autowired
		@Qualifier("stringRedisTemplate")
		private StringRedisTemplate stringRedisTemplate;
	    
	    private  Gson gson = new Gson();
	    
	    public void setRedisTemplate(RedisTemplate<String, String> redisTemp) {
	        redisTemplate = redisTemp;
	    }
	     
	    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemp) {
	    	stringRedisTemplate = stringRedisTemp;
	    }
	    
	    public boolean insertString(String key, String value) {  
	        try {  
	             redisTemplate.opsForValue().set(key, value);  
	            return true;  
	        } catch (Exception e) {  
	            return false;  
	        }  
	  
	    }
	

	    
	    /* -------------queue------------ */
	    /**
	     * <p>Title: pushEntity</br>
	     * <p>Description: (实体入队-从尾插入)</br>
	     * @param key 队列key 
	     * @param value 插入值
	     */
	    public <T>void pushEntity(final String key,final T value) {
			stringRedisTemplate.executePipelined(new RedisCallback<Long>() {
				public Long doInRedis(RedisConnection connection)
						throws DataAccessException {
					StringRedisConnection stringRedisConn = (StringRedisConnection) connection;
					Long l = stringRedisConn.rPush(key, toJson(value));
					return l;
				}
			});
		}
	    
	    
	    /**
	     * <p>Title: popEntity</br>
	     * <p>Description: (优先级队列实体出队-从头去除有锁)</br>
	     * @param timeout 超时时间 0为无限
	     * @param clazz 泛型
	     * @param keys 队列key 多个
	     * @return 
	     */
	    public <T> T popEntity(final int timeout,final Class<T> clazz,final String... keys) {
	    	String value = stringRedisTemplate.execute(new RedisCallback<String>() {
				public String doInRedis(RedisConnection connection)
						throws DataAccessException {
					StringRedisConnection stringRedisConn = (StringRedisConnection) connection;
					List<String> list = stringRedisConn.bLPop(timeout, keys);
					if (list == null) {
						return null;
					} else {
						return list.get(1);
					}
				}
			});
	    	if(value == null) return null;
	    	return parseJson(value, clazz);
		}
	    
	  
		/**
		 * <p>Title: push</br>
		 * <p>Description: (插入队列-多值插入-从尾插入)</br>
		 * @param key
		 * @param values 
		 */
		public void push(final String key,final String... values) {
			stringRedisTemplate.executePipelined(new RedisCallback<Long>() {
				public Long doInRedis(RedisConnection connection)
						throws DataAccessException {
					StringRedisConnection stringRedisConn = (StringRedisConnection) connection;
					Long l = stringRedisConn.rPush(key, values);
					return l;
				}
			});
		}
		

	
		/**
		 * <p>Title: pop</br>
		 * <p>Description: (优先级队列-从头取出有锁)</br>
		 * @param timeout 超时时间 为0时一直等待
		 * @param keys
		 * @return 
		 */
		public String pop(final int timeout,final String... keys) {
			return stringRedisTemplate.execute(new RedisCallback<String>() {
				public String doInRedis(RedisConnection connection)
						throws DataAccessException {
					StringRedisConnection stringRedisConn = (StringRedisConnection) connection;
					List<String> list = stringRedisConn.bLPop(timeout, keys);
					if (list == null) {
						return null;
					} else {
						return list.get(1);
					}
				}
			});
		}
	    
	    
	    /* ----------- common --------- */
	    public  Collection<String> keys(String pattern) {
	        return stringRedisTemplate.keys(pattern);
	    }
	    
	    public  void delete(String key) {
	    	stringRedisTemplate.delete(key);
	    }
	 
	    public  void delete(Collection<String> key) {
	    	stringRedisTemplate.delete(key);
	    }
	     
	    /* ----------- string --------- */
	    public  <T> T get(String key, Class<T> clazz) {
	        String value = stringRedisTemplate.opsForValue().get(key);
	        return parseJson(value, clazz);
	    }
	    
	    public  <T> T getByType(String key, Type type) {
	        String value = stringRedisTemplate.opsForValue().get(key);
	        return parseTypeJson(value, type);
	    }
	    
	    public  String get(String key) {
	        return stringRedisTemplate.opsForValue().get(key);
	    }
	     
	    public  <T> List<T> mget(Collection<String> keys, Class<T> clazz) {
	        List<String> values = stringRedisTemplate.opsForValue().multiGet(keys);
	        return parseJsonList(values, clazz);
	    }
	    
	    public  <T> void set(String key, T obj, Long timeout, TimeUnit unit) {
	        if (obj == null) {
	            return;
	        }
	         
	        String value = toJson(obj);
	        if (timeout != null) {
	        	stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
	        } else {
	        	stringRedisTemplate.opsForValue().set(key, value);
	        }
	    }
	    
	    public  <T> void setStr(String key, String value, Long timeout, TimeUnit unit) {
	        if (value == null) {
	            return;
	        }
//	        String value = toJson(obj);
	        if (timeout != null) {
	        	stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
	        } else {
	        	stringRedisTemplate.opsForValue().set(key, value);
	        }
	    }
	 
	    public  <T> T getAndSet(String key, T obj, Class<T> clazz) {
	        if (obj == null) {
	            return get(key, clazz);
	        }
	         
	        String value = stringRedisTemplate.opsForValue().getAndSet(key, toJson(obj));
	        return parseJson(value, clazz);
	    }
	     
	    public  int decrement(String key, int delta) {
	        Long value = stringRedisTemplate.opsForValue().increment(key, -delta);
	        return value.intValue();
	    }
	     
	    public Long increment(String key, int delta) {
	        Long value = stringRedisTemplate.opsForValue().increment(key, delta);
	        return value;
	    }
	     
	    /* ----------- list --------- */
	    
	    /**
	     * <p>Title: remove</br>
	     * <p>Description: (删除指定list中的value数量 count删除个数)</br>
	     * @param key
	     * @param count
	     * @param obj 
	     */
	    public  Long remove(String key, int count, String value) {
	        if (value == null) {
	            return null;
	        }
	        return stringRedisTemplate.opsForList().remove(key, count, value);
	    }
	    
	    
	    /**
	     * <p>Title: size</br>
	     * <p>Description: (list的长度)</br>
	     * @param key
	     * @return 
	     */
	    public  int size(String key) {
	        return stringRedisTemplate.opsForList().size(key).intValue();
	    }
	 
	    /**
	     * <p>Title: range</br>
	     * <p>Description: (list指定范围内的数据)</br>
	     * @param key
	     * @param start 开始位置
	     * @param end 结束位置 -1所有
	     * @param clazz
	     * @return 
	     */
	    public  <T> List<T> range(String key, long start, long end, Class<T> clazz) {
	        List<String> list = stringRedisTemplate.opsForList().range(key, start, end);
	        return parseJsonList(list, clazz);
	    }
	 
	   /* public  void rightPushAll(String key, Collection<?> values, Long timeout,
	            TimeUnit unit) {
	        if (values == null || values.isEmpty()) {
	            return;
	        }
	         
	        stringRedisTemplate.opsForList().rightPushAll(key, toJsonList(values));
	        if (timeout != null) {
	        	stringRedisTemplate.expire(key, timeout, unit);
	        }
	    }*/
	     
	    /** 
	     * 压栈 
	     *  
	     * @param key 
	     * @return 
	     */ 
	    public  <T> void leftPush(String key, T obj) {
	        if (obj == null) {
	            return;
	        }
	        stringRedisTemplate.opsForList().leftPush(key, toJson(obj));
	    }
	 
	    /** 
	     * 出栈 /出队
	     *  
	     * @param key 
	     * @return 
	     */  
	    public  <T> T leftPop(String key, Class<T> clazz) {
	        String value = stringRedisTemplate.opsForList().leftPop(key);
	        return parseJson(value, clazz);
	    }
	    
	    
	    /** 
	     * 堵塞线程(出栈 /出队)
	     *  
	     * @param key 
	     * @return 
	     */  
	    public  <T> T blockingPop(String key, Class<T> clazz) {
	        String value = stringRedisTemplate.opsForList().leftPop(key, 0, TimeUnit.SECONDS);
	        return parseJson(value, clazz);
	    }
	     
	    
	    /** 
	     * 入队 
	     *  
	     * @param key 
	     * @param value 
	     * @return 
	     */  
	    public <T> long rightPush(String key, T obj) {
	        return stringRedisTemplate.opsForList().rightPush(key, toJson(obj));
	    }
	    
	    /** 
	     * 栈/队列长 
	     *  
	     * @param key 
	     * @return 
	     */  
	    public Long length(String key) {  
	        return stringRedisTemplate.opsForList().size(key);  
	    }  
	    
	    
	    /**
	     * <p>Title: removeEntity</br>
	     * <p>Description: (删除指定list中的value数量 count删除个数)</br>
	     * @param key
	     * @param count
	     * @param obj 
	     */
	    public  Long removeEntity(String key, int count, Object obj) {
	        if (obj == null) {
	            return null;
	        }
	        return stringRedisTemplate.opsForList().remove(key, count, toJson(obj));
	    }
	     
	    /* ----------- zset --------- */
	    public  int zcard(String key) {
	        return stringRedisTemplate.opsForZSet().zCard(key).intValue();
	    }
	     
	    public  <T> List<T> zrange(String key, long start, long end, Class<T> clazz) {
	        Set<String> set = stringRedisTemplate.opsForZSet().range(key, start, end);
	        return parseJsonList(setToList(set), clazz);
	    }
	     
	    private  List<String> setToList(Set<String> set) {
	        if (set == null) {
	            return null;
	        }
	        return new ArrayList<String>(set);
	    }
	     
	    public  void zadd(String key, Object obj, double score) {
	        if (obj == null) {
	            return;
	        }
	        stringRedisTemplate.opsForZSet().add(key, toJson(obj), score);
	    }
	     
	    public  void zaddAll(String key, List<TypedTuple<?>> tupleList, Long timeout, TimeUnit unit) {
	        if (tupleList == null || tupleList.isEmpty()) {
	            return;
	        }
	         
	        Set<TypedTuple<String>> tupleSet = toTupleSet(tupleList);
	        stringRedisTemplate.opsForZSet().add(key, tupleSet);
	        if (timeout != null) {
	        	stringRedisTemplate.expire(key, timeout, unit);
	        }
	    }
	     
	    private  Set<TypedTuple<String>> toTupleSet(List<TypedTuple<?>> tupleList) {
	        Set<TypedTuple<String>> tupleSet = new LinkedHashSet<TypedTuple<String>>();
	        for (TypedTuple<?> t : tupleList) {
	            tupleSet.add(new DefaultTypedTuple<String>(toJson(t.getValue()), t.getScore()));
	        }
	        return tupleSet;
	    }
	     
	    public  void zrem(String key, Object obj) {
	        if (obj == null) {
	            return;
	        }
	        stringRedisTemplate.opsForZSet().remove(key, toJson(obj));
	    }
	     
	    public  void unionStore(String destKey, Collection<String> keys, Long timeout, TimeUnit unit) {
	        if (keys == null || keys.isEmpty()) {
	            return;
	        }
	         
	        Object[] keyArr = keys.toArray();
	        String key = (String) keyArr[0];
	         
	        Collection<String> otherKeys = new ArrayList<String>(keys.size() - 1);
	        for (int i = 1; i < keyArr.length; i++) {
	            otherKeys.add((String) keyArr[i]);
	        }
	         
	        stringRedisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
	        if (timeout != null) {
	        	stringRedisTemplate.expire(destKey, timeout, unit);
	        }
	    }
	     
	    /* ----------- tool methods --------- */
	    public  String toJson(Object obj) {
//	        return JSON.toJSONString(obj, SerializerFeature.SortField);
	    	return gson.toJson(obj);
	    }
	 
	    public  <T> T parseJson(String json, Class<T> clazz) {
//	        return JSON.parseObject(json, clazz);
	        return gson.fromJson(json, clazz);
	    }
	    
	    public  <T> T parseTypeJson(String json, Type type) {
//	        return JSON.parseObject(json, clazz);
	        return gson.fromJson(json, type);
	    }
	 
	    public  List<String> toJsonList(Collection<?> values) {
	        if (values == null) {
	            return null;
	        }
	 
	        List<String> result = new ArrayList<String>();
	        for (Object obj : values) {
	            result.add(toJson(obj));
	        }
	        return result;
	    }
	     
	    public  <T> List<T> parseJsonList(List<String> list, Class<T> clazz) {
	        if (list == null) {
	            return null;
	        }
	 
	        List<T> result = new ArrayList<T>();
	        for (String s : list) {
	            result.add(parseJson(s, clazz));
	        }
	        return result;
	    }
	
}
