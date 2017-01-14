package bigdata.cloud.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 配置文件操作工具类
 * @author hongliang
 *
 */
public class CloudConfigUtil {
	
	/**
	 * 读取指定的属性文件，把内容转为key：value对的Map结构
	 * @param propertiesFilePath	属性文件路径
	 * @return	Map<String,String> 对应 key ： value
	 */
	public static Map<String,String> getPropertiesMap(String propertiesFilePath) {
		
		Map<String,String> paramsMap = new HashMap<String,String>();
		Properties properties = new Properties();
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(new File(propertiesFilePath));
			properties.load(fileInputStream);
			//获取属性文件所有的key值
			Set<String> names = properties.stringPropertyNames();
			for(String name : names) {
				paramsMap.put(name, properties.getProperty(name));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != fileInputStream) {
					fileInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return paramsMap;
	}
	
	/**
	 * 读取配置文件，返回Map
	 * @param filePath	配置文件路径
	 * @param splitStr	文件参数中key和value的分隔符，如：key:value的分隔符是:
	 * @return
	 */
	public static Map<String,String> getConfigFileMap(String filePath, String splitStr){
		Map<String, String> map = new HashMap<String, String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			String line = br.readLine();
			while(line != null){
				String[] lineArray = line.split(splitStr);
				map.put(lineArray[0], lineArray[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

}
