package com.checkmydistance;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import bizz.Position;

/**
 * This class is an Adapter of the AlarmSetup_Activity It manage every row of
 * the list of positions
 * 
 * @author jvdur_000
 * 
 */
public class AlarmSetup_Adapter extends ArrayAdapter<Position> {

	private int resource;
	private LayoutInflater inflater = null;
	private final Drawable favIcon;
	private Context context;

	/**
	 * Constructor
	 * @param _activity The Activity who calls it
	 * @param _resource The layer id the adapter will fill
	 * @param _items The data to fill (positions)
	 */
	public AlarmSetup_Adapter(Activity _activity, int _resource,
			List<Position> _items) {
		super(_activity, _resource, _items);

		this.inflater = (LayoutInflater) _activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.resource = _resource;
		this.context = getContext();

		favIcon = getContext().getResources().getDrawable(R.drawable.star_full);
	}

	/**
	 * A static ephemeral instance of data
	 * 
	 * @author jvdur_000
	 *
	 */
	public static class ViewHolder {
		ImageView favori;
		TextView nom;
	}

	/**
	 * Generate the rows of the adaptor, return a view
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		ViewHolder holder;

		// Inflater and holder manager
		if (convertView == null) {
			vi = inflater.inflate(resource, null);
			holder = new ViewHolder();

			holder.favori = (ImageView) vi.findViewById(R.id.favori);
			holder.nom = (TextView) vi.findViewById(R.id.nom);

			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}

		Position item = getItem(position);

		// set the data in the row
		if (item.isFavorite())
			holder.favori.setImageDrawable(favIcon);
		holder.nom.setText(item.getName());
		holder.nom.setTag(position);

		// If we click on the row
		final OnClickListener listener = new OnClickListener() {
			int pos = position;

			@Override
			public void onClick(View v) {

				((AlarmSetup_Activity) context).actionToGo(pos);
			}
		};
		vi.setOnClickListener(listener);

		return vi;
	}
}