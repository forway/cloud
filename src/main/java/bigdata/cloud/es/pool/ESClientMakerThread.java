package bigdata.cloud.es.pool;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
/**
 * es client创建线程类
 * @author hongliang
 *
 */
public class ESClientMakerThread implements Callable<Client>
{
	private String es_hosts;
	private int es_port;
	private String es_cluster_name;

	public ESClientMakerThread(String es_hosts, int es_port, String es_cluster_name) {
		super();
		this.es_hosts = es_hosts;
		this.es_port = es_port;
		this.es_cluster_name = es_cluster_name;
	}

	@Override
	public Client call() throws Exception {
		return createClient();
	}

	/**
	 * 创建es客户端对象
	 * @return
	 */
	private Client createClient(){
		Settings settings = Settings.settingsBuilder().put("cluster.name", this.es_cluster_name).build();
		TransportClient transportClient = TransportClient.builder().settings(settings).build();
		String[] hostArray = this.es_hosts.split(",");
		try{
			/**
			 * 设置多个host，如果某个host出现连接问题，其它host还可以使用
			 */
			for(String host : hostArray){
				transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), this.es_port));
			}
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Client client = transportClient;
		return client;
	}
}
