package bigdata.cloud.es.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * 
 * @author hongliang
 * ES client 对象池工厂
 *
 */
public class ESClientPoolableObjectFactory implements PooledObjectFactory<Client> {
	
	private static Logger logger = Logger.getLogger(ESClientPoolableObjectFactory.class);

	private Settings esSettings;
	InetSocketTransportAddress[] addressArray;

	public ESClientPoolableObjectFactory(){
		
	}
	
	public ESClientPoolableObjectFactory(Settings esSettings, InetSocketTransportAddress[] addressArray) {
		super();
		this.esSettings = esSettings;
		this.addressArray = addressArray;
	}

	@Override
	public PooledObject<Client> makeObject() throws Exception {
		logger.info("es pool : ------ create an new es client ------");
		TransportClient transportClient = TransportClient.builder().settings(this.esSettings).build();
		transportClient.addTransportAddresses(this.addressArray);
		return new DefaultPooledObject<Client>(transportClient);
	}

	@Override
	public void destroyObject(PooledObject<Client> p) throws Exception {
		if(p.getObject() != null) {
			logger.info("es pool : ------ destroy an es client ------");
			p.getObject().close();
		}
	}

	@Override
	public boolean validateObject(PooledObject<Client> p) {
		return p.getObject() != null;
	}

	@Override
	public void activateObject(PooledObject<Client> p) throws Exception {
		logger.info("es pool : ------ get an es client ------");
	}

	@Override
	public void passivateObject(PooledObject<Client> p) throws Exception {
		logger.info("es pool : ------ release an es client ------");
	}

	//get set function
	public Settings getEsSettings() {
		return esSettings;
	}

	public void setEsSettings(Settings esSettings) {
		this.esSettings = esSettings;
	}

	public InetSocketTransportAddress[] getAddressArray() {
		return addressArray;
	}

	public void setAddressArray(InetSocketTransportAddress[] addressArray) {
		this.addressArray = addressArray;
	}
	
}
