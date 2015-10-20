package com.checkmydistance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import model.GUI_Model;

/**
 * 
 * This class is a Fragment that appears in Alarm_Activity 
 * 
 * @author jvdur_000
 *
 */
public class AlarmMain_Fragment extends Fragment {

	private GUI_Model model;
	
	private View view;
	private int pourcentage = 0;
	private View circle;

	/**
	 * Method called when Fragment is created, return View to display
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.model = GUI_Model.getInstance();
		view = inflater.inflate(R.layout.activity_alarm_main, container,
				false);
		circle = (View) new AlarmMainCircle_View(getActivity().getApplicationContext(), pourcentage);
		
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.alarm_circle_ll);
		ll.addView(circle);
		
		
		return view;
	}

	/**
	 * Called when the activity appears (again) or is refresh
	 */
	@Override
	public void onResume() {
		super.onResume();

		try {
			TextView title = (TextView) getView().findViewById(R.id.alarm_title);
			title.setText("Alarm for " + model.getAlarm().getPosition().getName());
			
			circle.invalidate();	
		} catch (Exception e) {
		}		
	}
	

}