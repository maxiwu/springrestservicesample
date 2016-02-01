package helloworldmvc;

import java.io.Console;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

@RestController
@RequestMapping("/rest")
public class HomeController {

	private MongoClient getMongoClient() throws UnknownHostException {
		

		String mongoUri = "mongodb://127.0.0.1:27017/";

		return new MongoClient(new MongoClientURI(mongoUri));
	}

	final String DB_NAME = "pass";

	@RequestMapping(value = "/1/classes/{className}", method = RequestMethod.POST)
	@ResponseBody
	public DBObject mongoSaveObject(@PathVariable String className,
			@RequestBody DBObject dbo) {

		System.out.println("POST recieve");
		try {
			MongoClient mongoClient = getMongoClient();
			DB db = mongoClient.getDB(DB_NAME);

			// add id before insert
			ObjectId id = new ObjectId();
			dbo.put("_id", id);

			//should have check the structure of the JSON, parse don't allow different structure in 1 class
			
			// just like table without schema, group JSON together, like class
			// name in parse object
			DBCollection cc = db.getCollection(className);
			cc.insert(dbo);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dbo;
	}

	@RequestMapping(value = "/1/classes/{className}/{objectId}", method = RequestMethod.GET)
	@ResponseBody
	public DBObject mongoFindService(@PathVariable String className,
			@PathVariable String objectId) {
		//System.out.println("GET recieve");
		try {
			MongoClient mongoClient = getMongoClient();
			DB db = mongoClient.getDB(DB_NAME);

			DBCollection cc = db.getCollection(className);
			// if object id is not correct, it return 503
			DBObject dbo = cc.findOne(new ObjectId(objectId));

			return dbo;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/1/classes/{className}", method = RequestMethod.GET)
	@ResponseBody
	public List<DBObject> mongoQueryService(@PathVariable String className,
			@RequestBody(required = false) DBObject query) {
		System.out.println("GET recieve");
		try {
			System.out.println("query request reicieved");
			MongoClient mongoClient = getMongoClient();
			DB db = mongoClient.getDB(DB_NAME);

			DBCollection cc = db.getCollection(className);

			List<DBObject> list = null;
			DBCursor dbc = null;
			if (query == null) {
				// return all result
				dbc = cc.find();
			} else {
				// if object id is not correct, it return 503
				dbc = cc.find(query);
			}
			list = dbc.toArray();
			dbc.close();

			return list;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/1/classes/{className}/{objectId}", method = RequestMethod.PUT)
	@ResponseBody
	public DBObject mongoUpdateService(@PathVariable String className,
			@PathVariable String objectId, @RequestBody DBObject dbo) {

		
		/*!!!!Parse check for type and update a field
		but MONGO just replace the whole object!!!*/
		
		try {
			MongoClient mongoClient = getMongoClient();
			DB db = mongoClient.getDB(DB_NAME);

			DBCollection cc = db.getCollection(className);
			
			BasicDBObject searchQuery = new BasicDBObject().append("_id",
					new ObjectId(objectId));
			String updateCmd = String.format("{$set:%s}",dbo.toString());
			
			//should parse the command, use $exists to update only if field exists
			
			DBObject updateObj = (DBObject) JSON.parse(updateCmd);
			WriteResult wresult = cc.update(searchQuery, updateObj);

			// parse has a timestamp updateAt

			return dbo;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/1/classes/{className}/{objectId}", method = RequestMethod.DELETE)
	@ResponseBody
	public String mongoDelService(@PathVariable String className,
			@PathVariable String objectId) {

		try {
			MongoClient mongoClient = getMongoClient();
			DB db = mongoClient.getDB(DB_NAME);

			DBCollection cc = db.getCollection(className);
			BasicDBObject searchQuery = new BasicDBObject().append("_id",
					new ObjectId(objectId));
			WriteResult wresult = cc.remove(searchQuery);

			return wresult.getN() + " record removed.";
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


}
