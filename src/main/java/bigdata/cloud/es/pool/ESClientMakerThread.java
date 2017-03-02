package bigdata.cloud.es.pool;

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
	private Settings settings;
	private InetSocketTransportAddress[] addressArray;

	public ESClientMakerThread(Settings settings, InetSocketTransportAddress[] addressArray) {
		super();
		this.settings = settings;
		this.addressArray = addressArray;
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
		TransportClient transportClient = TransportClient.builder().settings(this.settings).build();
		transportClient.addTransportAddresses(this.addressArray);
		return transportClient;
	}
	
	
}
