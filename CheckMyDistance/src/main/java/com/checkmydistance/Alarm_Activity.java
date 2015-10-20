package com.checkmydistance;

import java.util.Locale;

import model.GUI_Model;
import services.PositionAlarmService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

/**
 * 
 * This class is the Activity that manage the ALARM screen
 * It it composed of two fragments
 * 		- AlarmMain_Fragment
 * 		- AlarmGraph_Frahment
 * 
 * Sources used :
 * http://fr.openclassrooms.com/informatique/cours/creez-des-applications-pour-android/les-services
 * http://stackoverflow.com/
 * http://fr.openclassrooms.com/informatique/cours/creez-des-applications-pour-android/les-capteurs-de-position
 * http://stackoverflow.com/questions/15622497/android-magnetometer-returning-random-values
 * 
 * @author jvdur_000
 *
 */
public class Alarm_Activity extends FragmentActivity {

	// Page Adaptor
	SectionsPagerAdapter mSectionsPagerAdapter;
	// ViewPager
	ViewPager mViewPager;

	/*
	 * FRAGMENTS
	 */
	private Fragment fAlarmGraph;
	private Fragment fAlarmMain;

	/*
	 * My service
	 */
	public Intent intentMyIntentService;

	/*
	 * BroadCast Receiver
	 */
	private MyBroadcastReceiver myBroadcastReceiver;
	private MyBroadcastReceiver_Update myBroadcastReceiver_Update;

	/*
	 * Model
	 */
	private GUI_Model model;

	/*
	 * MAGNETOMETER SENSOR
	 */
	private Sensor magneticSens;
	private Sensor accelSens;
	private SensorManager sensorManager;
	private boolean deviceIsMagnetic;
	private float[] mGeomagnetic;
	private float[] mGravity;
	private float azimuthInDegress; // last orientation

	/**
	 * Method called when created
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);

		this.model = GUI_Model.getInstance();

		/*
		 * PAGE ADAPTOR & FRAGMENTS
		 */
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		/*
		 * MAGNETOMETRE
		 */
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		magneticSens = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		accelSens = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		if (magneticSens != null && accelSens != null) {
			// Il y a au moins un accelerometre
			deviceIsMagnetic = true;
		} else {
			// Il n'y en a pas
			deviceIsMagnetic = false;
		}

		/*
		 * SERVICE
		 */
		String msgToIntentService = "Hellow darling !";

		// Start PositionAlarmService
		intentMyIntentService = new Intent(this, PositionAlarmService.class);
		intentMyIntentService.putExtra(PositionAlarmService.EXTRA_KEY_IN,
				msgToIntentService);
		intentMyIntentService.putExtra(PositionAlarmService.EXTRA_KEY_IN_RAYON,
				model.getAlarm().getRayon());
		intentMyIntentService.putExtra(PositionAlarmService.EXTRA_KEY_IN_LAT,
				model.getAlarm().getPosition().getLatitude());
		intentMyIntentService.putExtra(PositionAlarmService.EXTRA_KEY_IN_LON,
				model.getAlarm().getPosition().getLongitude());
		intentMyIntentService.addFlags(1);

		startService(intentMyIntentService);

		myBroadcastReceiver = new MyBroadcastReceiver();
		myBroadcastReceiver_Update = new MyBroadcastReceiver_Update();

		// register BroadcastReceiver
		Log.i("xyz", "Register BroadCast receiver");
		IntentFilter intentFilter = new IntentFilter(
				PositionAlarmService.ACTION_MyIntentService);
		intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(myBroadcastReceiver, intentFilter);

