package com.fabriqate.android.lbs.service;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.client.ClientProtocolException;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.fabriqate.android.lbs.C2DMReceiver;
import com.fabriqate.android.lbs.ui.login.C2DMessaging;
import com.fabriqate.android.lbs.ui.select.AddressTask;
import com.fabriqate.android.lbs.ui.select.GoogleWeatherHandler;
import com.fabriqate.android.lbs.ui.select.PathMenuActivity;
import com.fabriqate.android.lbs.ui.select.IAddressTask.MLocation;
import com.fabriqate.android.lbs.utils.LBSConfig;
import com.fabriqate.android.lbs.utils.MissConnectionUtils;
import com.fabriqate.android.lbs.utils.Utility;
import com.google.android.maps.GeoPoint;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class MissConnectionService extends Service{
	private static final String PACKAGE_NAME = "com.fabriqate.android.lbs";
	private static final String TAG = "MissConnectionService";
	private static final int LOCATION = 0;
	private static final int POSITION = 1;
	private static final long DELAY = 1000;
	private static final long MISSCONNETION_PERIOD = 60*60* 1000;
	
	private static final long NEWFINDING_PERIOD = 5*60*1000;
	
	private boolean serviceIsRunning = true;
	private Location location;
	private Editor editor;
	
	private static final String LONGITUDE = "longitude";
	private static final String LATITUDE = "latitude";
	private static final String COUNTRYNAME = "country_name";
	private static final String LOCALITY = "locality";
	private Timer locationTimer; 	//miss connection
	private Timer positionTimer; //new finding
	private NewFindingBroadcastReceiver  newFindingBroadcast=new NewFindingBroadcastReceiver();
	
	private static final int RESET = 2;
	
	private LocationTimerTask mLocationTimerTask;
	private PositionTimerTask mPositionTimerTask;
	
	private int pushTotalCount;//总共要推送的次数
	private int pushAlreadyCount;//已经推送过的次数
	
	private String currentCity="guangzhou";//default "guangzhou"
	private String currentWeather="sunny";// default 
	
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			if(what == LOCATION){
			
				try {
					
					editor.putString(LONGITUDE, "113.31042212499999");
					editor.putString(LATITUDE, "23.122357325");
					editor.putString(COUNTRYNAME, "China");
					editor.putString(LOCALITY, "Guangzhou");
					editor.commit();
					
				/*	location = findCurrentLocation();
					if(location!=null)
					{
						GeoPoint gp = MissConnectionUtils.getGeoByLocation(location);
						Address mAddress = MissConnectionUtils.getAddressbyGeoPoint(getApplicationContext(),gp); 
						editor.putString(LONGITUDE, String.valueOf(location.getLongitude()));
						editor.putString(LATITUDE, String.valueOf(location.getLatitude()));
						editor.putString(COUNTRYNAME, mAddress.getCountryName());
						editor.putString(LOCALITY, mAddress.getLocality());
						
						editor.commit();
					}*/
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					SharedPreferences sharedPreferences = getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE);
					Log.d(TAG, "Longitude: " + sharedPreferences.getString(LONGITUDE, "") + " Latitude: " + sharedPreferences.getString(LATITUDE, ""));
					Log.d(TAG, "Address: " + sharedPreferences.getString(COUNTRYNAME, "")+"," + sharedPreferences.getString(LOCALITY, ""));
				}				
			}
			//new finding
			else if(what == POSITION){
				//findCurrentLocation();//测试调用GPS，#######################################，因为代码实际是写死地址的
				
				location = findCurrentLocation();
			    String	city=getCurrentCity(location);

			    if(city!=null  && !city.equals(""))
			    {
			    	currentCity=city;
					String weather=Utility.getWeather(city);
					if(weather!=null && !weather.equals(""))
					{
						currentWeather= Utility.convertWeather(weather) ;
					}
			    }
				Log.d(TAG, "what == POSITION" +" currentCity="+currentCity+" currentWeather="+currentWeather);
				
				
				AppService appService = new AppService();
				SharedPreferences mPrefs = getSharedPreferences("com.fabriqate.android.lbs", MODE_PRIVATE);
				String registrationId = mPrefs.getString("registration_id", "");
				if(registrationId.equals("")){
			    	C2DMessaging.register(getApplicationContext(), C2DMReceiver.SENDER_ID);
			    	registrationId = C2DMessaging.getRegistrationId(getApplicationContext());
			    	if(registrationId != null)
			    		mPrefs.edit().putString("registration_id", registrationId).commit();
				}
				Log.d(TAG, "========== registrationId " + registrationId);
				try {
					
					appService.getPushApp(registrationId,currentWeather,currentCity);
					
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else if(what==RESET)
			{
			   int minutes=	msg.getData().getInt("minutes");
			 
			   if(mPositionTimerTask!=null)
			   {
				   mPositionTimerTask.cancel(); 
			   }
			   
			   pushTotalCount= (int) Math.ceil( minutes *(60*1000) / NEWFINDING_PERIOD );
		
			   pushAlreadyCount=0;
			   if(pushTotalCount==0)
			   {
				   pushTotalCount=1;
			   }
			   Log.d(TAG, "pushTotalCount="+pushTotalCount);
			   mPositionTimerTask=new PositionTimerTask();
			   positionTimer.schedule(mPositionTimerTask, NEWFINDING_PERIOD, NEWFINDING_PERIOD);
			}

		}
	};
	@Override
	public void onCreate() {
		serviceIsRunning = true;
		super.onCreate();
		SharedPreferences sharedPreferences = getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE);
		editor = sharedPreferences.edit();
		
		
		locationTimer = new Timer();
		mLocationTimerTask=new LocationTimerTask();
		locationTimer.schedule(mLocationTimerTask, DELAY,MISSCONNETION_PERIOD);
		
		positionTimer = new Timer();
		
		//mPositionTimerTask=new PositionTimerTask();
		//positionTimer.schedule(mPositionTimerTask, DELAY,NEWFINDING_PERIOD);
		
		IntentFilter filter=new IntentFilter(LBSConfig.ACTION_NEWFINDING_TIME);
		registerReceiver(newFindingBroadcast, filter);
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		serviceIsRunning = false;
		if(locationTimer != null){
			locationTimer.cancel();
		}
		
	   unregisterReceiver(newFindingBroadcast);
	}
	
	private Location findCurrentLocation(){
		LocationManager locMan = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		Location location = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		return location;
	}
	private String  getCurrentCity(Location location) {
		String result=null;
		MLocation mlocation = null;
		try {

			mlocation = new AddressTask(MissConnectionService.this,
					AddressTask.DO_GPS).doGpsPost(
					location.getLatitude(), location.getLongitude());

		} catch (Exception e1) {

			Log.e(TAG, "new finding  findCurrentLocation error");
		}
		if(mlocation != null)
		{
			result=mlocation.City;
		}
		return result;
	}

	
	private class PositionTimerTask extends TimerTask {
		@Override
		public void run() {
			Log.d(TAG, "PositionTimerTask run");

			if(pushAlreadyCount<pushTotalCount)
			{
				pushAlreadyCount++;
				handler.sendEmptyMessage(POSITION);
		    }
			else 
			{
				Log.d(TAG, "auto cancel.... ");
				cancel();
			}
			//when this service is close
			if(!serviceIsRunning){
				if(locationTimer != null){
					locationTimer.cancel();
				}
			}				
		}
	}
		
	private class LocationTimerTask extends  TimerTask {
		@Override
		public void run() {
			//Log.d(TAG, "LocationTimerTask run");
			handler.sendEmptyMessage(LOCATION);
			//when this service is close
			if(!serviceIsRunning){
				if(locationTimer != null){
					locationTimer.cancel();
				}
			}
		}
	}
		
	private class NewFindingBroadcastReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			
			if(intent.getAction().equals(LBSConfig.ACTION_NEWFINDING_TIME))
			{
				Log.d(TAG, "get new find broadcast ,minutes="+intent.getIntExtra("minutes",0)+"");
				
				Message msg=handler.obtainMessage(RESET);
				Bundle data=new Bundle();
				data.putInt("minutes", intent.getIntExtra("minutes",0));
				msg.setData(data);
				handler.sendMessage(msg);
			}
			
		}
		
	}
}
