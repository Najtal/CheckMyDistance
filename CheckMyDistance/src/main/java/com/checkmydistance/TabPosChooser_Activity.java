package com.checkmydistance;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;

import model.GUI_Model;

/**
 * This class is the Activity that will manage the UC distance between two positions.
 * It holds two tabs
 * 	- TabPosChooser_list : who list the positions with a checkbox
 * 	- TabPosChooser_info : who give the distance between the selected positions
 * 
 * This class has mostly been structures by Eclipse
 * 
 * @author jvdur_000
 *
 */
public class TabPosChooser_Activity extends FragmentActivity implements OnPageChangeListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	private final static int AMOUNT_TABS = 2;
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	private GUI_Model model;
	
	@SuppressWarnings("unused")
	private Fragment fList;
	private Fragment fInfo;

	
	/**
	 * Method called when created
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.model = GUI_Model.getInstance();

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
				
		mViewPager.setOnPageChangeListener(this);
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
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		/**
		 * Constructor
		 * @param fm Fragment manager of the Activity
		 */
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		/**
		 * When a tab is selected
		 */
		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			Fragment fragment = null;
			if (position == 0) {
				fragment = new TabPosChooser_list();
				fList = fragment;
			} else if (position == 1) {
				fragment = new TabPosChooser_info();
				fInfo = fragment;
			}
			
			Bundle bld = new Bundle();
			fragment.setArguments(bld);
			
			return fragment;
		}

		/**
		 * return the amount of tabs
		 */
		@Override
		public int getCount() {
			return AMOUNT_TABS;
		}

		/**
		 * Return the tab titles
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.select_pos_select).toUpperCase(l);
			case 1:
				return getString(R.string.select_pos_info).toUpperCase(l);
			}
			return null;
		}
		
		
	}
	
	/**
	 * Called when trhe activity is destroyed
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		model.setListOfElements(null);
	}

	/**
	 * Called when a page (TAB) is selected
	 */
	@Override
	public void onPageSelected(int pagePos) {
		if (pagePos == 1) {
			Log.i("xyz", "page 2 select");
			fInfo.onResume();
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

}
