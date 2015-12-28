package com.fabriqate.android.lbs;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TeachActivity extends Activity {
	public static final String PREFS_NAME = "MyPrefsFile";
	 
	        public static final String FIRST_RUN = "first";
	 
	        private boolean first;
	        private ImageView myImageView;
	
	 
	        /** Called when the activity is first created. */
	 
	        @Override
	 
	        public void onCreate(Bundle savedInstanceState) {
	 
	                super.onCreate(savedInstanceState);
	            	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// ǿ��Ϊ����
	        		requestWindowFeature(Window.FEATURE_NO_TITLE);
	 
	                setContentView(R.layout.teach);
	             
	                // Restore preferences
	 
	                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	 
	                first = settings.getBoolean(FIRST_RUN, true);
	 
	                if (first) {
	                	

	                        SharedPreferences.Editor editor = settings.edit();
		                	 editor.putBoolean(FIRST_RUN, false);
		                	 editor.commit();
		                	 Intent intent = new Intent(TeachActivity.this, TestViewPager.class);
		 					startActivity(intent); 
		 					this.finish();
	 
                } else {
	 

                       Intent intent = new Intent(TeachActivity.this, LoginActivity.class);
	 					startActivity(intent); 
	 					this.finish();
                     
                }
	        }
		 
	}
