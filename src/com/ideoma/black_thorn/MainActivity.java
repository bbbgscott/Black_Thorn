package com.ideoma.black_thorn;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.Global;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends FragmentActivity {
	GoogleMap map = null;
	public static String fragTag;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	
	String MainSiteUrl = "http://www.sustainablemontereycounty.org/monterey-green-action.html";
	
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
		
		//setUpMapIfNeeded();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//setUpMapIfNeeded();
	}
	
	private void setUpMapIfNeeded() {
		final LatLng Monterey = new LatLng(36.6003, 121.8936);
		Log.e("fragerr", getSupportFragmentManager().findFragmentById(R.id.mappy).toString());
		if(map == null) {
			Log.e("fragerr", "startif");
			//map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mappy)).getMap();
			if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) == 0)
	        {
	            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentByTag(fragTag)).getMap();
	            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	            map.addMarker(new MarkerOptions().position(Monterey).title("Monterey"));
	            //Marker monterey = map.addMarker(new MarkerOptions().position(Monterey).title("Monterey"));
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(Monterey, 15));
				map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	        }
			Log.e("fragerr", "afterif");
			if(map != null) {
				setUpMap();
			}
		}
	}
	
	private void setUpMap() {
		map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
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
			Fragment pageFragment;
			final LatLng Monterey = new LatLng(36.6003, 121.8936);
			Bundle args = null;
			
			switch(position) {
			case 0:
				pageFragment = new AboutFragment();
				break;
			case 1:
				pageFragment = new WelcomeFragment();
				break;
			case 2:
				//MyMapFragment mapFrag = new MyMapFragment();
				pageFragment = new MyMapFragment();
				//map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mappy)).getMap();
				/*Log.e("fragerr", "about to map");
				Log.e("fragerr", R.id.mappy + "");
				if(map == null){
					Log.e("fragerr", "map is null");
					int x = 1;
					while(map == null){
						Log.e("fragerr", x + "");
						try{
							MapFragment mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.mappy);
							if(mapFrag != null) {Log.e("fragerr", "mapFrag works");}
							map = mapFrag.getMap();
						} catch(Exception e) {
							x++;
							continue;
						}
					}
					if(map != null) {
						Log.e("fragerr", "Map available");
					}
				} else {
					Log.e("fragerr", "map not null");
				}
				
				Marker monterey = map.addMarker(new MarkerOptions().position(Monterey).title("Monterey"));
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(Monterey, 15));
				map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);*/
				break;
			default:
				pageFragment = new DummySectionFragment();
				args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				args.putInt("view", R.layout.fragment_main_dummy);
				pageFragment.setArguments(args);
				return pageFragment;
			}
			return pageFragment;
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
		
		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			try {
				layout = savedInstanceState.getInt("view");
			} catch(Exception e) {
				Log.e("viewerr", "View Error: " + e);
				Log.e("viewerr", "Layout: " + layout);
				if(savedInstanceState == null) {
					Log.e("viewerr", "Bundle is empty");
					layout = R.layout.fragment_main_dummy;
				}else{
					Log.e("viewerr", "Bundle is not empty");
				}
				
			}
			View rootView = inflater.inflate(layout,
					container, false);
			return rootView;
		}
	}
	
	public static class MyMapFragment extends Fragment {
		//final LatLng Monterey = new LatLng(36.6003, 121.8936);
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
			
			FragmentTransaction trans = getFragmentManager().beginTransaction();
			//trans.add(this, this.getTag());
			fragTag = this.getTag();
			//trans.commit();
			//map = ((MapFragment) getActivity().getFragmentManager().getFragment(savedInstanceState, R.id.mappy + "")).getMap();
			Log.e("fragerr", "Hi There");
			/*MapFragment fragThing = (MapFragment) this.getFragmentManager().findFragmentById(R.id.mappy);
			map = ((MapFragment) fragThing).getMap();
			Marker monterey = map.addMarker(new MarkerOptions().position(Monterey).title("Monterey"));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(Monterey, 15));
			map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);*/
			
			//map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mappy)).getMap();
			//map = ((MapFragment) rootView.findViewById(R.layout.map).getTag(41)).getMap();
			//Log.e("fragerr", rootView.findViewById(R.layout.map).toString());
		
			return rootView;
		}
		
		public GoogleMap getMyMap() {
			return map;
		}
		/*
		@Override
		public void onResume() {
			Log.e("fragerr", "starting resume");
			super.onResume();
			Log.e("fragerr", "ending resume");
		}*/
	}

	public static class WelcomeFragment extends Fragment {
		//static final LatLng Monterey = new LatLng(36.6003, 121.8936);
		//private GoogleMap map;
		
		public WelcomeFragment() {
			
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = null;
			try {
				rootView = inflater.inflate(R.layout.mainlayout, container, false);
			} catch(InflateException e) {
				Log.e("mapstuff", "Error, Will Robinson: " + e);
			}
			Log.w("mapid", R.id.mappy + "");
			
			return rootView;
		}
	}

	public static class AboutFragment extends Fragment {
		final LatLng Monterey = new LatLng(36.6003, 121.8936);
		private GoogleMap map;
		String MainSiteUrl = "http://www.sustainablemontereycounty.org/monterey-green-action.html";
		
		public AboutFragment() 
		{
			
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			try {
				final View rootView =  inflater.inflate(R.layout.aboutuslayout, container, false);
			
				//map = (GoogleMap)((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
				//map = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
				
				Button donateButton = (Button)rootView.findViewById(R.id.button1);
				donateButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						Toast.makeText(rootView.getContext(), "Sorry, there is no donation function yet.", Toast.LENGTH_SHORT).show();
					}
				});
				
				Button siteButton = (Button)rootView.findViewById(R.id.Button01);
				siteButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(MainSiteUrl));
						startActivity(intent);
					}
				});
				
				return rootView;
			} catch(InflateException e) {
				Log.e("mapstuff", "Error, Will Robinson: " + e);
			}
			Log.w("mapid", R.id.mappy + "");
			
			return null;
		}
	}
	
	public class MarqueeChangeTask extends AsyncTask {
		long startTime = 0;
		long nextTime = 2*(60*1000);
		Calendar c;
		@Override
		protected Object doInBackground(Object... arg0) {
			if(c.getTimeInMillis()-startTime >= nextTime)
			{
				RestartTime(c);
				//Change text
			}
			return null;
		}
		
		protected void onPreExecute()
		{
			c = Calendar.getInstance();
			RestartTime(c);
		}
		
		private void RestartTime(Calendar cal)
		{
			startTime = cal.getTimeInMillis();
		}
	}
	
	LatLng[] GetGpsCoordsFromResource(int res)
	{
		try {
			ArrayList<LatLng> coords = new ArrayList<LatLng>();
			InputStream in = getResources().openRawResource(res);
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
