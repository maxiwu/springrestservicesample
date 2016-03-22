package helloworldmvc;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpConnection;
import org.springframework.integration.ip.tcp.connection.TcpConnectionEvent;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

@EnableIntegration
@IntegrationComponentScan
@Configuration
@PropertySource("classpath:/config.properties")
public class IntegrationConfig implements
		ApplicationListener<TcpConnectionEvent> {
	@Value("${listen.port:8000}")
	private int port;

	//could use in-memory too
	@Autowired
	MongoClient dbStore;
	final String DB_NAME = "pass";
	final String DB_COLLECTIONNAME = "installations";

	@Bean
	public MessageChannel fromTcp() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel toTcp() {
		return new DirectChannel();
	}

	// receive from MVC controller
	@Bean
	public MessageChannel invokeChannel() {
		MessageChannel invokeChl = new DirectChannel();		
		//MessageChannel invokeChl = new ExecutorChannel(null);
		return invokeChl;
	}

	@Bean
	public TcpReceivingChannelAdapter in(
			AbstractServerConnectionFactory connectionFactory) {
		TcpReceivingChannelAdapter adapter = new TcpReceivingChannelAdapter();
		adapter.setOutputChannel(fromTcp());
		adapter.setConnectionFactory(connectionFactory);
		return adapter;
	}

	@Transformer(inputChannel = "fromTcp", outputChannel = "toCollaborate")
	public String convert(byte[] bytes) {
		return new String(bytes);
	}

	// MessageHeaders staticheader;
	@ServiceActivator(inputChannel = "toCollaborate", requiresReply="false")
	public Message<String> handleTcpMessage(Message<String> jsonMsg) {
		// save the header, collaborate to output channel
		MessageHeaders header = jsonMsg.getHeaders();
		// parse payload to JSON

		DB db = dbStore.getDB(DB_NAME);
		
		//should look up database, delete duplicate or update timestamp
		/*BasicDBObject searchQuery = new BasicDBObject().append("installationId",
				payload);*/
		DBObject dbo = (DBObject) JSON.parse(jsonMsg.getPayload());
		DBObject searchQuery = (DBObject) JSON.parse(jsonMsg.getPayload());
		Iterator it = header.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			dbo.put(pair.getKey().toString(), pair.getValue());
		}

		DBCollection cc = db.getCollection(DB_COLLECTIONNAME);
		//use upsert, installationId as search clause
		cc.update(searchQuery, dbo, true, false);

		return jsonMsg;
	}

	@Transformer(inputChannel = "invokeChannel", outputChannel = "toTcp")
	public Message<String> headerBeforeSend(String payload) {
		Message<String> msg = new GenericMessage<String>("hello");
		Message<String> msg2 = null;

		DB db = dbStore.getDB(DB_NAME);
		DBCollection cc = db.getCollection(DB_COLLECTIONNAME);	
		BasicDBObject searchQuery = new BasicDBObject().append("installationId",
				payload);
		DBObject info = cc.findOne(searchQuery);
		if (info != null) {
			msg2 = MessageBuilder
					.fromMessage(msg)
					.setHeader("ip_connectionId",
							info.get("ip_connectionId")).build();

		}
		return msg2;
	}

	@ServiceActivator(inputChannel = "toTcp", requiresReply="false")
	@Bean
	public TcpSendingMessageHandler out(
			AbstractServerConnectionFactory connectionFactory) {
		TcpSendingMessageHandler tcpOutboundAdp = new TcpSendingMessageHandler();
		tcpOutboundAdp.setConnectionFactory(connectionFactory);
		return tcpOutboundAdp;
	}

	// should need only 1 factory? and keep connectin alive
	// server for in coming connection
	@Bean
	public AbstractServerConnectionFactory serverCF() {
		return new TcpNetServerConnectionFactory(this.port);
	}

	@Override
	public void onApplicationEvent(TcpConnectionEvent tcpEvent) {
		// TODO Auto-generated method stub
		TcpConnection source = (TcpConnection) tcpEvent.getSource();

	}

}
