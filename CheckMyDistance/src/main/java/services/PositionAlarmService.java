package services;

import java.util.LinkedList;
import java.util.Queue;

import util.GeoMath;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.checkmydistance.R;

/**
 * This class is an INTEND SERVICE
 * 
 * This IntendService is used as a service to manage the location of 
 * the alarm and create a notification if the position of the phone 
 * comes closer to the position of the alarm than the value of the radius.
 * It is not properly closed (using a static boolean)
 * But it still works fine and the performances are good (verifier!)
 * 
 * This class has been created using several tutorials and forums
 * 
 * @author jvdur_000
 * 
 *         some part of the code from
 *         http://stackoverflow.com/questions/6866415/
 *         intentservice-how-to-enqueue-correctly
 * 
 */
public class PositionAlarmService extends IntentService {

	/*
	 * ACTION_KEYS
	 */
	// Alarm when the distance is close enought
	public static final String ACTION_MyIntentService = "service.RESPONSE";

	// Update the Activity
	public static final String ACTION_MyUpdate = "service.UPDATE";

	/*
	 * EXTRA_KEYS
	 */
	// What the activity sends to the Service (position to get)
	public static final String EXTRA_KEY_IN = "EXTRA_IN";
	public static final String EXTRA_KEY_IN_RAYON = "EXTRA_IN_RAYON";
	public static final String EXTRA_KEY_IN_LAT = "EXTRA_IN_LAT";
	public static final String EXTRA_KEY_IN_LON = "EXTRA_IN_LON";

	// What the service will send the first position
	public static final String EXTRA_KEY_UPDATE_FIRST = "EXTRA_FIRST";
	public static final String EXTRA_KEY_UPDATE_FIRST_LAT = "EXTRA_FIRST_LAT";
	public static final String EXTRA_KEY_UPDATE_FIRST_LON = "EXTRA_FIRST_LON";

	// What the service will send to the Activity when progressing
	public static final String EXTRA_KEY_UPDATE = "EXTRA_UPDATE";
	public static final String EXTRA_KEY_UPDATE_LAST_LAT = "EXTRA_LAST_LAT";
	public static final String EXTRA_KEY_UPDATE_LAST_LON = "EXTRA_LAST_LON";

	// What the service will send to the Activity when finish
	public static final String EXTRA_KEY_OUT = "EXTRA_OUT";

	/*
	 * VAR_MSGS
	 */
	String msgFromActivity;

	/*
	 * Location Manager
	 */
	private Location mLocation = null;
	@SuppressWarnings("unused")
	private Location aLocation = null;

	/*
	 * Data share add | pol
	 */
	private Queue<Location> locQueue;
	public static boolean continueProcess;

	/*
	 * Position to reach
	 */
	private boolean firstPosSended = false;
	private double pos_longitude;
	private double pos_latitude;
	private int pos_rayon;
	private double act_dist_to_pos;

	/*
	 * Notification
	 */
	private NotificationManager notificationManager;
	private Notification myNotification;
	private boolean notifSend = false;
	
	/**
	 * Constructor
	 */
	public PositionAlarmService() {
		super("sercice.PositionAlarmService");
	}

	/**
	 * When the service is create
	 */
	 @Override
	 public void onCreate() {
	  super.onCreate();
	  notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
	 }
	
