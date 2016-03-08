package helloworldmvc;

import java.net.UnknownHostException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@EnableWebMvc
@Configuration
@ComponentScan({ "helloworldmvc" })
public class WebappConfig extends WebMvcConfigurerAdapter {

	// do not need view resolver for REST api

	// register the custom message converter
	/*
	 * @Override public void extendMessageConverters(
	 * List<HttpMessageConverter<?>> converters) {
	 * 
	 * 
	 * //super.extendMessageConverters(converters); //converters.add(0, new
	 * MongoMessageConverter()); converters.add(new MongoMessageConverter()); }
	 */

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Override
	public void extendMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		converters.add(0,new MongoMessageConverter());		
	}
	
	@Bean
	public MongoClient dbStore() throws UnknownHostException
	{
		String mongoUri = "mongodb://127.0.0.1:27017/";
		return new MongoClient(new MongoClientURI(mongoUri));
	}
}
