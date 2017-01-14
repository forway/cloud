package bigdata.cloud.system;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系统版本信息
 * @author hongliang
 *
 */
@Component
@ConfigurationProperties(prefix="version",locations="classpath:version.yml")
public class Version
{
	private String build_num;
	private String build_date;
	private String build_name;
	private String copyright;
	
	public String getBuild_num() {
		return build_num;
	}
	public void setBuild_num(String build_num) {
		this.build_num = build_num;
	}
	public String getBuild_date() {
		return build_date;
	}
	public void setBuild_date(String build_date) {
		this.build_date = build_date;
	}
	public String getBuild_name() {
		return build_name;
	}
	public void setBuild_name(String build_name) {
		this.build_name = build_name;
	}
	public String getCopyright() {
		return copyright;
	}
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	
}
