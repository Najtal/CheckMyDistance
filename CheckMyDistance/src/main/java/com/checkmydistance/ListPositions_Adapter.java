package com.checkmydistance;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class is the adapter used by ListPositions_Activity
 * It manage all the rows of the list. Rows of positions
 * 
 * @author jvdur_000
 *
 */
public class ListPositions_Adapter extends ArrayAdapter<String> {
	private final String[] values;
	private final Boolean[] favorites;
	
	private final Drawable favIcon;
	private LayoutInflater inflater;

	/**
	 * Constructor
	 * @param context The context of the Activity who calls it
	 * @param values The data to populate the rows
	 * @param favorites2 an other data table with favorite info about the positions
	 */
	public ListPositions_Adapter(Context context, String[] values, Boolean[] favorites2) {
		super(context, R.layout.activity_list_ud, values);
		
		inflater = LayoutInflater.from(context);
		
		this.values = values;
		this.favorites = favorites2;
		
		favIcon = context.getResources().getDrawable( R.drawable.star_full );
	}

	/**
	 * Generate the rows of the adaptor, return a view
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = inflater.inflate(R.layout.activity_list_ud, parent, false);
		
		// nom de la position
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		textView.setText(values[position]);
		
		
		// favorite
		ImageView img = (ImageView) rowView.findViewById(R.id.favori);
		if (favorites[position]) {
			img.setImageDrawable(favIcon);
		}
		
		return rowView;
	}
}