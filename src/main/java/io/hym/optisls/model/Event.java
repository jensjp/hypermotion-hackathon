/**
 * 
 */
package io.hym.optisls.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author jens
 *
 */
public class Event {

	private String place;
	private String type = "Port";
	private Calendar date;
	private String desc;
	private String klass;
	private String subKlass;
	private Coordinates coordinates;
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
	 * @param place
	 *            the place to set
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
	 * @param type
	 *            the type to set
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
	 * @param desc
	 *            the desc to set
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
	 * @param klass
	 *            the klass to set
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
	 * @param subKlass
	 *            the subKlass to set
	 */
	public void setSubKlass(String subKlass) {
		this.subKlass = subKlass;
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

	/**
	 * @return the suppliers
	 */
	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	/**
	 * @param suppliers
	 *            the suppliers to set
	 */
	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}
	
	/**
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	static final DateFormat DF = new SimpleDateFormat("yyyyMMdd");
	
	@JsonIgnore
	public String encode(){
		StringBuilder sbul = new StringBuilder();
		sbul.append(place).append(",")
			.append(type).append(",")
			.append(desc).append(",")
			.append(klass).append(",")
			.append(subKlass).append(",")
			.append(coordinates.encode()).append(",")
			.append(weight).append(",")
			.append(volume).append(",")
			.append(DF.format(this.date.getTime()))
			;
		return sbul.toString();
	}

	static final Pattern ptrn = Pattern.compile(",");
	public void decode(String line){
		String[] splts = ptrn.split(line);
		for(int x = 0 ; x < splts.length; x++){
			String tokn = splts[x];
			switch(x){
				case 0 : this.place = tokn; break;
				case 1 : this.type = tokn; break;
				case 2 : this.desc = tokn; break;
				case 3 : this.klass = tokn; break;
				case 4 : this.subKlass = tokn; break;
				case 5 : this.coordinates = new Coordinates(Double.parseDouble(tokn), 0 ); break;
				case 6 : this.coordinates.setLng(Double.parseDouble(tokn)); break;
				case 7 : this.weight = Double.parseDouble(tokn); break;
				case 8 : this.volume = Double.parseDouble(tokn); break;
				case 9 : this.date = new GregorianCalendar(); 
				try {
					this.date.setTime(DF.parse(tokn));
				} catch (ParseException e) {
					throw new RuntimeException(e);
				} 
					break; 
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Event [place=");
		builder.append(place);
		builder.append(", type=");
		builder.append(type);
		builder.append(", date=");
		builder.append(date);
		builder.append(", desc=");
		builder.append(desc);
		builder.append(", klass=");
		builder.append(klass);
		builder.append(", subKlass=");
		builder.append(subKlass);
		builder.append(", coordinates=");
		builder.append(coordinates);
		builder.append(", weight=");
		builder.append(weight);
		builder.append(", volume=");
		builder.append(volume);
		builder.append(", suppliers=");
		builder.append(suppliers);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
