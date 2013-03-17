package com.ideoma.black_thorn;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity implements OnMyLocationChangeListener{

	//LatLng[] montereyArrayCoords = new LatLng[]{new LatLng(36.654360,-121.800420),new LatLng(36.654244,-121.799272),
	//		new LatLng(36.654600,-121.799846),new LatLng(36.654360,-121.800420)};
	LatLng[] montereyArrayCoords;
	double latRadiusStart = 0.0005;
	MediaPlayer myPlayer;
	
	public final static String baseResPath = "android.resource://com.ideoma.black_thorn/";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		myPlayer = new MediaPlayer();
		myPlayer.setLooping(false);
		
		LatLng monterey = new LatLng(36.654244, -121.799272);
		
		GoogleMap map;
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		/*
		map.addMarker(new MarkerOptions()
        .position(new LatLng(36.654244, -121.799272))
        .title("Hello world"));
        */
		map.setMyLocationEnabled(true);
		/*
		LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener mlocListener = new MyLocationListener();
		mlocManager.*/
		
		montereyArrayCoords = GetGpsCoordsFromResource(R.raw.csumb_gps_coordinates);
		
		for(int i = 0; i < montereyArrayCoords.length; i++)
		{
			map.addMarker(new MarkerOptions().position(montereyArrayCoords[i]).title(i+" pos"));
		}
		
		Polyline line = map.addPolyline(new PolylineOptions()
	     	.add(montereyArrayCoords)
	     	.width(5)
	     	.color(Color.RED)
	     );
	}

	@Override
	public void onMyLocationChange(Location loc) {
		Log.i("tittysprinkles","myLocationChanged! " + loc.getLatitude() + " " + loc.getLongitude());
		for(int i = 0; i < montereyArrayCoords.length; i++)
		{
			LatLng ll = montereyArrayCoords[i];
			double dx = Math.abs(loc.getLatitude() - ll.latitude);
			double dy = Math.abs(loc.getLongitude() - ll.longitude);
			double d = Math.sqrt(dx*dx - dy*dy);
			if(d < latRadiusStart)
			{
				Log.i("tittysprinkles","Radius activated! " + d);
				try {
					myPlayer.reset();
					myPlayer.setDataSource(baseResPath + R.raw.convo1);
					Log.i("tittysprinkles","Data source set: " + baseResPath + R.raw.convo1);
					myPlayer.prepare();
					myPlayer.start();
				} catch (Exception e) {
					Log.e("tittysprinkles","Error setting player's data source! " + e.getMessage());
				}
			}
		}
	} 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
		
			Log.e("titty_sprinkles","In: " + in.available());
			
			Document doc = parser.ParseXMLToDoc(in);
			NodeList nList = parser.ParseDocByTagName(doc, "point");
			for(int i = 0; i < nList.getLength(); i++)
			{
				double lat = 0, lng = 0;
				Element ele = (Element) nList.item(i);
				lat = Double.parseDouble(parser.GetTextValueByTagName(ele, "lat"));
				lng = Double.parseDouble(parser.GetTextValueByTagName(ele, "lng"));
				Log.e("titty_sprinkles","Lat: " + lat + ", Lng: " + lng);
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