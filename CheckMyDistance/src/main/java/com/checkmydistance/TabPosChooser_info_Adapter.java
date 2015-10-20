package com.checkmydistance;

import java.util.List;

import bizz.DistanceBTW2Positions;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class is the adapter of the TabPosChooser_info Fragment
 * It fills the rows with the name of the positions and the distance between those one
 * 
 * @author jvdur_000
 *
 */
public class TabPosChooser_info_Adapter extends
		ArrayAdapter<DistanceBTW2Positions> {

	private final List<DistanceBTW2Positions> list;
	private final Activity context;

	private final Drawable favIcon;

	/**
	 * Consructor
	 * @param context The context of the Activity who calls
	 * @param list The data used to fill the rows
	 */
	public TabPosChooser_info_Adapter(Activity context,
			List<DistanceBTW2Positions> list) {
		super(context, R.layout.pos_chooser_info, list);
		this.context = context;
		this.list = list;

		favIcon = context.getResources().getDrawable(R.drawable.star_full);
	}

	
	/**
	 * A static ephemeral instance of data
	 * 
	 * @author jvdur_000
	 */
	static class ViewHolder {
		protected TextView posName1;
		protected ImageView favorite1;

		protected TextView posName2;
		protected ImageView favorite2;

		protected TextView distance;
	}

	/**
	 * Generate the rows of the adaptor, return a view
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;

		// Inflater and holder manager
		LayoutInflater inflator = context.getLayoutInflater();
		view = inflator.inflate(R.layout.pos_chooser_info, null);
		final ViewHolder viewHolder = new ViewHolder();

		// Position 1
		viewHolder.posName1 = (TextView) view.findViewById(R.id.pos1);
		viewHolder.favorite1 = (ImageView) view.findViewById(R.id.favori1);

		viewHolder.posName1.setText(list.get(position).getPosFrom().getName());
		if (list.get(position).getPosFrom().isFavorite()) {
			viewHolder.favorite1.setImageDrawable(favIcon);
		}
		
		// Position 2
		viewHolder.posName2 = (TextView) view.findViewById(R.id.pos2);
		viewHolder.favorite2 = (ImageView) view.findViewById(R.id.favori2);

		viewHolder.posName2.setText(list.get(position).getPosTo().getName());
		if (list.get(position).getPosTo().isFavorite()) {
			viewHolder.favorite2.setImageDrawable(favIcon);
		}
		
		// Distance
		viewHolder.distance = (TextView) view.findViewById(R.id.distance);
		viewHolder.distance.setText(list.get(position).getDistance() + " km");
		
		return view;
	}
}