/**
 * 
 */
package io.hym.optisls.model;

import java.util.List;

/**
 * @author jens
 *
 */
public class Event {

	private String place;
	private String type;
	private String desc;
	private String klass;
	private String subKlass;
	private double lat;
	private double lon;
	private double weight;
	private double volume;
	private List<Supplier> suppliers;
	
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
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @return the klass
	 */
	public String getKlass() {
		return klass;
	}
	/**
	 * @param klass the klass to set
	 */
	public void setKlass(String klass) {
		this.klass = klass;
	}
	/**
	 * @return the subKlass
	 */
	public String getSubKlass() {
		return subKlass;
	}
	/**
	 * @param subKlass the subKlass to set
	 */
	public void setSubKlass(String subKlass) {
		this.subKlass = subKlass;
	}
	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}
	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}
	/**
	 * @return the lon
	 */
	public double getLon() {
		return lon;
	}
	/**
	 * @param lon the lon to set
	 */
	public void setLon(double lon) {
		this.lon = lon;
	}
	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}
	/**
	 * @param weight the weight to set
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
	 * @param volume the volume to set
	 */
	public void setVolume(double volume) {
		this.volume = volume;
	}
	/**
	 * @return the suppliers
	 */
	public List<Supplier> getSuppliers() {
		return suppliers;
	}
	/**
	 * @param suppliers the suppliers to set
	 */
	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}
	
	
	
}
