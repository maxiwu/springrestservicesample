package helloworldmvc;



import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class MongoMessageConverter implements HttpMessageConverter<Object> {

	@Override
	public boolean canRead(Class<?> aClass, MediaType mediaType) {
		boolean isGeneric = DBObject.class.isAssignableFrom(aClass);
		boolean isCollection = List.class.isAssignableFrom(aClass);
		return (isGeneric || isCollection);		
	}

	@Override
	public boolean canWrite(Class<?> aClass, MediaType mediaType) {
		//return DBObject.class.isAssignableFrom(aClass);
		boolean isGeneric = DBObject.class.isAssignableFrom(aClass);
		boolean isCollection = List.class.isAssignableFrom(aClass);
		return (isGeneric || isCollection);		
	}

	@Override
	public List<MediaType> getSupportedMediaTypes() {
		List<MediaType> supports = new ArrayList<MediaType>();
		supports.add(MediaType.APPLICATION_JSON);
		return supports;
	}

	@Override
	public DBObject read(Class<?> aClass,
			HttpInputMessage httpInputMessage) throws IOException,
			HttpMessageNotReadableException {
		Object object = JSON.parse(readToEnd(httpInputMessage.getBody()));
		if (object == null) {
			return new BasicDBObject();
		} else {
			return (DBObject) object;
		}
	}

	@Override
	public void write(Object o, MediaType mediaType,
			HttpOutputMessage httpOutputMessage) throws IOException,
			HttpMessageNotWritableException {
		//o could be DBObject or List<DBObject>
		// do not need to know, call toString() anyway
		httpOutputMessage.getBody().write(o.toString().getBytes());
	}

	protected String readToEnd(InputStream is) throws IOException {
		try (java.util.Scanner s = new java.util.Scanner(is)) {
			// regex "\A" matches the beginning of the input
			s.useDelimiter("\\A");
			String result = s.hasNext() ? s.next() : "";
			
			String queryStart = "where=";
			//handle query command
			if(result.startsWith(queryStart))
			{
				result = result.substring(queryStart.length());
				//System.out.println(result);
			}
			
			return result;
		} catch (Exception e) {
			return e.getMessage();
		}
		

	}
}
