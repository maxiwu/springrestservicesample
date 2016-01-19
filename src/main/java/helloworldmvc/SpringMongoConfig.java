package helloworldmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
public class SpringMongoConfig {
	public @Bean
	MongoDbFactory mongoDbFactory() throws Exception {
		/*String mongoUri = "mongodb://$OPENSHIFT_MONGODB_DB_HOST:$OPENSHIFT_MONGODB_DB_PORT/";
		
		return new SimpleMongoDbFactory(new MongoClient(new MongoClientURI(mongoUri)), "pass");*/
		return new SimpleMongoDbFactory(new MongoClient("127.0.0.1"), "test");
		
		//let's test openshift
		//mongodb://$OPENSHIFT_MONGODB_DB_HOST:$OPENSHIFT_MONGODB_DB_PORT/
		/*MongoClient mongoClient = new MongoClient();
		DB db = mongoClient.getDB("database name");
		boolean auth = db.authenticate("username", "password".toCharArray());*/
		
	}

	public @Bean
	MongoTemplate mongoTemplate() throws Exception {
		
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
				
		return mongoTemplate;
		
	}
}
