package io.hym.optisls;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.hym.optisls.model.Event;

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
			if(e.getDate() == null)
				System.err.println(line);
			else
				events.add(e);
		}
		logger.info("loaded event DB size : {}", events.size());
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
