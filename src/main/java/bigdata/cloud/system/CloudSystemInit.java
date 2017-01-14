package bigdata.cloud.system;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import bigdata.cloud.es.pool.ESClientPool;
import bigdata.cloud.redis.pool.RedisClientPool;

/**
 * cloud系统初始化类
 * @author hongliang
 *
 */
@Component
public class CloudSystemInit {
	
	private static Logger logger = Logger.getLogger(CloudSystemInit.class);
	
	/**
	 * 系统启动后自动执行
	 */
	@PostConstruct
	public void initAllInfo(){
		logger.info("cloud system init ......");
		//加载配置文件信息
		CloudSystemConfig.initConfigInfo();
		//初始化ES客户端对象池
		ESClientPool.initPool();
		//初始化redis客户端对象池
		RedisClientPool.initPool();
		
		logger.info("cloud system init finished.");
	}
	
	

}
