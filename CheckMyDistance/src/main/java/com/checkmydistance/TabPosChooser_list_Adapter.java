package com.checkmydistance;

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import bizz.PositionChooser;

/**
 * This class is the adapter used in TabPosChooser_list.
 * It list all the positions with a checkbox
 * 
 * @author jvdur_000
 *
 */
public class TabPosChooser_list_Adapter extends ArrayAdapter<PositionChooser> {

	private final List<PositionChooser> list;
	private final Activity context;
	
	private final Drawable favIcon;

	/**
	 * Constructor
	 * @param context The Context of the activity who calls
	 * @param list	The data used to populate the list
	 */
	public TabPosChooser_list_Adapter(Activity context, List<PositionChooser> list) {
		super(context, R.layout.pos_chooser_list, list);
		this.context = context;
		this.list = list;
		
		favIcon = context.getResources().getDrawable( R.drawable.star_full );
	}

	
	/**
	 * A static ephemeral instance of data
	 * 
	 * @author jvdur_000
	 */
	static class ViewHolder {
		protected TextView text;
		protected CheckBox checkbox;
		protected ImageView favorite;
	}

	/**
	 * Generate the rows of the adaptor, return a view
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.pos_chooser_list, null);
			final ViewHolder viewHolder = new ViewHolder();
			
			// Text
			viewHolder.text = (TextView) view.findViewById(R.id.label);
			
			
			// favorite
			viewHolder.favorite = (ImageView) view.findViewById(R.id.favori);
			if (list.get(position).getPosition().isFavorite()) {
				viewHolder.favorite.setImageDrawable(favIcon);
			}
			
			// Check box
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
			viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				/**
				 * Called when a checkbox is selected or unselected
				 */
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
						PositionChooser element = (PositionChooser) viewHolder.checkbox.getTag();
						element.setSelected(buttonView.isChecked());
	
					}
				});
			
			
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(list.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
		}
		
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.text.setText(list.get(position).getPosition().getName());
		holder.checkbox.setChecked(list.get(position).isSelected());
		return view;
	}
}