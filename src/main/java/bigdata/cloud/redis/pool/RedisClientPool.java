package bigdata.cloud.redis.pool;

import bigdata.cloud.system.CloudSystemConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis客户端对象池
 * @author hongliang
 *
 */
public class RedisClientPool {
	
	//使用单例模式
	private static RedisClientPool redisClientPool;
	private static JedisPool pool;
	
	private RedisClientPool(){
		//初始化
		initPool();
	}
	
	/**
	 * 单例模式获取redis客户端对象池
	 * @return
	 */
	public static RedisClientPool getInstance(){
		if(redisClientPool == null){
			redisClientPool = new RedisClientPool();
		}
		return redisClientPool;
	}
	
	/**
	 * 获取redis客户端资源
	 * @return
	 */
	public Jedis getClient(){
		if(pool == null){
			initPool();
		}
		return pool.getResource();
	}
	
	/**
	 * 初始化redis 客户端池
	 * @return
	 */
	public static void initPool(){
		if(pool == null){
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxIdle(5000);
			config.setMaxTotal(50000);
			config.setMaxWaitMillis(CloudSystemConfig.redis_timeout);
			config.setTestOnBorrow(true);
			
			pool = new JedisPool(config, CloudSystemConfig.redis_host, CloudSystemConfig.redis_port,
					CloudSystemConfig.redis_timeout, CloudSystemConfig.redis_password);
		}
	}

}
