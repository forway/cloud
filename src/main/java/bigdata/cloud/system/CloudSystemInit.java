package bigdata.cloud.system;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.springframework.stereotype.Component;

import bigdata.cloud.es.pool.ESClientPool;

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
		//RedisClientPool.initPool();
		
		logger.info("cloud system init finished.");
		
		Client client = ESClientPool.getInstance().getClient();
    	System.out.println(((TransportClient)client).connectedNodes().size());

    	
    	String index = "linux-syslog-2016.12.20";
    	String type = "logs";
    	ClusterState cs = client.admin().cluster().prepareState().setIndices(index).execute().actionGet()
				.getState();
		IndexMetaData imd = cs.getMetaData().index(index);
		MappingMetaData mdd = imd.mapping(type);
		Map<String, Object> map = null;
		try
		{
			map = mdd.getSourceAsMap();
			for(String s : map.keySet()){
				System.out.println(s + " --- " + map.get(s));
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("aa  -----  " + ESClientPool.pool.getBorrowedCount());
		System.out.println("aa  -----  " + ESClientPool.pool.getReturnedCount());
		ESClientPool.getInstance().release(client);
		System.out.println("bb  -----  " + ESClientPool.pool.getBorrowedCount());
		System.out.println("bb  -----  " + ESClientPool.pool.getReturnedCount());
	}
	
	

}
