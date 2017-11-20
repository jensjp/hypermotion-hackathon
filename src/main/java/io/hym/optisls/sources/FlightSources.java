/**
 * 
 */
package io.hym.optisls.sources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.regex.Pattern;

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

import io.hym.optisls.model.Connections;
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
	
	static final Map<String, Connections> CONN_CACHE = new HashMap<>(); 
	static{
		try {
			loadConnCache();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void persistConnCache() throws Exception{
		BufferedWriter bw = Files.newBufferedWriter(Paths.get("/Users/jens/tmp", "conns.csv"), StandardOpenOption.CREATE);
		for(Map.Entry<String, Connections> e : CONN_CACHE.entrySet()){
			if(e.getValue() == null || e.getValue().getCoordinates() == null)
				continue;
			StringBuilder sbul = new StringBuilder();
			sbul.append(e.getKey()).append(",").append(e.getValue().encode()).append("\n");
			bw.write(sbul.toString());
		}
		bw.flush();
		bw.close();
	}
	
	public static void loadConnCache() throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(EventSources.class.getResourceAsStream("/data/conns.csv")));
		Pattern ptrn = Pattern.compile(",");
		for(String line = br.readLine(); line != null && line.trim().length() > 0 ; line = br.readLine()){
			String[] splts = ptrn.split(line);
			Connections conn = new Connections();
			for(int x = 0 ; x < splts.length; x++){
				String tkn = splts[x];
				switch(x){
					case 0 : CONN_CACHE.put(tkn, conn); break;
					case 1 : conn.setLH(Boolean.parseBoolean(tkn)); break;
					case 2 : conn.setPlace(tkn); break;
					case 3 : conn.setCoordinates(new Coordinates(0, 0));
							 conn.getCoordinates().setLat(Double.parseDouble(tkn));
							 break;
					case 4 : conn.getCoordinates().setLng(Double.parseDouble(tkn)); break;
				}
			}
		}
	}
	
	public static Connections retrieveAllFlight(String origin, String destination, String dateTime,String product) throws Exception{
		String key = origin + "-" + destination;
		if(CONN_CACHE.containsKey(key))
			return CONN_CACHE.get(key);
		Map<String, Object> dataOrg = retrieveFlights(origin, destination, dateTime, product);
		Map<String, Object> data = (Map<String, Object>)dataOrg.get("ScheduleResource");
		if(data == null){
			Connections conn = new Connections();
			CONN_CACHE.put(key, conn);
			System.err.println(dataOrg);
			return conn;
		}
		data = (Map<String, Object>)data.get("Schedule");
		Object flightsOrFlight = data.get("Flight");
		Connections conn = new Connections();
		if(flightsOrFlight instanceof List){
			List<Map<String, Object>> flights = (List<Map<String, Object>>)flightsOrFlight;
			for(Map<String, Object> flight : flights){
				Map<String, Object> arrival = (Map<String, Object>)flight.get("Arrival");
				conn.setPlace((String)arrival.get("AirportCode"));
				conn.setCoordinates(EventSources.getAirportCoordinates(conn.getPlace()));
				String carrier = (String)((Map<String, Object>)flight.get("MarketingCarrier")).get("AirlineID");
				conn.setLH("LH".equals(carrier));
				break;
			}
		}else{
			Map<String, Object> flight = (Map<String, Object>)flightsOrFlight;
			Map<String, Object> arrival = (Map<String, Object>)flight.get("Arrival");
			conn.setPlace((String)arrival.get("AirportCode"));
			conn.setCoordinates(EventSources.getAirportCoordinates(conn.getPlace()));
			String carrier = (String)((Map<String, Object>)flight.get("MarketingCarrier")).get("AirlineID");
			conn.setLH("LH".equals(carrier));
		}
		conn = conn.isLH() ? null : conn;
		CONN_CACHE.put(key, conn);
		return conn;
	}
	
	public static Map<String, Object> retrieveFlights(String origin, String destination, String dateTime,String product) throws Exception{
		WebClient client = WebClient.create("https://api.lufthansa.com/v1/operations/schedules/"
				+ origin +"/"
				+ destination
				+ "/2017-11-20?directFlights=0&limit=1");
		client.accept(MediaType.APPLICATION_JSON);
		client.header("Authorization", "Bearer 2jum7x4zmba5undfz8d9j6eh");
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
