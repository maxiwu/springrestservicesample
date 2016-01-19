package helloworldmvc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class Mongo2MessageConverter extends
		AbstractHttpMessageConverter<Object> implements
		GenericHttpMessageConverter<Object> {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	public Mongo2MessageConverter() {
		super(new MediaType("application", "json", DEFAULT_CHARSET),
				new MediaType("application", "*+json", DEFAULT_CHARSET));
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return canRead(mediaType);
	}

	@Override
	public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
		return canRead(mediaType);
	}

	@Override
	public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
		// TODO Auto-generated method stub
		return canWrite(mediaType);
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		// should not be called, since we override canRead/Write instead
		throw new UnsupportedOperationException();
	}

	@Override
	public Object read(Type type, Class<?> contextClass,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {

		TypeToken<?> token = getTypeToken(type);
		return readTypeToken(token, inputMessage);
	}

	@Override
	protected Object readInternal(Class<? extends Object> clazz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		// TODO Auto-generated method stub
		TypeToken<?> token = getTypeToken(clazz);
		return readTypeToken(token, inputMessage);
	}

	// handle write
	@Override
	protected void writeInternal(Object o, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		// TODO Auto-generated method stub
		Charset charset = getCharset(outputMessage.getHeaders());
		OutputStreamWriter writer = new OutputStreamWriter(
				outputMessage.getBody(), charset);

		/*
		 * try { if (this.jsonPrefix != null) { writer.append(this.jsonPrefix);
		 * } this.gson.toJson(o, writer); writer.close(); }
		 * catch(JsonIOException ex) { throw new
		 * HttpMessageNotWritableException("Could not write JSON: " +
		 * ex.getMessage(), ex); }
		 */
	}
	
	protected void writeInternal(List<Object> o, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		// TODO Auto-generated method stub
		Charset charset = getCharset(outputMessage.getHeaders());
		OutputStreamWriter writer = new OutputStreamWriter(
				outputMessage.getBody(), charset);

		/*
		 * try { if (this.jsonPrefix != null) { writer.append(this.jsonPrefix);
		 * } this.gson.toJson(o, writer); writer.close(); }
		 * catch(JsonIOException ex) { throw new
		 * HttpMessageNotWritableException("Could not write JSON: " +
		 * ex.getMessage(), ex); }
		 */
	}

	protected TypeToken<?> getTypeToken(Type type) {
		return TypeToken.get(type);
	}

	// handle read
	private Object readTypeToken(TypeToken<?> token,
			HttpInputMessage inputMessage) throws IOException {
		/*Reader json = new InputStreamReader(inputMessage.getBody(),
				getCharset(inputMessage.getHeaders()));*/
		//return null;

		try {			
			Object object = JSON.parse(readToEnd(inputMessage.getBody()));
			//return this.gson.fromJson(json, token.getType());
			return object;
		} catch (JsonParseException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: "
					+ ex.getMessage(), ex);
		}

	}

	private Charset getCharset(HttpHeaders headers) {
		if (headers == null || headers.getContentType() == null
				|| headers.getContentType().getCharSet() == null) {
			return DEFAULT_CHARSET;
		}
		return headers.getContentType().getCharSet();
	}

	@Override
	public void write(Object t, Type type, MediaType contentType,
			HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {
		// TODO Auto-generated method stub

		TypeToken<?> token = getTypeToken(type);
		//com.google.gson.reflect.TypeToken
		//System.out.println(token.getClass().getName());
		String rawName = token.getRawType().getName();
		//java.util.List
		//System.out.println(rawName);

		/*if(token.getRawType() instanceof  )*/
		writeInternal(t, outputMessage);
	}
	
	protected String readToEnd(InputStream is) throws IOException {
		try (java.util.Scanner s = new java.util.Scanner(is)) {
			// regex "\A" matches the beginning of the input
			s.useDelimiter("\\A");
			String result = s.hasNext() ? s.next() : "";
			
			return result;
		} catch (Exception e) {
			return e.getMessage();
		}
		

	}
}
