package com.checkmydistance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import dal.DbManager;
import model.GUI_Model;

/**
 * This class is the Activity that will be used
 * 		- when a new position will be registered
 * 		- when a position will be edited
 * 
 * @author jvdur_000
 *
 */
public class PositionCUD_Activity extends Activity implements OnClickListener,
		android.widget.RadioGroup.OnCheckedChangeListener, LocationListener {

	// MVC & DB Manager
	private GUI_Model model;
	private DbManager dbm;

	private long posIdToEdit;
	private boolean editMode;
	
	private Cursor cpe; // all the positions

	// Activity buttons
	private Button btSave;
	private Button btBack;
	private Button btDelete;
	private Button btMap;

	// Layout
	private LinearLayout llInputNewPosition;
	private LinearLayout llDeletePosition;
	private LinearLayout llFindMap;
	private LinearLayout llSaveButton;
	
	// RadioButtons
	// Type of position
	private RadioGroup rbgSelectTypePosition;
	private RadioButton actualPosition;
	private RadioButton differentPosition;
	//private RadioButton mapPosition;
	
	// favorite
	private TextView tvFavorite;
	private boolean favStatut = false;

	// INPUT
	// Latitude
	private EditText newLatitude;
	private RadioButton rbNorth;
	private RadioButton rbSouth;

	// Longitude
	private EditText newLongitude;
	//private RadioGroup rbgNS;
	private RadioButton rbEast;
	private RadioButton rbWest;

	// Location manager
	private LocationManager locationManager;
	//private RadioGroup rbgEW;
	private double latitude;
	private double longitude;

	// Name
	private EditText edName;
	private TextView tvTitle;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cud);

		this.model = GUI_Model.getInstance();
		this.dbm = new DbManager(getApplicationContext());
		this.dbm.open();

		// FIND BY ID
		llInputNewPosition = (LinearLayout) findViewById(R.id.add_ll_inputPosition);
		llDeletePosition = (LinearLayout) findViewById(R.id.add_ll_delete);
		llFindMap = (LinearLayout) findViewById(R.id.add_ll_newPosition_OnMap);
		llSaveButton = (LinearLayout) findViewById(R.id.add_ll_save);

		// Buttons
		btSave = (Button) findViewById(R.id.bt_add_save);
		btBack = (Button) findViewById(R.id.bt_add_cancel);
		btDelete = (Button) findViewById(R.id.bt_add_delete);
		btMap = (Button) findViewById(R.id.add_bt_findOnMap);
		
		btSave.setOnClickListener(this);
		btBack.setOnClickListener(this);
		btDelete.setOnClickListener(this);
		btMap.setOnClickListener(this);
		
		// Title
		tvTitle = (TextView) findViewById(R.id.textView1);
		
		// favorite button
		tvFavorite = (TextView) findViewById(R.id.add_imgbt_fav);
		tvFavorite.setOnClickListener(this);
		

		// Input type radio buttons
		rbgSelectTypePosition = (RadioGroup) findViewById(R.id.add_rbg_radioTypeInput);
		actualPosition = (RadioButton) findViewById(R.id.add_rb_inputType_actualPosition);
		differentPosition = (RadioButton) findViewById(R.id.add_rb_inputType_newPosition);
		//mapPosition = (RadioButton) findViewById(R.id.add_rb_inputType_newPosition_OnMap);
		
		rbgSelectTypePosition.setOnCheckedChangeListener(this);
		actualPosition.setChecked(true);

		
		// Input new location
		newLatitude = (EditText) findViewById(R.id.add_te_latitude);
		//rbgNS = (RadioGroup) findViewById(R.id.add_rbg_radioLatNS);
		rbNorth = (RadioButton) findViewById(R.id.add_rb_North);
		rbSouth = (RadioButton) findViewById(R.id.add_rb_South);

		newLongitude = (EditText) findViewById(R.id.add_te_longitude);
		//rbgEW = (RadioGroup) findViewById(R.id.add_rbg_radioLonEW);
		rbEast = (RadioButton) findViewById(R.id.add_rb_East);
		rbWest = (RadioButton) findViewById(R.id.add_rb_West);

		edName = (EditText) findViewById(R.id.add_et_positionName);

		// LOCATION MANGER
		locationManager = (LocationManager) getApplicationContext()
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);

		// load the positions
		cpe = dbm.getAllPositions();
		
		posIdToEdit = model.getCUPPosValue();
		model.setCUDPosValue(-1);
		
		llSaveButton.setVisibility(View.VISIBLE);
		
		/*
		 * Here are the changes made if the activity is used to 
		 * add a new Position or if used to edit a existing position
		 */
		if (posIdToEdit >= 0) {
			editMode = true;
			setValues();
			
			llDeletePosition.setVisibility(View.VISIBLE);
		} else {
			editMode = false;
			
			// remember where the app where first launched :)
			if (cpe.getCount() == 0) {
				dbm.insertPosition("first connexion", 0.0, 0.0, false);
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

	@Override
	protected void onResume() {
		super.onResume();
		if (model.getModeNewPosition() == 2) {
			if (latitude < 0) {
				rbSouth.setChecked(true);
				latitude *= -1;
			} else {
				rbNorth.setChecked(true);
			}
			newLatitude.setText("" + model.getPosLatitude());

			if (longitude < 0) {
				rbEast.setChecked(true);
				longitude *= -1;
			} else {
				rbWest.setChecked(true);
			}
			newLongitude.setText("" + model.getPosLongitude());

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
	 * Set the values of the known data in case of edit
	 */
	private void setValues() {
		
		// On recupere les infos concernant cette position
		//Cursor cpe = dbm.getPosition(posIdToEdit+2);
		long dec = posIdToEdit;

		cpe.moveToFirst();
		
		while (dec != 0) {
			dec--;
			cpe.moveToNext();
		}
		
		System.out.println("pos qu'on check : " + cpe.getInt(0) + " : " + cpe.getString(1));
		posIdToEdit = cpe.getInt(0);
		
		String name = cpe.getString(1);
		latitude = cpe.getDouble(2);
		longitude = cpe.getDouble(3);
		int fav = cpe.getInt(4);
		if (fav == 1) {
			favStatut = true;
		}

		differentPosition.setChecked(true);

		edName.setText(name);
		tvTitle.setText("edit " + name);

		if (latitude < 0) {
			rbSouth.setChecked(true);
			latitude *= -1;
		} else {
			rbNorth.setChecked(true);
		}

		newLatitude.setText("" + latitude);

		if (longitude < 0) {
			rbEast.setChecked(true);
			longitude *= -1;
		} else {
			rbWest.setChecked(true);
		}

		newLongitude.setText("" + longitude);
		
		if (favStatut) {
			tvFavorite.setCompoundDrawablesWithIntrinsicBounds( R.drawable.star_full, 0, 0, 0);
		}
		

	}

	/**
	 * Method called when a button is clicked
	 */
	@Override
	public void onClick(View v) {

		/*
		 * FAV BUTTON
		 */
		if (v.getId() == R.id.add_imgbt_fav) {
			if (favStatut) {
				tvFavorite.setCompoundDrawablesWithIntrinsicBounds( R.drawable.star, 0, 0, 0);
			} else {
				tvFavorite.setCompoundDrawablesWithIntrinsicBounds( R.drawable.star_full, 0, 0, 0);
			}
			favStatut = !favStatut;
			
			
		/*
		 * MAP BUTTON
		 */
		} else if (v.getId() == R.id.add_bt_findOnMap) {
			model.setPosLatitude(latitude);
			model.setPosLongitude(longitude);
			Intent i = new Intent(this, MapsActivity.class);
			startActivity(i);
			
		/*
		 * SAVE BUTTON
		 */
	    } else if (v.getId() == R.id.bt_add_save) {
			double inLatitude = 0;
			double inLongitude = 0;

			// si on prend la position actuelle
			if (actualPosition.isChecked()) {
				if (!locationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					Toast.makeText(getApplicationContext(),
							"Your location is not available",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (edName.getText().length() < 2) {
					Toast.makeText(
							getApplicationContext(),
							"The name of the location should have 2 to 50 caracters",
							Toast.LENGTH_SHORT).show();
					return;
				}
				inLatitude = latitude;
				inLongitude = longitude;

			} else {
				// on test les valeurs
				inLatitude = Double.parseDouble(newLatitude.getText()
						.toString());
				inLongitude = Double.parseDouble(newLongitude.getText()
						.toString());

				// on verifie les valeurs
				if (inLatitude > 90 || inLatitude < 0) {
					Toast.makeText(getApplicationContext(),
							"The latitude should be between 0° and 90°",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (inLongitude > 180 || inLongitude < 0) {
					Toast.makeText(getApplicationContext(),
							"The longitude should be between 0° and 180°",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (edName.getText().length() < 2) {
					Toast.makeText(
							getApplicationContext(),
							"The name of the location should have 2 to 50 caracters",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (!(rbNorth.isChecked() || rbSouth.isChecked())) {
					Toast.makeText(getApplicationContext(), "North or South ?",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (!(rbEast.isChecked() || rbWest.isChecked())) {
					Toast.makeText(getApplicationContext(), "East or West ?",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (rbSouth.isChecked()) {
					inLatitude *= -1;
				}

				if (rbEast.isChecked()) {
					inLongitude *= -1;
				}

			}

			
			
			// APPLY SAVE CHANGES
			if (editMode) {
				dbm.updatePosition(posIdToEdit, edName.getText().toString(), inLatitude, inLongitude, favStatut);

				Toast.makeText(getApplicationContext(),
						edName.getText().toString() + " sucessfully updated !",
						Toast.LENGTH_SHORT).show();
				
				Intent i = new Intent(this, ListPositions_Activity.class);
				startActivity(i);
				
			} else {
				dbm.insertPosition(edName.getText().toString(),
						inLatitude, inLongitude, favStatut);

				Toast.makeText(getApplicationContext(),
						edName.getText().toString() + " sucessfully saved !",
						Toast.LENGTH_SHORT).show();
			}
			finish();
		}
		
		/*
		 *  DELETE BUTTON
		 */
		if (editMode && v.getId() == R.id.bt_add_delete) {
			System.out.println("delete : " + posIdToEdit);
			if (dbm.deletePosition(posIdToEdit)) {
				Toast.makeText(getApplicationContext(),
						edName.getText().toString() + " sucessfully deleted !",
						Toast.LENGTH_SHORT).show();
				
				Intent i = new Intent(this, ListPositions_Activity.class);
				startActivity(i);
				
				finish();
				
			} else {
				Toast.makeText(getApplicationContext(),
						"Sorry, " + edName.getText().toString() + " couldn't be deleted !",
						Toast.LENGTH_SHORT).show();
			}
			
			
		}

		/*
		 *  CANCEL BUTTON
		 */
		if (v.getId() == R.id.bt_add_cancel) {
			finish();
		}
	}

	
	/**
	 * Called when a Radio button group change
	 */
	@Override
	public void onCheckedChanged(RadioGroup rg, int i) {

		if (rg.getId() == R.id.add_rbg_radioTypeInput) {
			
			if (i == R.id.add_rb_inputType_actualPosition) {
				llInputNewPosition.setVisibility(View.GONE);
				llFindMap.setVisibility(View.VISIBLE);
				llSaveButton.setVisibility(View.VISIBLE);
				model.setModeNewPosition(1);
				
			} else if (i == R.id.add_rb_inputType_newPosition) {
				llFindMap.setVisibility(View.VISIBLE);
				llSaveButton.setVisibility(View.VISIBLE);
				llInputNewPosition.setVisibility(View.VISIBLE);
				model.setModeNewPosition(2);

			} else if (i == R.id.add_rb_inputType_newPosition_OnMap) {
				llInputNewPosition.setVisibility(View.GONE);
				llSaveButton.setVisibility(View.GONE);
				llFindMap.setVisibility(View.VISIBLE);
			}
		}

	}

	
	/*
	 * POSITION LISTENER
	 */
	
	/**
	 * Method called when position change
	 */
	@Override
	public void onLocationChanged(Location loc) {
		latitude = loc.getLatitude();
		longitude = loc.getLongitude();
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
