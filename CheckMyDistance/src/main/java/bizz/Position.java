package bizz;

import java.io.Serializable;

import exceptions.InvalidArgumentException;

/**
 * 
 * This class contains all the data from a position
 * 
 * @author jvdur_000
 *
 */
public class Position implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private double latitude;
	private double longitude;
	private boolean favorite;
	private String name;


	/**
	 * Construtor
	 * @param latitude the latitude of the position (signed)
	 * @param longitude the longitude of the position (signed)
	 * @param name  the name of the position
	 * @param favorite If the position is considered as a favorite
	 * @throws InvalidArgumentException Throws this exception if the latitude or the longitude has uncorrect values
	 */
	public Position(double latitude, double longitude, String name, boolean favorite) throws InvalidArgumentException {
		setLatitude(latitude);
		setLongitude(longitude);
		this.name = name;
		this.favorite = favorite;
	}


	/*
	 * GETTERS & SETTERS
	 */
	public void setLatitude(double latitude) throws InvalidArgumentException {
		// latitude [-90;90]
		if (latitude < -90 || latitude > 90) {
			throw new InvalidArgumentException("Latitude must be between -90 and 90 degrees");
		}
		
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) throws InvalidArgumentException {
		// latitude [-180;180]
		if (longitude < -180 || longitude > 180) {
			throw new InvalidArgumentException("longitude must be between -180 and 180 degrees");
		}
		
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public String getName() {
		return name;
	}
}
