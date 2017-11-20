/**
 * 
 */
package io.hym.optisls.model;

/**
 * @author jens
 *
 */
public class Connections {

	private boolean isLH = true;
	private Coordinates coordinates;
	private String place;
	
	public String encode(){
		StringBuilder sbul = new StringBuilder();
		sbul.append(isLH).append(",").append(place).append(",")
			.append(coordinates.encode());
		return sbul.toString();
	}
	
	/**
	 * @return the isLH
	 */
	public boolean isLH() {
		return isLH;
	}
	/**
	 * @param isLH the isLH to set
	 */
	public void setLH(boolean isLH) {
		this.isLH = isLH;
	}
	/**
	 * @return the coordinates
	 */
	public Coordinates getCoordinates() {
		return coordinates;
	}
	/**
	 * @param coordinates the coordinates to set
	 */
	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}
	/**
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}
	/**
	 * @param place the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}
	
	
	
}
