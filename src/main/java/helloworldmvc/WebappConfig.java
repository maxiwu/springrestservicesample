package helloworldmvc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
@ComponentScan({"helloworldmvc"})
public class WebappConfig extends WebMvcConfigurerAdapter{

	//do not need view resolver for REST api
	
}
