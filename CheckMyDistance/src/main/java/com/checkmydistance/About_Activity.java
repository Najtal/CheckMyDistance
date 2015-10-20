package com.checkmydistance;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * 
 * This class is the Activity who manage the ABOUT screen
 * 
 * @author jvdur_000
 *
 */
public class About_Activity extends Activity {

	/**
	 * Method called when created
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

	/**
	 * Method that manage the option menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.about_, menu);
		return true;
	}

}
