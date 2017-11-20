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
public class EventSources {

	static final String PHQ_DFS = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	static final DateFormat PHQ_DF = new SimpleDateFormat(PHQ_DFS);
	
	public static void mapAirports() throws Exception {
		BufferedWriter bw = Files.newBufferedWriter(Paths.get("/Users/jens/tmp", "predictHq_mapped.csv"), StandardOpenOption.CREATE);
		BufferedReader br = Files.newBufferedReader(Paths.get("/Users/jens/tmp", "predictHq_x.csv"));
		List<Event> events = new ArrayList<>();
		WebClient client = WebClient.create("https://api.lufthansa.com/v1/references/airports/nearest/");
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Authorization", "Bearer hnrh4aqx4vh4ze99s9ub7f2x");
		for(String line = br.readLine(); line != null; line = br.readLine()){
			System.out.println("Processing Line : " + line);
			Event e = new Event();
			events.add(e);
			e.decode(line);
			client.replacePath(e.getCoordinates().getLat() + "," + e.getCoordinates().getLng());
			Map<String, Object> data = mapToJson((InputStream)client.get().getEntity());
			data = (Map<String, Object>)data.get("NearestAirportResource");
			data = (Map<String, Object>)data.get("Airports");
			List<Map<String, Object>> airports = (List<Map<String, Object>>)data.get("Airport"); 
			if(airports != null){
				for(Map<String, Object> arp : airports){
					e.setPlace((String)arp.get("AirportCode"));
					System.out.println("Got :" + e.getPlace());
					break;
				}				
						
			}else{
				System.err.println("Unable to find airport");
			}
			Thread.sleep(500);
		}
		for(Event e : events){
			bw.write(e.encode());
			bw.write("\n");
		}
		bw.flush();
		bw.close();
	}
	
	/**
	 * 
	 * @param string
	 * @throws Exception 
	 */
	public static Coordinates getAirportCoordinates(String string) throws Exception {
		
		WebClient client = WebClient.create("https://api.lufthansa.com/v1/references/airports/"+string+"?limit=1&offset=0&LHoperated=1");
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Authorization", "Bearer hnrh4aqx4vh4ze99s9ub7f2x");
		mapToJson((InputStream)client.get().getEntity());
		
		Map<String, Object> data = mapToJson((InputStream)client.get().getEntity());
		data = (Map<String, Object>)data.get("AirportResource");
		data = (Map<String, Object>)data.get("Airports");
		Map<String, Object> airports = (Map<String, Object>)data.get("Airport"); 
		if(airports != null){
		 	Map<String, Object> m1=(Map<String, Object>)airports.get("Position");
		 	Map<String, Object> m=(Map<String, Object>)m1.get("Coordinate");
		 	
		 	Double d1=(Double) m.get("Latitude");
		 	Double d2=(Double) m.get("Longitude");
			Coordinates c=new Coordinates(d1.doubleValue(),d2.doubleValue());
			return c;
		}
		
		return null;
	}

	public static void retrieveAllPHQEvents() throws Exception{
		List<Event> events = new ArrayList<>();
		for(int x = 0 ; x < 1000; x++){
			Map<String, Object> data = retrievePHQEvents(x);
			List<Map<String, Object>> results = (List<Map<String, Object>>) data.get("results");
			for(Map<String, Object> r : results){
				Event e = new Event();
				e.setDesc((String)r.get("title"));
				e.setKlass((String)r.get("category"));
				e.setSubKlass((String)r.get("category"));
				String dateStr = (String)r.get("start");
				Date date = PHQ_DF.parse(dateStr);
				Calendar cal = new GregorianCalendar();
				cal.setTime(date);
				e.setDate(cal);
				List<Object> coord = (List<Object>)r.get("location");
				e.setCoordinates(new Coordinates(Double.parseDouble(coord.get(0).toString()), Double.parseDouble(coord.get(1).toString())));
				events.add(e);
			}
			if(data == null || data.isEmpty() || data.get("next") == null)
				break;
		}
		System.out.println(events.size());
		BufferedWriter br = Files.newBufferedWriter(Paths.get("/Users/jens/tmp", "predictHq_x.csv"), StandardOpenOption.CREATE);
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
		//client.query("id", "phq.C5G4pU57NRFzEZgYZ7ZkxnpBDj3lzBgsp4uT7DAI");
		client.query("category", "expos,concerts,festivals,sports,airport-delays,severe-weather,disasters,terror");
		//client.query("labels", "all");
		//client.query("labels.op", "any");
		client.query("rank_level", "4,5");
		client.query("start.gte", "2017-09-01");
		client.query("start.lt", "2018-02-01");
		//client.query("date", "next30days");
		client.query("places", "all");
		client.query("sort", "start");
		client.query("offset", "" + (offset * 10));
		
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