	/**
	 * Incomming intent Handler and computing manager
	 */
	@Override
	protected void onHandleIntent(Intent intent) {

		// When get input
		msgFromActivity = intent.getStringExtra(EXTRA_KEY_IN);
		pos_rayon = intent.getIntExtra(EXTRA_KEY_IN_RAYON, -1);
		pos_latitude = intent.getDoubleExtra(EXTRA_KEY_IN_LAT, -1);
		pos_longitude = intent.getDoubleExtra(EXTRA_KEY_IN_LON, -1);

		// Initiate the share location queue
		locQueue = new LinkedList<Location>();

		// Initialisating the GPS listener Thread
		Log.d("xyz", "launching location thread");
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationThread thread = new LocationThread(locationManager);
		thread.start();

		continueProcess = true;

		try {

			// Tant que POS_CONNUE et PAS DANS LE RAYON
			while (!firstPosSended || act_dist_to_pos > 0) {

				// Waiting for position update
				while (locQueue.isEmpty() && continueProcess) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						return;
					}
				}

				mLocation = locQueue.poll();

				// Si on mets a jour la distance on rafraichi
				if (getDistanceToDest() && continueProcess) {
					sendUpdate(act_dist_to_pos);

					// si entree dans rayon
					if (!notifSend && act_dist_to_pos < pos_rayon) {
						sendNotificationAndRingTone(act_dist_to_pos);
						notifSend = true;
						sendFinal();
					}
				
				}
				

				if (!continueProcess) {
					thread.interrupt();
					return;
				}

			}

		} catch (Exception e) {
			return;
		}
	}

	/**
	 * Send update of the current location to the 
	 * Activity registred to this service
	 * @param act_dist_to_pos2
	 */
	private void sendUpdate(double act_dist_to_pos2) {
		try {
			if (firstPosSended) {
				Intent intentUpdate = new Intent();
				intentUpdate.setAction(ACTION_MyUpdate);
				intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
				intentUpdate.putExtra(EXTRA_KEY_UPDATE, act_dist_to_pos2);
				intentUpdate.putExtra(EXTRA_KEY_UPDATE_LAST_LAT,
						mLocation.getLatitude());
				intentUpdate.putExtra(EXTRA_KEY_UPDATE_LAST_LON,
						mLocation.getLongitude());
				sendBroadcast(intentUpdate);
			} else {
				Intent intentUpdate = new Intent();
				intentUpdate.setAction(ACTION_MyUpdate);
				intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
				intentUpdate.putExtra(EXTRA_KEY_UPDATE_FIRST, true);
				intentUpdate.putExtra(EXTRA_KEY_UPDATE_FIRST_LAT,
						mLocation.getLatitude());
				intentUpdate.putExtra(EXTRA_KEY_UPDATE_FIRST_LON,
						mLocation.getLongitude());
				intentUpdate.putExtra(EXTRA_KEY_UPDATE, act_dist_to_pos2);
				sendBroadcast(intentUpdate);
				firstPosSended = true;
			}
		} catch (Exception e) {

		}
	}

	
	/**
	 * Send a final mesage to the activity in case the service ends
	 */
	private void sendFinal() {
		try {
			Intent intentResponse = new Intent();
			intentResponse.setAction(ACTION_MyIntentService);
			intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
			intentResponse.putExtra(EXTRA_KEY_OUT, continueProcess);
			sendBroadcast(intentResponse);
		} catch (Exception e) {

		}
	}

	/**
	 * Method called when the distance to the position of the alarm is 
	 * smaller than the alarm radius.
	 * Create a notification
	 * @param act_dist_to_pos2
	 */
	private void sendNotificationAndRingTone(double act_dist_to_pos2) {

		//generate notification
		   String notificationText = "You are less thas " + pos_rayon + " meters to your destination";
		   myNotification = new NotificationCompat.Builder(getApplicationContext())
		   .setContentTitle("Destination is close")
		   .setContentText(notificationText)
		   .setTicker("You reach radius !")
		   .setWhen(System.currentTimeMillis())
		   .setDefaults(RingtoneManager.TYPE_RINGTONE)
		   .setAutoCancel(false)
		   .setSmallIcon(R.drawable.ic_launcher)
		   .build();
		   
		   notificationManager.notify(1, myNotification);

		/*
		 * Play ringtone Uri notification = RingtoneManager
		 * .getDefaultUri(RingtoneManager.TYPE_RINGTONE); Ringtone r =
		 * RingtoneManager.getRingtone(getApplicationContext(), notification);
		 * r.play();
		 */
	}

	/**
	 * Compute the distance from position to distance
	 * 
	 * @return
	 */
	public boolean getDistanceToDest() {

		int sol = (int) (1000 * GeoMath.GetDistance(mLocation.getLatitude(),
				mLocation.getLongitude(), pos_latitude, pos_longitude));

		act_dist_to_pos = sol;

		return true;
	}

	/*
	 * POSITION MANAGER
	 */
	/**
	 * This class is a Thread that will implement a LoacationListener.
	 * The role of this class is to provide th actual location th the 
	 * intentService
	 * 
	 * @author jvdur_000
	 *
	 */
	private class LocationThread extends Thread implements LocationListener {
		protected LocationManager locationManager = null;

		/**
		 * Constructor
		 * @param locationManager a locationManager
		 */
		public LocationThread(LocationManager locationManager) {
			super("UploaderService-Uploader");
			this.locationManager = locationManager;
		}

		/**
		 * When the Thread starts to run
		 */
		@Override
		public void run() {
			Log.d("xyz", "Thread.run");
			Looper.prepare();
			// this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
			// 0, 0, this);
			this.locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, this);

			Looper.loop();
		}

		/**
		 * Called when the location changes
		 */
		@Override
		public void onLocationChanged(Location location) {
			Log.d("xyz", "onLocationChanged(" + location.toString() + ")");

			if (locQueue.size() > 100)
				locQueue.clear();

			locQueue.add(location);
		}

		@Override
		public void onProviderDisabled(String arg0) {
		}

		@Override
		public void onProviderEnabled(String arg0) {
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		}

	}

}
