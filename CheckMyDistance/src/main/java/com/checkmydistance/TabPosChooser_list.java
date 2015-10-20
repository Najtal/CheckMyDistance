package com.checkmydistance;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import bizz.Position;
import bizz.PositionChooser;
import dal.DbManager;
import exceptions.InvalidArgumentException;
import model.GUI_Model;

/**
 * This class is the Fragment that is used by TabPoschooser_Activity to list all the positions
 * It uses the Adapter TabPosChooser_list_Adapter
 * 
 * @author jvdur_000
 *
 */
public class TabPosChooser_list extends ListFragment {

	// Context
	private Context context;

	// MVC & DB Manager
	private GUI_Model model;
	private DbManager dbm;

	/**
	 * Method called when created
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		this.context = getActivity().getApplicationContext();
		this.model = GUI_Model.getInstance();
		this.dbm = new DbManager(context);

		// On enregistre dans le modele la liste d'elements
		if (model.getListOfElements() == null) {
			model.setListOfElements(getModel());
		}

		ArrayAdapter<PositionChooser> adapter = new TabPosChooser_list_Adapter(
				getActivity(), model.getListOfElements());

		setListAdapter(adapter);
	}

	/**
	 * Recupere les donnees et les enregistre dans la liste des position chooser
	 * 
	 * @return list of position chooser
	 */
	private List<PositionChooser> getModel() {

		// open DB
		this.dbm.open();

		List<PositionChooser> list = new ArrayList<PositionChooser>();

		Cursor cp = dbm.getAllPositions();

		// remember where the app where first launched :)
		if (cp.getCount() < 2) {
			Toast.makeText(context,
					R.string.toastMsgAddSomePossitions,
					Toast.LENGTH_SHORT).show();
			getActivity().finish();
		}

		for (cp.moveToFirst(); cp.moveToNext(); cp.isAfterLast()) {
			boolean f = false;
			if (cp.getInt(4) == 1)
				f = true;

			try {
				PositionChooser pc = new PositionChooser(new Position(
						cp.getDouble(2), cp.getDouble(3), cp.getString(1), f));
				list.add(pc);
			} catch (InvalidArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		dbm.close();
		return list;
	}
}