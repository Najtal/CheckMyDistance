package com.checkmydistance;

import model.GUI_Model;
import services.PositionAlarmService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import bizz.Alarm;

/**
 * 
 * This class is a Fragment that appears in Alarm_Activity 
 * 
 * @author jvdur_000
 *
 */
public class AlarmGraph_Fragment extends Fragment implements OnClickListener {
	
	// Data retriever
	private GUI_Model model;
	private Alarm alarm;

	// TextViews
	private TextView title;
	private TextView alarm_tv_destPos_Lat;
	private TextView alarm_tv_destPos_Lon;
	private TextView alarm_tv_lastPos_Lat;
	private TextView alarm_tv_lastPos_Lon;
	private TextView alarm_tv_radius;
	private TextView alarm_tv_distAlarm;
	private TextView alarm_tv_distDest;
	
	// Buttons of menu
	private Button alarm_bt_back;
	private Button alarm_bt_quit;

	/**
	 * Method called when Fragment is created, return View to display
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.model = GUI_Model.getInstance();
		View view = inflater.inflate(R.layout.activity_alarm_det,
				container, false);

		this.alarm = model.getAlarm();
		
		this.alarm_tv_destPos_Lat = (TextView) view.findViewById(R.id.alarm_destPos_Lat);
		this.alarm_tv_destPos_Lon = (TextView) view.findViewById(R.id.alarm_destPos_Lon);
		this.alarm_tv_lastPos_Lat = (TextView) view.findViewById(R.id.alarm_lastPos_Lat);
		this.alarm_tv_lastPos_Lon = (TextView) view.findViewById(R.id.alarm_lastPos_Lon);
		this.alarm_tv_radius = (TextView) view.findViewById(R.id.alarm_radius);
		this.alarm_tv_distAlarm = (TextView) view.findViewById(R.id.alarm_distAlarm);
		this.alarm_tv_distDest = (TextView) view.findViewById(R.id.alarm_distDest);
		
		this.alarm_bt_back = (Button) view.findViewById(R.id.alarm_bt_back);
		this.alarm_bt_quit = (Button) view.findViewById(R.id.alarm_bt_stopAlarm);

		this.alarm_bt_back.setOnClickListener(this);
		this.alarm_bt_quit.setOnClickListener(this);

		this.title = (TextView) view.findViewById(R.id.alarm_title);
		this.title.setText(model.getAlarm().getPosition().getName());
		
		return view;
	}
	
	/**
	 * Called when the activity appears (again) or is refresh
	 */
	@Override
	public void onResume() {
		super.onResume();
		
		if (alarm.firstKnownLat > 0)
			alarm_tv_destPos_Lat.setText(alarm.firstKnownLat + " N");
		else 
			alarm_tv_destPos_Lat.setText(alarm.firstKnownLat*-1 + " S");
		
		
		if (alarm.firstKnownLon > 0) 
			alarm_tv_destPos_Lon.setText(alarm.firstKnownLon + " W");
		else 
			alarm_tv_destPos_Lon.setText(alarm.firstKnownLon*-1 + " E");

		
		
		
		if (alarm.getLastKnownPositionLat() > 0)
			alarm_tv_lastPos_Lat.setText(alarm.getLastKnownPositionLat() + " N");
		else 
			alarm_tv_lastPos_Lat.setText(alarm.getLastKnownPositionLat()*-1 + " S");
		
		if (alarm.getLastKnownPositionLon() > 0) 
			alarm_tv_lastPos_Lon.setText(alarm.getLastKnownPositionLon() + " W");
		else 
			alarm_tv_lastPos_Lon.setText(alarm.getLastKnownPositionLon()*-1 + " E");
		
		alarm_tv_radius.setText(alarm.getRayon() + " meters");

		
		double distDestM = alarm.getLastDistanceCalculated();
		double distAlarmM = alarm.getLastDistanceCalculated()-alarm.getRayon();

		if (distDestM > 1000)
			alarm_tv_distDest.setText((distDestM/1000) + " km");
		else 
			alarm_tv_distDest.setText((int) distDestM + " meters");


		if (distAlarmM > 1000)
			alarm_tv_distAlarm.setText(distAlarmM/1000 + " km");
		else 
			alarm_tv_distAlarm.setText((int) distAlarmM + " km");

	}

	/**
	 * When one of the buttons are clicked
	 */
	@Override
	public void onClick(View v) {
		
		if (v.getId() == R.id.alarm_bt_stopAlarm) {
			/*
			 *  On ferme le service
			 */
			//Alarm_Activity.instance.intentMyIntentService.setFlags(2);
			PositionAlarmService.continueProcess = false;
			/*
			 *  On modifie l'alarme dans le model
			 */
			model.setAlarm(null);
			getActivity().finish();
			//Intent i = new Intent(getActivity(), Home_Activity.class);
			//startActivity(i);
		} else {
			Intent i = new Intent(getActivity(), Home_Activity.class);
			startActivity(i);
		}
	}

}