/**
 * 
 */
package io.hym.optisls_test;

import java.util.List;

import io.hym.optisls.EventController;
import io.hym.optisls.model.Event;
import io.hym.optisls.sources.EventSources;
import io.hym.optisls.sources.FlightSources;

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
		//EventSources.persistCoordCache();
		//FlightSources.persistConnCache();
		System.out.println("Done");
	}

}
