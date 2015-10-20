package util;


/**
 * 
 * This class provides some computations (as GeoMath)
 * 
 * http://dotclear.placeoweb.com/post/Formule-de-
 * calcul-entre-2-points-wgs84-
 * pour-calculer-la-distance-qui-separe-ces-deux-points
 * 
 * @author jvdur_000
 *
 */
public class Wgs84 {

	/**
	 * Distance entre 2 points GPS
	 * http://dotclear.placeoweb.com/post/Formule-de-
	 * calcul-entre-2-points-wgs84-
	 * pour-calculer-la-distance-qui-separe-ces-deux-points
	 * 
	 * La distance mesuree le long d'un arc de grand cercle entre deux points
	 * dont on connait les coordonnees {lat1,lon1} et {lat2,lon2} est donnee par
	 * : d=acos(sin(lat1)*sin(lat2)+cos(lat1)*cos(lat2)*cos(lon1-lon2)) Le tout
	 * * 6366 pour l'avoir en km
	 * 
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return
	 */
	public static double distanceVolOiseauEntre2PointsSansPrecision(
			double lat1, double lon1, double lat2, double lon2) {

		// d=acos(sin(lat1)*sin(lat2)+cos(lat1)*cos(lat2)*cos(lon1-lon2))

		return Math.acos(
				Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2)
				* Math.cos(lon1 - lon2));

	}


}