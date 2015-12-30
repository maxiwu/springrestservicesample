package helloworldmvc;

import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class HomeController {
	
	@RequestMapping("/hello")
	public String helloService()
	{
		return "hello from spring rest service";
	}
	
	// need Message Conversion!!
	@RequestMapping(value = "/xpb124/v1", method = RequestMethod.POST, consumes="application/json")
	public ResponseEntity<String> redirectPostService(@RequestBody RequestBodyWrapper requestWrapper) {

		
		//System.out.println(getCurrentDateStr()+": receive POST request");
		try {
			//executeGet(String.format("https://%s", requestWrapper.getCmd()), "");
			System.out.println(requestWrapper.getCmd());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ResponseEntity<String> response = new ResponseEntity<String>(e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
			//System.out.println(getCurrentDateStr()+": executeGet fail on POST input");			
			return response;
		}

		ResponseEntity<String> response = new ResponseEntity<String>("OK", HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/xpb124/echo", method = RequestMethod.POST, consumes="application/json")
	public ResponseEntity<String> echoPostService(@RequestBody RequestBodyWrapper requestWrapper) {

		
		//System.out.println(getCurrentDateStr()+": receive POST request");
		try {
			//executeGet(String.format("https://%s", requestWrapper.getCmd()), "");
			System.out.println(requestWrapper.getCmd());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ResponseEntity<String> response = new ResponseEntity<String>(e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
			//System.out.println(getCurrentDateStr()+": executeGet fail on POST input");			
			return response;
		}

		ResponseEntity<String> response = new ResponseEntity<String>(requestWrapper.getCmd(),HttpStatus.OK);
		return response;
	}
	
	public void executeGet(String targetURL, String urlParameters) {
		HttpURLConnection connection = null;
		try {
			// Create connection
			String urlstr = "";
			if (urlParameters == null || urlParameters.isEmpty()) {
				urlstr = targetURL;
			} else {
				urlstr = targetURL + "?" + urlParameters;
			}
			URL url = new URL(urlstr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Get Response
			connection.getResponseCode();
			
		} catch (Exception e) {
			e.printStackTrace();
			// return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
