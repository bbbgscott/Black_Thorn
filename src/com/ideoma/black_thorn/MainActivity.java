package com.ideoma.black_thorn;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	
	private final int OPEN_SECURITY_SETTINGS = 27;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final LocationManager locmanager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		if(!locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			final AlertDialog ad = new AlertDialog.Builder(this).create();
			ad.setTitle("Warning!");
			ad.setMessage("You do not have your GPS location service activate! The map won't function!");
			ad.setButton(AlertDialog.BUTTON_POSITIVE,"Take me to the app anyway", new DialogInterface.OnClickListener()
			{
				@Override public void onClick(DialogInterface dialog, int which) { ad.dismiss(); }
			});
			ad.setButton(AlertDialog.BUTTON_NEGATIVE, "Exit", new DialogInterface.OnClickListener() 
			{
				@Override public void onClick(DialogInterface dialog, int which) { finish(); }
			});
			ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Enable GPS", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					EnableGPS();
				}
			});
			ad.show();
		}
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setCurrentItem(1);
		mViewPager.setOffscreenPageLimit(2);

	}
	
	public void EnableGPS()
	{
		Intent intent = new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS);
		startActivityForResult(intent,OPEN_SECURITY_SETTINGS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			//Fragment fragment = new DummySectionFragment();
			Fragment pageFragment = null;
			final LatLng Monterey = new LatLng(36.6003, 121.8936);
			MapFragment frag = null;
			GoogleMap map = null;
			Bundle args = null;
			switch(position) {
			case 0:
				pageFragment = new DummySectionFragment();
				args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				args.putInt("view", R.layout.aboutuslayout);
				pageFragment.setArguments(args);
				return pageFragment;
			case 1:
				pageFragment = new DummySectionFragment();
				args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				args.putInt("view", R.layout.mainlayout);
				pageFragment.setArguments(args);
				return pageFragment;
			case 2:
				pageFragment = new MyMapFragment();
				try{
					//map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
				} catch(Exception e) {
					if(getFragmentManager().findFragmentById(R.id.map) == null) {
						Log.e("maperr", "Fragment is null");
					}
					Log.e("maperr", "id: " + R.id.map);
					Log.e("maperr", "Error: " + e);
				}
				
				//Marker monterey = map.addMarker(new MarkerOptions().position(Monterey).title("Monterey"));
				args = new Bundle();
				return pageFragment;
			default:
				pageFragment = new DummySectionFragment();
				args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				args.putInt("view", R.layout.fragment_main_dummy);
				pageFragment.setArguments(args);
				return pageFragment;
			}
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return "Hello";
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		public int layout = R.layout.fragment_main_dummy;
		Bundle savedInstanceState;
		
		public DummySectionFragment() {
			savedInstanceState.get("view");
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(layout,
					container, false);
			return rootView;
		}
	}
	
	public static class MyMapFragment extends Fragment {
		static final LatLng Monterey = new LatLng(36.6003, 121.8936);
		private GoogleMap map;
		
		public MyMapFragment() {
			
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = null;
			try {
				rootView = inflater.inflate(R.layout.map, container, false);
			} catch(InflateException e) {
				Log.e("mapstuff", "Error, Will Robinson: " + e);
			}
			Log.w("mapid", R.id.map + "");
			
			//map = (GoogleMap)((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			//map = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
			
			return rootView;
		}
	}

	LatLng[] GetCsumbGpsCoords()
	{
		try {
			ArrayList<LatLng> coords = new ArrayList<LatLng>();
			InputStream in = getResources().openRawResource(R.xml.csumb_gps_coordinates);
			XMLParser parser = new XMLParser();
		
			Document doc = parser.ParseXMLToDoc(in);
			NodeList nList = parser.ParseDocByTagName(doc, "lat_long_pts");
			for(int i = 0; i < nList.getLength(); i++)
			{
				long lat = 0, lng = 0;
				Element ele = (Element) nList.item(i);
				lat = Long.parseLong(parser.GetTextValueByTagName(ele, "lat"));
				lng = Long.parseLong(parser.GetTextValueByTagName(ele, "lat"));
				coords.add(new LatLng(lat,lng));
			}
			LatLng[] latlngarray = new LatLng[coords.size()];
			coords.toArray(latlngarray);
			return latlngarray;
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
