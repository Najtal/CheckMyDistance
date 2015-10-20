package com.checkmydistance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import model.GUI_Model;

/**
 * This class is the Activity that will be first called when the application starts
 * It consists essentially in a menu
 *
 * @author jvdur_000
 *
 */
public class Home_Activity extends Activity implements OnClickListener, LocationListener {

	// MVC
	private GUI_Model model;

	// Buttons
	private Button btAddPosition;
	private Button btListPosition;
	private Button btNewAlarm;
	private Button btPositionBtw2pts;

	// Gps switch & values
	private TextView tvLatitude;
	private TextView tvLongitude;

	// private MainLocationManager mainLocationManager;
	private LocationManager locationManager;

	/**
	 * Method called when created
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// retrieve data
		this.model = GUI_Model.getInstance();
		this.model.setCUDPosValue(-1);

		// initialise les boutons
		btAddPosition = (Button) findViewById(R.id.bt_add_position);
		btListPosition = (Button) findViewById(R.id.bt_list_positions);
		btNewAlarm = (Button) findViewById(R.id.bt_set_new_alarm);
		btPositionBtw2pts = (Button) findViewById(R.id.bt_get_distanceBtw2pos);

		tvLatitude = (TextView) findViewById(R.id.lat_value);
		tvLongitude = (TextView) findViewById(R.id.long_value);

		// on set les listeners
		btAddPosition.setOnClickListener(this);
		btListPosition.setOnClickListener(this);
		btNewAlarm.setOnClickListener(this);
		btPositionBtw2pts.setOnClickListener(this);


		// Location stuffs
		locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

		tvLatitude.setText("...");
		tvLongitude.setText("...");
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
	 * Method called when the Activity appears on screen
	 */
	@Override
	protected void onResume() {
		super.onResume();

		if (model.getAlarm() == null) {
			btNewAlarm.setText(R.string.home_setNewAlarm);
		} else {
			btNewAlarm.setText(R.string.home_resumeAlarm);
		}

	}

	/**
	 * Manage the action on menu items
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_option_about:
				startActivity(new Intent(this, About_Activity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * OnClickListener result on buttons click
	 */
	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.bt_add_position) {
			Intent i = new Intent(this, PositionCUD_Activity.class);
			startActivity(i);
		} else if (v.getId() == R.id.bt_list_positions) {
			Intent i = new Intent(this, ListPositions_Activity.class);
			startActivity(i);
		} else if (v.getId() == R.id.bt_set_new_alarm) {
			if (model.getAlarm() == null) {
				Intent i = new Intent(this, AlarmSetup_Activity.class);
				startActivity(i);
			} else {
				Intent i = new Intent(this, Alarm_Activity.class);
				startActivity(i);
			}
		} else if (v.getId() == R.id.bt_get_distanceBtw2pos) {
			Intent i = new Intent(this, TabPosChooser_Activity.class);
			startActivity(i);
		}
	}


	/**
	 * OnLocationManager listener : when location change
	 */
	@Override
	public void onLocationChanged(Location loc) {
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			tvLatitude.setText("...");
			tvLongitude.setText("...");
		} else {

			double latv = (double) Math.round(loc.getLatitude() * 10000)
					/ (double) 10000;
			String latd = "N";
			if (latv < 0) {
				latv *= -1;
				latd = "S";
			}

			double lonv = (double) Math.round(loc.getLongitude() * 10000)
					/ (double) 10000;
			String lond = "W";
			if (lonv < 0) {
				lonv *= -1;
				lond = "E";
			}

			tvLatitude.setText(latv + " " + latd);
			tvLongitude.setText(lonv + "  " + lond);
		}
	}

	/**
	 * OnLocationManager listener : when provider change
	 */
	@Override
	public void onProviderDisabled(String arg0) {
		tvLatitude.setText("...");
		tvLongitude.setText("...");
	}

	@Override
	public void onProviderEnabled(String arg0) {
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	}

}
