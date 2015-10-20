package bizz;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.Map;

/**
 * 
 * Bizz object that contains all the info about an alarm
 * 
 * @author jvdur_000
 *
 */
public class Alarm implements Serializable {

	private static final long serialVersionUID = 1L;

	// set at instanciation
	private int rayon;
	private Position position;
	
	// settable
	private double lastDistanceCalculated;
	public double firstKnownLat;
	public double firstKnownLon;
	private double firstKnownDistance;
	private float azimuthInDegress;
	private double lastKnownPositionLat;
	private double lastKnownPositionLon;

	// Not used (yet)
	private GregorianCalendar gcCreate;
	private Map<GregorianCalendar, Double> mapPrevPos; // Time, Distance (KM)
	
	
	/**
	 * Constructor
	 * @param rayon Radius
	 * @param position Position to get
	 */
	public Alarm(int rayon, Position position) {
		this.rayon = rayon;
		this.position = position;
		
		this.gcCreate = new GregorianCalendar();
	}
	
	
	/*
	 * GETTERS AND SETTERS
	 */
	public double getLastKnownPositionLat() {
		return lastKnownPositionLat;
	}

	public float getAzimuthInDegress() {
		return azimuthInDegress;
	}

	public void setLastKnownPositionLat(double lastKnownPositionLat) {
		this.lastKnownPositionLat = lastKnownPositionLat;
	}

	public double getLastKnownPositionLon() {
		return lastKnownPositionLon;
	}

	public void setLastKnownPositionLon(double lastKnownPositionLon) {
		this.lastKnownPositionLon = lastKnownPositionLon;
	}

	public void addDistanceRecord(double distance) {
		mapPrevPos.put(new GregorianCalendar(), distance);
	}
	
	public Map<GregorianCalendar, Double> getDistancerecord() {
		return mapPrevPos;
	}

	public int getRayon() {
		return rayon;
	}

	public void setRayon(int rayon) {
		this.rayon = rayon;
	}

	public Position getPosition() {
		return position;
	}

	public double getLastDistanceCalculated() {
		return lastDistanceCalculated;
	}

	public void setLastDistanceCalculated(double update) {
		this.lastDistanceCalculated = update;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public GregorianCalendar getGcCreate() {
		return gcCreate;
	}

	public void setFirstKnownPosition(double fnpLat, double fnpLon, double update) {
		firstKnownLat = fnpLat;
		firstKnownLon = fnpLon;
		firstKnownDistance = update;
	}

	public double getFirstKnownDistance() {
		return firstKnownDistance;
	}

	public void setMyOrientation(float azimuthInDegress) {
		this.azimuthInDegress = azimuthInDegress;
	}
	
}
