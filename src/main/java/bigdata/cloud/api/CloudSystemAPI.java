package bigdata.cloud.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import bigdata.cloud.system.CloudSystemConfig;
import bigdata.cloud.system.Version;

/**
 * 系统信息api类
 * 
 * @author hongliang
 *
 */
@RestController
@RequestMapping("/cloud/system")
public class CloudSystemAPI
{
	@Autowired
	Version version;

	/**
	 * 获取版本号等信息
	 * @return
	 */
	@RequestMapping("/version")
	@ResponseBody
	public Version getVersion()
	{
		return version;
	}
	
	/**
	 * 获取es配置信息
	 * @return
	 */
	@RequestMapping("/es")
	@ResponseBody
	public String getESConfigInfo()
	{
		String es_msg = "hosts : " + CloudSystemConfig.es_hosts + 
						"port : " + CloudSystemConfig.es_port + 
						"clientPoolSize : " + CloudSystemConfig.es_clientPoolSize + 
						"clusterName : " + CloudSystemConfig.es_clusterName;
		return es_msg;
	}
}
