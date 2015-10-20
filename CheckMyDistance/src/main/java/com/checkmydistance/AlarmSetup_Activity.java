package com.checkmydistance;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import bizz.Alarm;
import bizz.Position;
import dal.DbManager;
import model.GUI_Model;

/**
 * 
 * This Activity list all the positions.
 * It comes as setup of the Alarm
 * 
 * @author jvdur_000
 *
 */
public class AlarmSetup_Activity extends Activity {

	// MVC & DB Manager
	private GUI_Model model;
	private DbManager dbm;
	
	private List<Position> positions;
    private AlarmSetup_Adapter aa;
    private ListView dataListView;
    private EditText etRadius;
    

		
	
	/**
	 * Method called when created
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_setup);
		
		etRadius = (EditText) findViewById(R.id.alarm_et_radius);
		
		// DATA
		this.model = GUI_Model.getInstance();
		this.dbm = new DbManager(getApplicationContext());
		this.dbm.open();
		
		try {
			 positions = dbm.getListPositions();
			 dbm.close();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), R.string.toastMsgAddSomePossitions, Toast.LENGTH_LONG).show();	
			finish();
			return;
		}

		
        // ADAPTATOR
        dataListView = (ListView)findViewById(R.id.aas_list);     
       
        int resID = R.layout.activity_list_ud;
        aa = new AlarmSetup_Adapter(this, resID, positions);
        dataListView.setAdapter(aa);
        dataListView.setItemsCanFocus(true);
        
	    aa.notifyDataSetChanged();
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
	 * Method that is called when an Alarm is launched
	 * @param position
	 */
	public void actionToGo(int position) {
		
		if (etRadius.length() == 0) {
			toast();
			return;
		}
		
		int rayon = Integer.parseInt(etRadius.getText().toString());
		
		Alarm a = new Alarm(rayon, positions.get(position));
		model.setAlarm(a);
		
		Intent i = new Intent(getApplicationContext(), Alarm_Activity.class);
		startActivity(i);
		
		finish();
	}
	
	/**
	 * Show a Toast
	 */
	private void toast() {
		Toast.makeText(getApplicationContext(), "please enter a radius distance for the alarm", Toast.LENGTH_SHORT).show();
	}
	
}
