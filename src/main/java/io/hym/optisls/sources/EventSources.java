/**
 * 
 */
package io.hym.optisls.sources;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
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

/**
 * @author jens
 *
 */
public class EventSources {

	
	public static void retrieveAllPHQEvents() throws Exception{
		List<Event> events = new ArrayList<>();
		for(int x = 0 ; x < 1000; x++){
			Map<String, Object> data = retrievePHQEvents(x);
			if(data == null || data.isEmpty() || data.get("next") == null)
				break;
			List<Map<String, Object>> results = (List<Map<String, Object>>) data.get("results");
			for(Map<String, Object> r : results){
				Event e = new Event();
				e.setDesc((String)r.get("title"));
				e.setKlass((String)r.get("category"));
				e.setSubKlass((String)r.get("category"));
				List<Object> coord = (List<Object>)r.get("location");
				e.setCoordinates(new Coordinates(Double.parseDouble(coord.get(0).toString()), Double.parseDouble(coord.get(1).toString())));
				events.add(e);
			}
		}
		System.out.println(events.size());
		BufferedWriter br = Files.newBufferedWriter(Paths.get("/Users/jens/tmp", "predictHq.csv"), StandardOpenOption.CREATE);
		for(Event e : events){
			br.write(e.encode());
			br.write("\n");
		}
		br.flush();
		br.close();
	}
	
	public static Map<String, Object> retrievePHQEvents(int offset) throws Exception{
		WebClient client = WebClient.create("https://api.predicthq.com/v1/events/");
		client.accept(MediaType.APPLICATION_JSON);
		
		client.query("categories", "expos,concerts,festivals,sports,airport-delays,severe-weather,disasters,terror");
		client.query("labels", "all");
		client.query("labels.op", "any");
		client.query("ranks", "level5");
		client.query("from", "2017-09-01");
		client.query("to", "2018-02-01");
		//client.query("date", "next30days");
		client.query("places", "all");
		//client.query("limit", "100");
		client.query("offset", "" + offset * 10);
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
