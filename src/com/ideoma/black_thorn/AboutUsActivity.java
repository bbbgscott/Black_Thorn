package com.ideoma.black_thorn;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AboutUsActivity extends Activity
{
	String mainUrl = "http://www.sustainablemontereycounty.org/monterey-green-action.html";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutuslayout);
		
		Button siteButt = (Button)findViewById(R.id.SiteButton);
		Button donateButt = (Button)findViewById(R.id.DonateButton);
		siteButt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(mainUrl));
				startActivity(intent);
			}
		});
		donateButt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "No donate function yet :'( sorrry", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
