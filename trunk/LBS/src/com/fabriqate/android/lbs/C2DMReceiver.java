package com.fabriqate.android.lbs;

import java.io.IOException;

import org.json.JSONException;

import com.fabriqate.android.lbs.R;
import com.fabriqate.android.lbs.bean.AppInfo;
import com.fabriqate.android.lbs.service.AppService;
import com.fabriqate.android.lbs.ui.appinfo.DetailsActivity;
import com.fabriqate.android.lbs.ui.appinfo.GalleryActivity;
import com.fabriqate.android.lbs.ui.login.C2DMBaseReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class C2DMReceiver extends C2DMBaseReceiver{
	
	private static final String TAG = "C2DMReceiver";
	private static final String CONGRATULATIONS = "Your entry has been submitted successfully";
	public static final String SENDER_ID = "Gumball.Android@fabriqate.com";//password:6swe1a7dsfvg
	public static AppInfo appInfo;
	//
	public C2DMReceiver() {
		super(SENDER_ID);
	}
	public C2DMReceiver(String senderId) {
		super(senderId);
	}
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.v(TAG, "C2DMReceiver message");
		Bundle extras = intent.getExtras();
		if(extras!=null){
			String msg = (String)extras.get("msg");
			String id = (String)extras.get("id");
			SharedPreferences mPrefs = getSharedPreferences("com.fabriqate.android.lbs", MODE_PRIVATE);
			mPrefs.edit().putString("lbs_item_id", id).commit();
			Log.v(TAG, "The received msg = " + msg);
			Log.v(TAG, "The received id = " + id);
			AppService appService = new AppService();
			appInfo = null;
			try {
				appInfo = appService.getPositionAppById(id);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(appInfo == null){
				return;
			}
			Log.v(TAG, "appInfo = " + appInfo.toString());
			NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			Notification notification = new Notification(R.drawable.ic_launcher, msg, System.currentTimeMillis());
			Intent i = new Intent(this, GalleryActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.putExtra("notification", true);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, 0);
			notification.setLatestEventInfo(this, getString(R.string.app_name), msg, contentIntent);
			notificationManager.notify(0, notification);
			
		}
	}

	@Override
	public void onError(Context context, String errorId) {
		Log.v(TAG, "C2DMReceiver error");
	}
	
	@Override
	public void onRegistered(Context context, String registrationId) throws IOException {
		super.onRegistered(context, registrationId);
		Log.v(TAG, "C2DMReceiver Register");
	}
	@Override
	public void onUnregistered(Context context) {
		super.onUnregistered(context);
		Log.v(TAG, "C2DMReceiver UnRegister");
	}	
}
