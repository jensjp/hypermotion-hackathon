package io.hym.optisls.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Coordinates {

	private double lat;
	private double lng;

	public Coordinates(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	@JsonIgnore
	public String encode(){
		StringBuilder sbul = new StringBuilder();
		sbul.append(lat).append(",").append(lng);
		return sbul.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Coordinates [lat=");
		builder.append(lat);
		builder.append(", lng=");
		builder.append(lng);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
