package com.checkmydistance;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;
import bizz.DistanceBTW2Positions;
import bizz.Position;
import bizz.PositionChooser;
import model.GUI_Model;

/**
 * This class is the Fragment used in TabPosChooser_Activity
 * It uses the Adapte TabPosChooser_info_Adapter
 * 
 * @author jvdur_000
 *
 */
public class TabPosChooser_info extends ListFragment {
	
	// MVC & DB Manager
	private GUI_Model model;
	
	
	/**
	 * Method called when created
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		this.model = GUI_Model.getInstance();
		
		//return inflater.inflate(R.layout.pos_chooser__list, container, false);
	}
	
	/**
	 * Method called when the activity appears
	 */
	@Override
	public void onResume() {
		super.onResume();
		ArrayAdapter<DistanceBTW2Positions> adapter = new TabPosChooser_info_Adapter(getActivity(), setContent());
		setListAdapter(adapter);
	}
	
	/**
	 * Retrieve and compute all the data that will be shown
	 */
	private List<DistanceBTW2Positions> setContent() {
		
		// On recupere toute les positions selectionnees
		List<Position> selectedPositions = new ArrayList<Position>();		

		for (int i=0; i < model.getListOfElements().size() ; i++) {
			if (((PositionChooser)model.getListOfElements().get(i)).isSelected())
				selectedPositions.add(((PositionChooser)model.getListOfElements().get(i)).getPosition());
		}
		
		// On genere les positions
		List<DistanceBTW2Positions> distPosToComp = new ArrayList<DistanceBTW2Positions>();	
		
		for (int i = 0; i < selectedPositions.size()-1; i++) {
			for (int j = i+1; j < selectedPositions.size(); j++) {
				distPosToComp.add(new DistanceBTW2Positions(selectedPositions.get(i), selectedPositions.get(j)));
			}
		}
		return distPosToComp;
	}
	
}