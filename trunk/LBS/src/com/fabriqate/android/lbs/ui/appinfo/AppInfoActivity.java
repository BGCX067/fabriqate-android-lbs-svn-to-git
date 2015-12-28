package com.fabriqate.android.lbs.ui.appinfo;

import com.fabriqate.android.lbs.R;
import com.fabriqate.android.lbs.bean.AppInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AppInfoActivity extends Activity {

	
	
	private final String TAG="AppInfoActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ac_appinfo);
		Intent intent=getIntent();
	    AppInfo appInfo= (AppInfo) intent.getSerializableExtra("AppInfo");
	    
	    Log.v(TAG, appInfo.toString());
		
	}

	
	
}
