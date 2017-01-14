package bigdata.cloud.system;

import java.util.Map;

import bigdata.cloud.utils.CloudConfigUtil;

/**
 * 系统配置文件信息
 * @author hongliang
 *
 */
public class CloudSystemConfig {
	
	public static String CLOUD_CONFIG_PATH;		//配置文件路径
	
	//es的配置信息，从配置文件获取es信息
	private static final int ES_DEFAULT_POOL_SIZE = 5;		//es客户端池默认的大小
	public static String es_hosts;				//es hosts: host1,host2,host3
	public static int es_port;					//es port
	public static int es_clientPoolSize = 0;	//es client pool size
	public static String es_clusterName;		//es cluster name
	//redis配置信息
	public static String redis_host;		//redis host
	public static int redis_port;			//redis port
	public static String redis_password;	//redis password
	public static int redis_timeout;		//redis timeout
	
	public static void initConfigInfo(){
		//获取配置文件路径
		CLOUD_CONFIG_PATH = CloudSystemConfig.class.getClass().getResource("/cloud.properties").getPath();
		//从配置文件加载信息并赋值
		Map<String, String> map = CloudConfigUtil.getPropertiesMap(CLOUD_CONFIG_PATH);
		
		//es配置赋值
		es_hosts = map.get("cloud.es.hosts");
		es_port = Integer.parseInt(map.get("cloud.es.port"));
		es_clusterName = map.get("cloud.es.cluster.name");
		String size = map.get("cloud.es.client.pool.size");
		if(size != null && !"".equals(size)){
			es_clientPoolSize = Integer.parseInt(size);
		}
		if(es_clientPoolSize < ES_DEFAULT_POOL_SIZE){
			es_clientPoolSize = ES_DEFAULT_POOL_SIZE;
		}
		//redis配置赋值
		redis_host = map.get("cloud.redis.host");
		redis_port = Integer.parseInt(map.get("cloud.redis.port"));
		redis_password = map.get("cloud.redis.password");
		redis_timeout = Integer.parseInt(map.get("cloud.redis.timeout"));
		
	}

}
