package com.fabriqate.android.lbs;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.fabriqate.android.lbs.service.AppService;
import com.fabriqate.android.lbs.service.MissConnectionService;
import com.fabriqate.android.lbs.ui.go.ColClockAdjustActivity;
import com.fabriqate.android.lbs.ui.login.C2DMessaging;
import com.fabriqate.android.lbs.ui.login.DialogError;
import com.fabriqate.android.lbs.ui.login.Facebook;
import com.fabriqate.android.lbs.ui.login.FacebookError;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private  final String TAG="LoginActivity";
	//For facebook
	private static final String FACEBOOK_APP_ID = "223893791058699";
	private Facebook facebook;
	private SharedPreferences mPrefs;
	private boolean isLogin = false;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// ǿ��Ϊ����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        Log.d(TAG, "onCreate()");
    	super.onResume();
    /*	ImageButton button = (ImageButton) findViewById(android.R.id.button1);
    	mPrefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
    	handleLogin();
    	button.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					isLogin = true;
					handleLogin();
				}
				return true;
			}
		});*/
    }
    
    @Override
    protected void onResume() {
    	Log.d(TAG, "onResume()");
    	super.onResume();
    	ImageButton button = (ImageButton) findViewById(android.R.id.button1);
    	mPrefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
    	handleLogin();
    	button.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					isLogin = true;
					handleLogin();
				}
				return true;
			}
		});
    }    
    
	private void handleLogin(){
    	Log.d(TAG, "handleLogin()");
		facebook = new Facebook(FACEBOOK_APP_ID);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
		if (facebook.isSessionValid()) {
			if(access_token != null && expires != 0){
				Intent i = new Intent(LoginActivity.this,ColClockAdjustActivity.class);
				startActivity(i);
				Intent intent = new Intent(LoginActivity.this, MissConnectionService.class);
			 	startService(intent);
			 	finish();	
			}
		} else {
			if(isLogin){
				facebook.authorize(this, new String[]{"publish_stream", "read_stream", "offline_access"},200,
				        new Facebook.DialogListener() {
							@Override
							public void onComplete(Bundle values) {
			                    SharedPreferences.Editor editor = mPrefs.edit();
			                    editor.putString("access_token", facebook.getAccessToken());
			                    editor.putLong("access_expires", facebook.getAccessExpires());
			                    editor.commit();
								Intent i = new Intent(LoginActivity.this,ColClockAdjustActivity.class);
								startActivity(i);
								Intent intent = new Intent(LoginActivity.this, MissConnectionService.class);
							 	startService(intent);
							 	finish();
							 	isLogin = false;
							}
							@Override
							public void onFacebookError(FacebookError e) {
						    	Log.d(TAG, "onFacebookError"+" isLogin="+isLogin);
								Toast.makeText(LoginActivity.this,"Facebook login error", Toast.LENGTH_SHORT).show();
								isLogin=false;
							}

							@Override
							public void onError(DialogError e) {
								Toast.makeText(LoginActivity.this,"Facebook login error", Toast.LENGTH_SHORT).show();
								Log.d(TAG, "onError"+" isLogin="+isLogin);
								isLogin=false;
							}
							@Override
							public void onCancel() {
								Toast.makeText(LoginActivity.this,"Facebook login cancel", Toast.LENGTH_SHORT).show();	
								Log.d(TAG, "onCancel"+" isLogin="+isLogin);
								isLogin=false;
							}
						});				
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  Log.v(TAG, "requestCode="+requestCode);
	  if(requestCode == 200){

		  facebook.authorizeCallback(requestCode, resultCode, data);
		  
	/*	  facebook.authorize(
				    LoginActivity.this,
				    new String[] {"publish_stream", "read_stream", "offline_access"},
				    Facebook.FORCE_DIALOG_AUTH,
				    null
				);*/
	  }
	 /* if(requestCode == Facebook.FORCE_DIALOG_AUTH){
		  facebook.authorizeCallback(requestCode, resultCode, data);
	  }
	  */
	  
	  
	}
    
	
    @Override
    protected void onStart() {
    	super.onStart();
    	Log.v(TAG, "onStart");
    }
    
}
