/**
 * 
 */
package io.hym.optisls_test;

import java.util.List;

import io.hym.optisls.EventController;
import io.hym.optisls.model.Event;

/**
 * @author jens
 *
 */
public class TestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		EventController ctr = new EventController();
		ctr.afterPropertiesSet();
		List<Event> e = ctr.getEvents();
		for(Event ev : e){
			if(ev.getDate() == null){
				System.out.println(ev.encode());
			}
		}
	}

}
