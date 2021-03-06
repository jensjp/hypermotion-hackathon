/**
 * 
 */
package io.hym.optisls.model;

/**
 * @author jens
 *
 */
public class Supplier {

	private String place;
	private String airline;
	private Coordinates coordinates;
	private double weight;
	private double volume;
	private Connections connection;
	
	/**
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * @param place
	 *            the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}

	/**
	 * @return the airline
	 */
	public String getAirline() {
		return airline;
	}

	/**
	 * @param airline
	 *            the airline to set
	 */
	public void setAirline(String airline) {
		this.airline = airline;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * @return the volume
	 */
	public double getVolume() {
		return volume;
	}

	/**
	 * @param volume
	 *            the volume to set
	 */
	public void setVolume(double volume) {
		this.volume = volume;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * @return the connection
	 */
	public Connections getConnection() {
		return connection;
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(Connections connection) {
		this.connection = connection;
	}

	
}
