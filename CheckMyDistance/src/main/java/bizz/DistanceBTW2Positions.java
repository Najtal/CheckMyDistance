package bizz;

import java.util.GregorianCalendar;

import util.GeoMath;

/**
 * 
 * This class 
 * 
 * @author jvdur_000
 *
 */
public class DistanceBTW2Positions {

	
	private Position posTo;
	private Position posFrom;
	private GregorianCalendar dateCreated;
	
	private double distance;


	// CONSTRUCTORS
	@SuppressWarnings("unused")
	private DistanceBTW2Positions() {
	}
	
	/**
	 * Constructor
	 * @param from the start position 
	 * @param to the destination position
	 */
	public DistanceBTW2Positions(Position from, Position to) {
		this.posFrom = from;
		this.posTo = to;
		this.dateCreated = new GregorianCalendar();
	}

	
	/**
	 * Get the distance between the two positions
	 * @return the distance in meters
	 */
	public double getDistanceFromTo() {
		return GeoMath.GetDistance(posFrom.getLatitude(), posFrom.getLongitude(), posTo.getLatitude(), posTo.getLongitude());
	}
	
	
	// Not used (yet)
	/**
	 * Get the direction from one position to the other
	 * @return the orientation in degrees
	 */
	public double getDirectionFromTo() {
		return GeoMath.GetDirection(posFrom.getLatitude(), posFrom.getLongitude(), posTo.getLatitude(), posTo.getLongitude());
	}
	
	
	// GETTERS AND SETTERS

	public Position getPosTo() {
		return posTo;
	}

	public void setPosTo(Position posTo) {
		this.posTo = posTo;
	}

	public Position getPosFrom() {
		return posFrom;
	}

	public void setPosFrom(Position posFrom) {
		this.posFrom = posFrom;
	}

	public double getDistance() {
		if (distance == 0) {
			distance = getDistanceFromTo();
		}
		return distance;
	}

	public GregorianCalendar getDateCreated() {
		return dateCreated;
	}
}
