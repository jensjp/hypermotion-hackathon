package io.hym.optisls;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.hym.optisls.model.Connections;
import io.hym.optisls.model.Event;
import io.hym.optisls.model.Supplier;
import io.hym.optisls.sources.EventSources;
import io.hym.optisls.sources.FlightSources;

@RestController
public class EventController implements InitializingBean{

	static final Logger logger = LoggerFactory.getLogger(EventController.class);

	private List<Event> events;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		this.events = new ArrayList<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/data/events_v2.csv")));
		for(String line = br.readLine(); line != null; line = br.readLine()){
			Event e = new Event();
			e.decode(line);
			
			switch(e.getKlass()){
				case "airport-delays" :
				case "severe-weather" :	
					break;
				case "disasters" :
					Supplier s = new Supplier();
					s.setPlace("FRA");
					s.setAirline("LH");
					s.setCoordinates(EventSources.getAirportCoordinates("FRA"));
					s.setVolume(25.0);
					s.setWeight(2500.0);
					e.getSuppliers().add(s);
					break;
				case "expos" :
					e.getSuppliers().addAll(populateExpos(e.getPlace()));
					break;
				case "festivals" :
				case "performing-arts" :
				case "concerts" :	
					e.getSuppliers().addAll(populateFestivals(e.getPlace()));
					break;
				case "sports" :
					e.getSuppliers().addAll(populateSports(e.getPlace()));
					break;
			}
			if(e.getDate() == null)
				System.err.println(line);
			else{
				populateConnections(e);
				events.add(e);
			}
		}
		logger.info("loaded event DB size : {}", events.size());
	}
	
	private void populateConnections(Event e) throws Exception{
		if(e.getSuppliers() != null){
			List<Supplier> deletes = new ArrayList<>();
			for(Supplier s : e.getSuppliers()){
				Connections conn = FlightSources.retrieveAllFlight(s.getPlace(), e.getPlace(), null, null);
				if(conn != null && conn.getPlace() == null){
					deletes.add(s);
				}else
					s.setConnection(conn);
			}
			e.getSuppliers().removeAll(deletes);
		}
	}
	
	private static List<Supplier> populateExpos(String airport) throws Exception{
		Random r = new Random();
		int numSupp = r.nextInt(4);
		List<Supplier> ans = new ArrayList<>(numSupp);
		Supplier s = null;
		for(int x = 0 ; x < numSupp; x++){
			switch(x){
			case 0 : 
				s = new Supplier();
				s.setPlace("FRA");
				s.setAirline("LH");
				s.setCoordinates(EventSources.getAirportCoordinates("FRA"));
				s.setVolume(25.0);
				s.setWeight(2500.0);
				if(!airport.equals("FRA"))
					ans.add(s);
				break;
			case 1 : 
				s = new Supplier();
				s.setPlace("NRT");
				s.setAirline("LH");
				s.setCoordinates(EventSources.getAirportCoordinates("NRT"));
				s.setVolume(25.0);
				s.setWeight(2500.0);
				if(!airport.equals("NRT"))
					ans.add(s);	
				break;
			case 2 : 
				s = new Supplier();
				s.setPlace("HKG");
				s.setAirline("LH");
				s.setCoordinates(EventSources.getAirportCoordinates("HKG"));
				s.setVolume(25.0);
				s.setWeight(2500.0);
				if(!airport.equals("HKG"))
					ans.add(s);	
				break;
			case 3 : 
				s = new Supplier();
				s.setPlace("LAX");
				s.setAirline("LH");
				s.setCoordinates(EventSources.getAirportCoordinates("LAX"));
				s.setVolume(25.0);
				s.setWeight(2500.0);
				if(!airport.equals("LAX"))
					ans.add(s);	
				break;	
			}
		}
		return ans;
	}
	
	private static List<Supplier> populateSports(String airport) throws Exception{
		Random r = new Random();
		int numSupp = r.nextInt(4);
		List<Supplier> ans = new ArrayList<>(numSupp);
		Supplier s = null;
		for(int x = 0 ; x < numSupp; x++){
			switch(x){
			case 0 : 
				s = new Supplier();
				s.setPlace("FRA");
				s.setAirline("LH");
				s.setCoordinates(EventSources.getAirportCoordinates("FRA"));
				s.setVolume(25.0);
				s.setWeight(2500.0);
				if(!airport.equals("FRA"))
					ans.add(s);
				break;
			case 1 : 
				s = new Supplier();
				s.setPlace("LHR");
				s.setAirline("LH");
				s.setCoordinates(EventSources.getAirportCoordinates("LHR"));
				s.setVolume(25.0);
				s.setWeight(2500.0);
				if(!airport.equals("LHR"))
					ans.add(s);	
				break;
			case 2 : 
				s = new Supplier();
				s.setPlace("GRU");
				s.setAirline("LH");
				s.setCoordinates(EventSources.getAirportCoordinates("GRU"));
				s.setVolume(25.0);
				s.setWeight(2500.0);
				if(!airport.equals("GRU"))
					ans.add(s);	
				break;
			case 3 : 
				s = new Supplier();
				s.setPlace("LAX");
				s.setAirline("LH");
				s.setCoordinates(EventSources.getAirportCoordinates("LAX"));
				s.setVolume(25.0);
				s.setWeight(2500.0);
				if(!airport.equals("LAX"))
					ans.add(s);	
				break;	
			}
		}
		return ans;
	}
	
	private static List<Supplier> populateFestivals(String airport) throws Exception{
		Random r = new Random();
		int numSupp = r.nextInt(4);
		List<Supplier> ans = new ArrayList<>(numSupp);
		Supplier s = null;
		for(int x = 0 ; x < numSupp; x++){
			switch(x){
			case 0 : 
				s = new Supplier();
				s.setPlace("FRA");
				s.setAirline("LH");
				s.setCoordinates(EventSources.getAirportCoordinates("FRA"));
				s.setVolume(25.0);
				s.setWeight(2500.0);
				if(!airport.equals("FRA"))
					ans.add(s);
				break;
			case 1 : 
				s = new Supplier();
				s.setPlace("CDG");
				s.setAirline("LH");
				s.setCoordinates(EventSources.getAirportCoordinates("CDG"));
				s.setVolume(25.0);
				s.setWeight(2500.0);
				if(!airport.equals("CDG"))
					ans.add(s);	
				break;
			case 2 : 
				s = new Supplier();
				s.setPlace("LHR");
				s.setAirline("LH");
				s.setCoordinates(EventSources.getAirportCoordinates("LHR"));
				s.setVolume(25.0);
				s.setWeight(2500.0);
				if(!airport.equals("LHR"))
					ans.add(s);	
				break;
			case 3 : 
				s = new Supplier();
				s.setPlace("JFK");
				s.setAirline("LH");
				s.setCoordinates(EventSources.getAirportCoordinates("JFK"));
				s.setVolume(25.0);
				s.setWeight(2500.0);
				if(!airport.equals("JFK"))
					ans.add(s);	
				break;	
			}
		}
		return ans;
	}

	@RequestMapping("/search/{filter}")
	public ResponseEntity<List<Event>> searchWithFilter(@PathVariable("filter") String filter) {
		final int month = Integer.parseInt(filter);
		List<Event> answer = events.stream().filter(e -> e.getDate().get(Calendar.MONTH) == month).collect(Collectors.toList());
		logger.info("Filer {}, Result {}", month, answer);
		return answer == null ? new ResponseEntity<List<Event>>(HttpStatus.INTERNAL_SERVER_ERROR)
				: new ResponseEntity<List<Event>>(answer, HttpStatus.OK);
	}

	/**
	 * @return the events
	 */
	public List<Event> getEvents() {
		return events;
	}

	/**
	 * @param events the events to set
	 */
	public void setEvents(List<Event> events) {
		this.events = events;
	}

	
}
