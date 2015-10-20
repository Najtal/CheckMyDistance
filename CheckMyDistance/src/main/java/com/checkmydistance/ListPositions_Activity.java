package com.checkmydistance;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import bizz.Position;
import dal.DbManager;

/**
 * This class is the activity that manage to list all the positions when 
 * 
 * @author jvdur_000
 *
 */
public class ListPositions_Activity extends Activity {

	private List<Position> positions;

    private DataItemAdapter aa;
    private ListView dataListView;
	
	// MVC & DB Manager
	private DbManager dbm;
	
	
	/**
	 * Method called when created
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_pos);

		// DATA
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
        dataListView = (ListView)findViewById(R.id.list);     
       
        int resID = R.layout.activity_list_ud;
        aa = new DataItemAdapter(this, resID, positions);
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
	 * Manage what happens when option menu are clicked
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
}
