package com.ideoma.black_thorn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class StartPageActivity extends Activity
{
	
	private final int OPEN_SECURITY_SETTINGS = 27;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainlayout);
		
		final LocationManager locmanager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		if(!locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
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
		
		Button startButton = (Button)findViewById(R.id.startButt);
		Button aboutUsButton = (Button)findViewById(R.id.aboutButt);
		
		startButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StartPageActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
		
		aboutUsButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StartPageActivity.this,AboutUsActivity.class);
				startActivity(intent);
			}
		});
	}
	
	public void EnableGPS()
	{
		Intent intent = new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS);
		startActivityForResult(intent,OPEN_SECURITY_SETTINGS);
	}
}
