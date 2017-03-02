package bigdata.cloud.es.pool;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import bigdata.cloud.system.CloudSystemConfig;

public class ESClientPool{
	
	private static ESClientPoolableObjectFactory poolFactory;
	public static GenericObjectPool<Client> pool;
	//使用单例模式
	private static ESClientPool esClientPool;

	private ESClientPool() {
		// 初始化
		if(pool == null || pool.getBorrowedCount() == 0){
			initPool();
		}
	}
	
	/**
	 * 初始化资源池
	 */
	public static void initPool(){
		
		Settings esSettings = Settings.settingsBuilder().put("cluster.name", CloudSystemConfig.es_clusterName)
				.put("client.transport.sniff", true).build();
		//设置多个host，如果某个host出现连接问题，其它host还可以使用
		InetSocketTransportAddress[] addressArray = getAllAddress(CloudSystemConfig.es_hosts.split(","), CloudSystemConfig.es_port);
		
		poolFactory = new ESClientPoolableObjectFactory(esSettings, addressArray);
		pool = new GenericObjectPool<Client>(poolFactory);
		pool.setMaxIdle(10);
		pool.setMinIdle(5);
		pool.setMaxTotal(10);
	}
	
	/**
	 * 单例模式获取ES客户端对象池
	 * @return
	 */
	public static ESClientPool getInstance(){
		if(esClientPool == null){
			esClientPool = new ESClientPool();
		}
		return esClientPool;
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
	 * 释放连接资源
	 * @return
	 */
	public void release(Client c){
		pool.returnObject(c);
	}
	/**
	 * 释放连接资源
	 * @return
	 */
	public void close(Client c){
		pool.returnObject(c);
	}
	
	/**
     * 获得所有的地址端口的netSocketTransportAddress数组
     *
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
