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
import io.hym.optisls.model.Supplier;

@RestController
public class EventController {

	@RequestMapping("/search/{filter}")
	public ResponseEntity<List<Event>> searchWithFilter(@PathVariable("filter") String filter) {

		List<Event> events = new ArrayList<Event>();

		Event event = new Event();
		event.setPlace("Frankfurt");
		event.setCoordinates(new Coordinates(50.11092209999999, 8.682126700000026));
		event.setSuppliers(new ArrayList<Supplier>());
		Supplier supplier = new Supplier();
		supplier.setPlace("New York");
		supplier.setCoordinates(new Coordinates(40.741895, -73.989308));
		event.getSuppliers().add(supplier);
		
		supplier = new Supplier();
		supplier.setPlace("Shangai");
		supplier.setCoordinates(new Coordinates(51.5073509, 121.47370209999997));
		event.getSuppliers().add(supplier);
		
		supplier = new Supplier();
		supplier.setPlace("Mexico");
		supplier.setCoordinates(new Coordinates(23.634501, -102.55278399999997));
		event.getSuppliers().add(supplier);
		
		
		events.add(event);

		System.out.println(filter);

		return events == null ? new ResponseEntity<List<Event>>(HttpStatus.INTERNAL_SERVER_ERROR)
				: new ResponseEntity<List<Event>>(events, HttpStatus.OK);
	}

}
