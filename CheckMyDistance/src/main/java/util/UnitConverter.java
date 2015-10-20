package util;

/**
 * This class was initially created to manage the computing two unit systems
 * Still to come ;)
 * 
 * @author jvdur_000
 *
 */
public class UnitConverter {
	
	private final static double RAPPORT_METRE_MILE = 0.621371;

	/**
	 * Convert meters in miles
	 * @param meters distance of meters
	 * @return return the distance in miles
	 */
	public static double getMileFromMeter(double meters) {
		if (meters < 0) {
			return 0;
		}
		
		return meters*RAPPORT_METRE_MILE;
		
	}
	
}