		IntentFilter intentFilter_update = new IntentFilter(
				PositionAlarmService.ACTION_MyUpdate);
		intentFilter_update.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(myBroadcastReceiver_Update, intentFilter_update);

	}

	/*
	 * Found on
	 * http://stackoverflow.com/questions/15622497/android-magnetometer-
	 * returning-random-values
	 */
	final SensorEventListener mSensorEventListener = new SensorEventListener() {
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// Que faire en cas de changement de precision ?
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				mGravity = event.values.clone();
			}

			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				mGeomagnetic = event.values.clone();
				Log.i("mag", mGeomagnetic.toString() + "");
			}

			if (mGravity != null && mGeomagnetic != null) {
				float R[] = new float[9];
				float I[] = new float[9];
				boolean success = SensorManager.getRotationMatrix(R, I,
						mGravity, mGeomagnetic);
				if (success) {
					float orientation[] = new float[3];
					SensorManager.getOrientation(R, orientation);

					azimuthInDegress = ((float) Math.toDegrees(orientation[0]) + 360) % 360;
					if (model.getAlarm() != null) {
						model.getAlarm().setMyOrientation(azimuthInDegress);
						if (fAlarmMain != null)
							fAlarmMain.onResume();
					}
				}
			}
		}
	};

	/*
	 * LIFECICLE METHODS
	 */
	/**
	 * Called when the activity is destroyed
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(myBroadcastReceiver);
			unregisterReceiver(myBroadcastReceiver_Update);
		} catch (Exception e) {
		}
	}

	/**
	 * Called when the activity is overlayered
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if (deviceIsMagnetic) {
			sensorManager
					.unregisterListener(mSensorEventListener, magneticSens);
			sensorManager.unregisterListener(mSensorEventListener, accelSens);
		}
	}

	/**
	 * Called when the activity appears (again)
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (deviceIsMagnetic) {
			sensorManager.registerListener(mSensorEventListener, magneticSens,
					SensorManager.SENSOR_DELAY_NORMAL);
			sensorManager.registerListener(mSensorEventListener, accelSens,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	/**
	 * When receive alarm from Service !
	 */
	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			try {
				boolean result = intent.getBooleanExtra(
						PositionAlarmService.EXTRA_KEY_OUT, false);
				if (!result)
					finish();
			} catch (Exception e) {
			}

		}
	}

	/**
	 * When receive update from service
	 */
	public class MyBroadcastReceiver_Update extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				if (intent.getBooleanExtra(
						PositionAlarmService.EXTRA_KEY_UPDATE_FIRST, false)) {
					double fnpLat = intent.getDoubleExtra(
							PositionAlarmService.EXTRA_KEY_UPDATE_FIRST_LAT, 0);
					double fnpLon = intent.getDoubleExtra(
							PositionAlarmService.EXTRA_KEY_UPDATE_FIRST_LON, 0);
					double update = intent.getDoubleExtra(
							PositionAlarmService.EXTRA_KEY_UPDATE, 0);
					model.getAlarm().setFirstKnownPosition(fnpLat, fnpLon,
							update);
				}

				double update = intent.getDoubleExtra(
						PositionAlarmService.EXTRA_KEY_UPDATE, 0);
				double fnpLat = intent.getDoubleExtra(
						PositionAlarmService.EXTRA_KEY_UPDATE_LAST_LAT, 0);
				double fnpLon = intent.getDoubleExtra(
						PositionAlarmService.EXTRA_KEY_UPDATE_LAST_LON, 0);
				model.getAlarm().setLastKnownPositionLat(fnpLat);
				model.getAlarm().setLastKnownPositionLon(fnpLon);
				model.getAlarm().setLastDistanceCalculated(update);
				fAlarmMain.onResume();
				fAlarmGraph.onResume();

			} catch (Exception e) {
			}
		}
	}

	/**
	 * Method that manage the option menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option_menu, menu);
		return true;
	}

	/**
	 * Generated by eclipse, modified by myself
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private static final int NBR_TABS = 2;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		/**
		 * When a new item is selected
		 */
		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			Fragment fragment = null;
			if (position == 0) {
				fragment = new AlarmMain_Fragment();
				fAlarmMain = fragment;
			} else if (position == 1) {
				fragment = new AlarmGraph_Fragment();
				fAlarmGraph = fragment;
			}

			Bundle bld = new Bundle();
			fragment.setArguments(bld);

			return fragment;
		}

		/**
		 * Return the amount of tabs
		 */
		@Override
		public int getCount() {
			return NBR_TABS;
		}

		/**
		 * Return the tab title
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.alarm_main_title).toUpperCase(l);
			case 1:
				return getString(R.string.alarm_graph_title).toUpperCase(l);
			}
			return null;
		}
	}

}
