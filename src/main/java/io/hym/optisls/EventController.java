package io.hym.optisls;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.hym.optisls.model.Coordinates;
import io.hym.optisls.model.Event;

@RestController
public class EventController {

	@RequestMapping("/search/{filter}")
	public ResponseEntity<List<Event>> searchWithFilter(@PathVariable("filter") String filter) {

		List<Event> events = new ArrayList<Event>();

		Event event = new Event();
		event.setPlace("New York");
		event.setCoordinates(new Coordinates(40.741895, -73.989308));
		events.add(event);

		event = new Event();
		event.setPlace("Frankfurt");
		event.setCoordinates(new Coordinates(50.11092209999999, 8.682126700000026));
		events.add(event);

		event = new Event();
		event.setPlace("Munich");
		event.setCoordinates(new Coordinates(48.1351253, 11.581980499999986));
		events.add(event);

		System.out.println(filter);

		return events == null ? new ResponseEntity<List<Event>>(HttpStatus.INTERNAL_SERVER_ERROR)
				: new ResponseEntity<List<Event>>(events, HttpStatus.OK);
	}

}
