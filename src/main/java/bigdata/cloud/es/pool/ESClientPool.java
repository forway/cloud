package bigdata.cloud.es.pool;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import bigdata.cloud.system.CloudSystemConfig;
/**
 * es客户端对象池
 * @author hongliang
 *
 */
public class ESClientPool {
	
	//存放es client的队列
	private static BlockingQueue<Client> esClientQueue = new ArrayBlockingQueue<Client>(CloudSystemConfig.es_clientPoolSize);
	//使用单例模式
	private static ESClientPool esClientPool;

	private ESClientPool(){
		//初始化
		initPool();
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
		return getClientFromQueue();
	}
	
	/**
	 * 初始化
	 */
	public static void initPool() {
		if (esClientQueue.isEmpty()) {
			// 初始化队列esClientQueue
			makeClients(CloudSystemConfig.es_hosts, CloudSystemConfig.es_port, CloudSystemConfig.es_clusterName, CloudSystemConfig.es_clientPoolSize);
		}
	}
	
	/**
	 * 从队列中获取一个ES客户端对象
	 * @return
	 */
	private static Client getClientFromQueue() {
		synchronized (esClientQueue) {
			if (!esClientQueue.isEmpty()) {
				Client client = esClientQueue.poll();
				//计算出剩余容量，如果剩余容量大于0，则往队列中加入es客户端对象
				int space = esClientQueue.remainingCapacity() - esClientQueue.size();
				if(space > 0){
					makeClients(CloudSystemConfig.es_hosts, CloudSystemConfig.es_port, CloudSystemConfig.es_clusterName, space);
				}
				return client;
			} else {
				makeClients(CloudSystemConfig.es_hosts, CloudSystemConfig.es_port, CloudSystemConfig.es_clusterName, CloudSystemConfig.es_clientPoolSize);
				return esClientQueue.poll();
			}
		}
	}
	
	/**
	 * 创建es客户端对象，并加入到队列ESClientQueue中
	 * @param es_hosts
	 * @param es_port
	 * @param es_cluster_name
	 * @param es_client_pool_size
	 */
	private static void makeClients(String es_hosts, int es_port, String es_cluster_name, int es_client_pool_size) {
		
		Settings settings = Settings.settingsBuilder().put("cluster.name", es_cluster_name).put("client.transport.sniff", true).build();
		//设置多个host，如果某个host出现连接问题，其它host还可以使用
		InetSocketTransportAddress[] addressArray = getAllAddress(es_hosts.split(","), es_port);
		//创建容量可变的线程池
		ExecutorService executorService = Executors.newCachedThreadPool();
		//用于存放每个线程创建返回的状态，包括线程放回结果
		List<Future<Client>> futures = new ArrayList<Future<Client>>();
		for (int i = 0; i < es_client_pool_size; i++) {
			futures.add(executorService.submit(new ESClientMakerThread(settings, addressArray)));
		}
		try {
			for (int i = 0; i < futures.size(); i++) {
				esClientQueue.add(futures.get(i).get());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		executorService.shutdown();
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
