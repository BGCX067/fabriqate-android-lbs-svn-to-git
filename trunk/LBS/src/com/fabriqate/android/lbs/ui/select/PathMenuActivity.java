package com.fabriqate.android.lbs.ui.select;



import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.fabriqate.android.lbs.R;
import com.fabriqate.android.lbs.service.CityImageService;
import com.fabriqate.android.lbs.ui.applist.ApplistActivity;
import com.fabriqate.android.lbs.ui.select.GpsTask.GpsData;
import com.fabriqate.android.lbs.ui.select.IAddressTask.MLocation;
import com.fabriqate.android.lbs.utils.AsynImageLoader;
import com.fabriqate.android.lbs.utils.MissConnectionUtils;
import com.fabriqate.android.lbs.utils.Utility;
import com.fabriqate.android.lbs.utils.AsynImageLoader.ImageCallback;
import com.google.android.maps.GeoPoint;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PathMenuActivity extends Activity {

	private boolean areButtonsShowing;
	private RelativeLayout composerButtonsWrapper;
	private ImageButton bank;
	private ImageButton shopping;
	private ImageButton music;
	private ImageButton eat;
	private ImageButton traffic;
	private ImageButton movie;
	private ImageButton hospital;
	private RelativeLayout click_me;
	private RelativeLayout composerButtonsShowHideButton;
	private TextView time;
	private String amPmValues;
	private TextView address;
	private ImageView weather;
	
	private RelativeLayout rlContainer;
	
	private String currentCity="guangzhou";//default "guangzhou"
	private String currentWeather="sunny";// default 
	
	CityImageService cityImageService;
	AsynImageLoader asynImageLoader;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖�?		 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select);
		findViews();
		
		 cityImageService=new CityImageService();
		 asynImageLoader=new AsynImageLoader(this);
		
		//获取系统当前时间
		  SimpleDateFormat formatter = new SimpleDateFormat (" HH:mm");//hh12小时  
	       Date curDate = new Date(System.currentTimeMillis());//获取当前时间    HH24小时  
	       String str = formatter.format(curDate);  
	       ContentResolver cv = getBaseContext().getContentResolver();   
	        String strTimeFormat = android.provider.Settings.System.getString(cv, android.provider.Settings.System.TIME_12_24);
	        if(strTimeFormat!=null && strTimeFormat.equals("24"))
	        {
	        Log.i("Debug","24小时制"+ amPmValues);
	        time.setText(str);
	        }
	        else
	        {
	        String amPmValues;   
	        Calendar c = Calendar.getInstance();  
	        if(c.get(Calendar.AM_PM) == 0)
	        {   
	        amPmValues = "AM";
	        }
	        else
	        {   
	        amPmValues = "PM";   
	        }   
	        Log.i("Debug","12小时制现在是：" + amPmValues);
	        time.setText(str+amPmValues);
	        }
	        
		
		MyAnimations.initOffset(PathMenuActivity.this);

		setListener();

		GpsTask gpstask = new GpsTask(this,
				new GpsTaskCallBack() {

					@Override
					public void gpsConnectedTimeOut() {
					address.setText("GPS fail");
					}

					@Override
					public void gpsConnected(GpsData gpsdata) {
						do_gps(gpsdata);
					}

				}, 5000);
		gpstask.execute();

	} 

	
	private void do_gps(final GpsData gpsdata) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				MLocation location = null;
				try {
					Log.i("do_gpspost", "经纬度：" + gpsdata.getLatitude() + "----" + gpsdata.getLongitude());
					location = new AddressTask(PathMenuActivity.this,
							AddressTask.DO_GPS).doGpsPost(gpsdata.getLatitude(),
									gpsdata.getLongitude());

					
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(location == null)
					return "Can't find the position info";
				else
				{
					currentCity=location.City;
					new CityImageTask().execute(currentCity);
					new CityWeatherTask().execute(currentCity);
			
				}
				
				return location.toString();
			}

			@Override
			protected void onPreExecute() {
				//dialog.show();
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(String result) {
				address.setText(result);
				//dialog.dismiss();
				super.onPostExecute(result);
			}
			
		}.execute();
	}


	private void findViews() {
		rlContainer=(RelativeLayout)findViewById(R.id.rlContainer);
		
		composerButtonsWrapper = (RelativeLayout) findViewById(R.id.composer_buttons_wrapper);
		composerButtonsShowHideButton = (RelativeLayout) findViewById(R.id.composer_buttons_show_hide_button);
		address=(TextView)findViewById(R.id.address);
		time = (TextView) findViewById(R.id.time);
		
		bank = (ImageButton) findViewById(R.id.bank);
		shopping = (ImageButton) findViewById(R.id.shopping);
		music = (ImageButton) findViewById(R.id.music);
		eat = (ImageButton) findViewById(R.id.eat);
		traffic = (ImageButton) findViewById(R.id.traffic);
		movie = (ImageButton) findViewById(R.id.movie);
		hospital = (ImageButton) findViewById(R.id.hospital);
		click_me = (RelativeLayout) findViewById(R.id.click_me);
		weather=(ImageView)findViewById(R.id.weather);
	}

	private void setListener() {
		composerButtonsShowHideButton.setOnClickListener(hideBtnListener);
		bank.setOnClickListener(bankBtnListener);
		shopping.setOnClickListener(shoppingBtnListener);
		music.setOnClickListener(musicBtnListener);
		eat.setOnClickListener(eatBtnListener);
		traffic.setOnClickListener(trafficBtnListener);
		movie.setOnClickListener(movieBtnListener);
		hospital.setOnClickListener(hospitalBtnListener);
	
	}
	private View.OnClickListener hideBtnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!areButtonsShowing) {
				// 图标的动�?				
				MyAnimations.startAnimationsIn(composerButtonsWrapper, 300);
				click_me.setVisibility(View.GONE);
			} else {
				// 图标的动�?				
				MyAnimations.startAnimationsOut(composerButtonsWrapper, 300);
				click_me.setVisibility(View.VISIBLE);
			}
			areButtonsShowing = !areButtonsShowing;
		
		}
	};
	private View.OnClickListener bankBtnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			//Toast.makeText(getApplicationContext(),"This is bank",Toast.LENGTH_SHORT).show();
			toApplistActivity("bank");
		}
	};
	private View.OnClickListener shoppingBtnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			//Toast.makeText(getApplicationContext(),"This is shoppingBtnListener",Toast.LENGTH_SHORT).show();
			toApplistActivity("shopping");
			
		}
	};
	private View.OnClickListener musicBtnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			//Toast.makeText(getApplicationContext(),"This is musicBtnListener",Toast.LENGTH_SHORT).show();
			toApplistActivity("music");
			
		}
	};
	private View.OnClickListener eatBtnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			//Toast.makeText(getApplicationContext(),"This is eatBtnListener",Toast.LENGTH_SHORT).show();
			toApplistActivity("eat");
		}
	};
	private View.OnClickListener trafficBtnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			//Toast.makeText(getApplicationContext(),"This is trafficBtnListener",Toast.LENGTH_SHORT).show();
			toApplistActivity("traffic");
		}
	};
	private View.OnClickListener movieBtnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			//Toast.makeText(getApplicationContext(),"This is movieBtnListener",Toast.LENGTH_SHORT).show();
			toApplistActivity("movie");
		}
	};
	private View.OnClickListener hospitalBtnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			//Toast.makeText(getApplicationContext(),"This is hospitalBtnListener",Toast.LENGTH_SHORT).show();
			toApplistActivity("hospital");
		}
	};

	private class CityImageTask extends AsyncTask<String, Void, String>
	{

		@Override
		protected void onPostExecute(String result) {
			Drawable drawable=asynImageLoader.loadDrawable(result, new ImageCallback() {
				@Override
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					if(imageDrawable!=null)
					{
							rlContainer.setBackgroundDrawable(imageDrawable);
							Log.v("Sean", " setBackgroundDrawable ");
					}
				}
			});
			if(drawable!=null)
			{
				rlContainer.setBackgroundDrawable(drawable);
			}
			super.onPostExecute(result);
		}

		@Override
		protected String doInBackground(String... params) {
			
			String cityImageUrl =cityImageService.getCityImageUrl(params[0]);

			return cityImageUrl;
		}

	}
	
	private class CityWeatherTask extends AsyncTask<String, Void, String>
	{
		@Override
		protected void onPostExecute(String result) {

			Log.v("sean", "weather="+result);
			if(result==null)
			{
				return;
			}

		   String weatherString= Utility.convertWeather(result);
		   if(weatherString.equals("sunny"))
		   {
				weather.setImageResource(R.drawable.sunny);
		   }
		   else if(weatherString.equals("rain"))
		   {
			   weather.setImageResource(R.drawable.rain);
		   }
		   
		   else if(weatherString.equals("snow"))
		   {
			   weather.setImageResource(R.drawable.snow);
		   }
		   super.onPostExecute(result);
		}

		@Override
		protected String doInBackground(String... params) {
			
			currentWeather =Utility.getWeather(params[0]);

			return currentWeather;
		}

	}
	
	private void toApplistActivity(String cat)
	{
		SimpleDateFormat formatter = new SimpleDateFormat (" HH:mm");//hh12小时  
	    Date curDate = new Date(System.currentTimeMillis());//获取当前时间    HH24小时  
	    String str = formatter.format(curDate);  
	    
		Intent intent=new Intent();
        intent.setClass(PathMenuActivity.this,ApplistActivity.class);
		intent.putExtra("time", str);
		intent.putExtra("weather", currentWeather);
		intent.putExtra("position", currentCity);
		intent.putExtra("cat", cat);

		startActivity(intent);
	}
}