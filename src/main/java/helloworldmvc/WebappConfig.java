package helloworldmvc;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.mongodb.DBObject;

@EnableWebMvc
@Configuration
@ComponentScan({ "helloworldmvc" })
public class WebappConfig extends WebMvcConfigurerAdapter {

	// do not need view resolver for REST api

	// register the custom message converter
/*	@Override
	public void extendMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		
		
		//super.extendMessageConverters(converters);
		//converters.add(0, new MongoMessageConverter());
		converters.add(new MongoMessageConverter());
	}*/
	
	
	@Override
	public void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		
		
		//super.extendMessageConverters(converters);
		//converters.add(0, new MongoMessageConverter());
		converters.add(new MongoMessageConverter());
	}
}
