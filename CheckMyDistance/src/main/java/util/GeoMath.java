package util;

import java.math.BigDecimal;

/**
 * This class provides a bunch of methods to compute distances and directions
 * 
 * Code from
 * http://stackoverflow.com/questions/365826/calculate-distance-between
 * -2-gps-coordinates the 15th of November
 * 
 * @author jvdur_000
 * 
 */
public class GeoMath {


	/**
	 * Get the distance between two positions
	 * @param lat1 position 1 latitude
	 * @param lon1 position 1 longitude
	 * @param lat2 position 2 latitude
	 * @param lon2 position 2 longitude
	 * @return the distance in KM between tho points
	 */
	public static double GetDistance(double lat1, double lon1, double lat2,
			double lon2) {
		
		lat1 = Math.toRadians(lat1);
		lon1 = Math.toRadians(lon1);
		lat2 = Math.toRadians(lat2);
		lon2 = Math.toRadians(lon2);
		
		double enKm =  Wgs84.distanceVolOiseauEntre2PointsSansPrecision(lat1, lon1, lat2, lon2) * 6366;
		return round(enKm, 3, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Get the orientation from pos 1 to pos 2
	 * @param lat1 position 1 latitude
	 * @param lon1 position 1 longitude
	 * @param lat2 position 2 latitude
	 * @param lon2 position 2 longitude
	 * @return the orientation (in degrees) on pos1 to reach pos2
	 */
	public static double GetDirection(double lat1, double lon1, double lat2,
			double lon2) {
		// code for Direction in Degrees
		//double dlat = deg2rad(lat1) - deg2rad(lat2);
		double dlon = deg2rad(lon1) - deg2rad(lon2);
		double y = Math.sin(dlon) * Math.cos(lat2);
		double x = Math.cos(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				- Math.sin(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(dlon);
		double direct = Math.round(rad2deg(Math.atan2(y, x)));
		if (direct < 0)
			direct = direct + 360;
		return (direct);
	}


	
	/*
	 * PRIVATE INNER METHODES
	 */
	private static double round(double unrounded, int precision, int roundingMode) {
		BigDecimal bd = new BigDecimal(unrounded);
		BigDecimal rounded = bd.setScale(precision, roundingMode);
		return rounded.doubleValue();
	}
	
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad / Math.PI * 180.0);
	}

}
