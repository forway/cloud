package bigdata.cloud.es.pool;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import bigdata.cloud.system.CloudSystemConfig;
/**
 * 
 * @author hongliang
 * ES client 对象池  
 * 使用说明：获取es client对象进行查询等操作后，需要释放client对象
 * 获取es client对象：getClient()
 * 释放es client对象：release(Client c)
 *
 */
public class ESClientPool{
	
	private static GenericObjectPool<Client> pool;
	//使用单例模式

	private ESClientPool() {
		// 初始化
		initPool();
	}
	
	/**
	 * 使用静态内部类实现单例模式
	 *
	 */
	private static class SingletonHolder {
		private static ESClientPool esClientPool = new ESClientPool();  
	}
	
	/**
	 * 单例模式获取ES客户端对象池
	 * @return
	 */
	public static ESClientPool getInstance(){
		return SingletonHolder.esClientPool;
	}
	
	/**
	 * 从ES客户端对象池获取一个ES客户端对象
	 * @return
	 */
	public Client getClient(){
		try {
			return pool.borrowObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 释放ES client连接资源
	 * @return
	 */
	public void release(Client c){
		if(c != null){
			pool.returnObject(c);
		}
	}
	
	/**
	 * 初始化资源池
	 */
	private static void initPool(){
		
		if(pool == null || pool.isClosed()){
			Settings esSettings = Settings.settingsBuilder().put("cluster.name", CloudSystemConfig.es_clusterName)
					.put("client.transport.sniff", true).build();
			//设置多个host，如果某个host出现连接问题，其它host还可以使用
			InetSocketTransportAddress[] addressArray = getAllAddress(CloudSystemConfig.es_hosts.split(","), CloudSystemConfig.es_port);
			
			ESClientPoolableObjectFactory poolFactory = new ESClientPoolableObjectFactory(esSettings, addressArray);
			GenericObjectPoolConfig config = new GenericObjectPoolConfig();
			config.setMaxTotal(CloudSystemConfig.es_clientPoolSize); //最大连接数
			config.setMaxIdle(CloudSystemConfig.es_clientPoolSize);	//链接池中最大空闲的连接数，默认为8.该参数一般尽量与_maxActive相同，以提高并发数
			config.setMinIdle(1);	//连接池中最少空闲的连接数，默认为0
			config.setMaxWaitMillis(120 * 1000);	//当连接池资源耗尽时，调用者最大阻塞的时间，超时将跑出异常。单位，毫秒数；默认为-1.表示永不超时
			config.setSoftMinEvictableIdleTimeMillis(30 * 60 * 1000);	//连接空闲的最小时间，达到此值后空闲链接将会被移除，且保留“minIdle”个空闲连接数。默认为-1.
			pool = new GenericObjectPool<Client>(poolFactory, config);
		}
	}
	
	/**
     * 获得所有的地址端口的netSocketTransportAddress数组
     * @return
     */
    private static InetSocketTransportAddress[] getAllAddress(String[] hostArray, int es_port) {
        InetSocketTransportAddress[] addressList = new InetSocketTransportAddress[hostArray.length];
        try {
        	for(int i=0; i<hostArray.length; i++){
        		addressList[i] = new InetSocketTransportAddress(InetAddress.getByName(hostArray[i]), es_port);
        	}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
        return addressList;
    }
	
}
