package bigdata.cloud.system;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
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
		
		//test
		for(int i=0; i<2500; i++){
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Client client = ESClientPool.getInstance().getClient();

			    	String index = "elasticsearch-sql_test_index";  //elasticsearch-sql_test_index    linux-syslog-2016.12.20
			    	String type = "account";   //account   logs
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

					ESClientPool.getInstance().release(client);
					
				}
			}).start();
			
		}
		
		
		
		
		
	}
}
