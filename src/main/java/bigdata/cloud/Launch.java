package bigdata.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.MultipartConfigElement;

/**
 * spring boot 启动类
 * @author hongliang
 *
 */
@SpringBootApplication 
@EnableScheduling 
public class Launch
{
	@Bean
	public MultipartConfigElement multipartConfigElement()
	{
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("200MB");
		factory.setMaxRequestSize("200MB");
		return factory.createMultipartConfig();
	}

	public static void main(String[] args)
	{
		SpringApplication.run(Launch.class, args);
	}
}
