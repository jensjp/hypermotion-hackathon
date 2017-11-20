/**
 * 
 */
package io.hym.optisls.sources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import io.hym.optisls.model.Coordinates;
import io.hym.optisls.model.Event;
import io.hym.optisls.model.Supplier;

/**
 * @author jens
 *
 */
@SuppressWarnings("all")
public class FlightSources {

	static final String PHQ_DFS = "yyyy-MM-dd";
	static final DateFormat PHQ_DF = new SimpleDateFormat(PHQ_DFS);

	public static void retrieveAllFlight(String origin, String destination, String dateTime,String product) throws Exception{
		
		
		
		
	}
	
	public static Map<String, Object> retrieveFlights(String origin, String destination, String dateTime,String product) throws Exception{
		WebClient client = WebClient.create("https://api.lufthansa.com/v1/operations/schedules/"
				+ origin +"-"
				+ destination
				+ "2017-11-23?directFlights=0");
		client.accept(MediaType.APPLICATION_JSON);
		
		
		client.header("Authorization", "Bearer O1fYKqxCyOGAEJWzJPqDOoJRANZD41");
		
		Response resp = client.get();
		Map<String, Object> data = mapToJson((InputStream)resp.getEntity());
		client.close();
		return data;
	}

	public static ObjectMapper createJacksonJsonMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		//mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		//mapper.setVisibility(PropertyAccessor.FIELD, Visibility.PUBLIC_ONLY);
		//mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		return mapper;
	}
	
	private static Map<String, Object> mapToJson(InputStream is) throws Exception{
		ObjectMapper mapper = createJacksonJsonMapper();
		TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
		HashMap<String,Object> data = mapper.readValue(is, typeRef);
		//Object x = mapper.readValue(is, String.class);
		return data;
	}
	
}
