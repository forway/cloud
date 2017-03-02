package bigdata.cloud.es.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * 
 * @author Administrator
 *
 */
public class ESClientPoolableObjectFactory implements PooledObjectFactory<Client> {

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
		TransportClient transportClient = TransportClient.builder().settings(this.esSettings).build();
		transportClient.addTransportAddresses(this.addressArray);
		return new DefaultPooledObject<Client>(transportClient);
	}

	@Override
	public void destroyObject(PooledObject<Client> p) throws Exception {
		
		if(p.getObject() != null) {
			p.getObject().close();
		}
	}

	@Override
	public boolean validateObject(PooledObject<Client> p) {
		
		return p.getObject() != null;
	}

	@Override
	public void activateObject(PooledObject<Client> p) throws Exception {
		
	}

	@Override
	public void passivateObject(PooledObject<Client> p) throws Exception {
		
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