package com.fabriqate.android.lbs.ui.applist;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.fabriqate.android.lbs.R;
import com.fabriqate.android.lbs.bean.AppInfo;
import com.fabriqate.android.lbs.service.AppService;
import com.fabriqate.android.lbs.ui.appinfo.AppInfoActivity;
import com.fabriqate.android.lbs.ui.appinfo.GalleryActivity;
import com.fabriqate.android.lbs.utils.AsynImageLoader;
import com.fabriqate.android.lbs.utils.AsynImageLoader.ImageCallback;
import com.fabriqate.android.lbs.utils.AsynImageLoader.LogoImageCallback;
import com.fabriqate.android.lbs.utils.LBSConfig;
import com.fabriqate.android.lbs.utils.Utility;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ApplistActivity extends Activity
{
    private final String TAG="ApplistActivity";
    private final String TYPE_FREE="free";
    private final String TYPE_PAID="paid";
    private final String TYPE_ALL="all";
    
    private final int MSG_WHAT_POSITION=1;
    private final int MSG_WHAT_TIME=2;
    private final int MSG_WHAT_WEATHER=3;
    
    private final String CATEGORY_POSITION="position";
    private final String CATEGORY_TIME="time";
    private final String CATEGORY_WEATHER="weather";
    
	private ImageButton ibtnFree;
	private ImageButton ibtnPaid;
	private ImageButton ibtnAll;
	private ListView lvCategory1;
	private ListView lvCategory2;
	private ListView lvCategory3;
	private TextView tvPosition;
	private TextView tvCurrentTime;
	private TextView tvWeather;
	
	private AsynImageLoader asynImageLoader;
	
	private HashMap<String, HashMap<String, ArrayList<AppInfo>>> typeMap;
	private HashMap<String, ArrayList<AppInfo>> catagroyMap;
	
	/* ArrayList<AppInfo> postionAppinfos;
	 ArrayList<AppInfo> timeAppinfos;
	  ArrayList<AppInfo> weatherAppinfos;*/
	  
	private AppAdapter positionAdapter;
	private  AppAdapter timeAdapter;
	private AppAdapter weatherAdapter;
	  
	  
	  String time;
	  String weather;
	  String position;
	  String cat;
	  
	  private AppService appService;
	  private  ArrayList<AppInfo> allApps;
	  private Boolean isReady=false;//��ֹ���all��free��paid����������
	 	  
	private Handler handler=new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			default:
				break;
			}
		}
	};

	
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {  
		   int width = bitmap.getWidth();  
		   int height = bitmap.getHeight();  
		   Matrix matrix = new Matrix();  
		   float scaleWidht = ((float) w / width);  
		   float scaleHeight = ((float) h / height);  
		   matrix.postScale(scaleWidht, scaleHeight);  
		   Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);  
		   return newbmp;  
		}  
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.applist_activity);
        findView();
        asynImageLoader = new AsynImageLoader(this);
        appService=new AppService();
        typeMap=new HashMap<String, HashMap<String,ArrayList<AppInfo>>>();
        catagroyMap=new HashMap<String, ArrayList<AppInfo>>();
//        
        OnClickListener listener =new ImageButtonClickListener();
		ibtnFree.setOnClickListener(listener);
		ibtnPaid.setOnClickListener(listener);
		ibtnAll.setOnClickListener(listener);
		
		setImageButtonBackgroud();
		ibtnFree.setBackgroundResource(R.drawable.ac_applist_btnfree_press);
	  
		
		 Intent intent=getIntent();
		 
		  time= intent.getStringExtra("time");
		  weather= intent.getStringExtra("weather");
		  position= intent.getStringExtra("position");
		  cat= intent.getStringExtra("cat");
		 
		 
		
		allApps=new ArrayList<AppInfo>();
		//setAdapterFromHashmap(TYPE_FREE);
	    new GetAppsTask(this).execute(TYPE_FREE);
    }

	private void setFreeData(ArrayList<AppInfo> allApps) {
		
		if(typeMap.get(TYPE_FREE)==null)
		{
			 HashMap<String, ArrayList<AppInfo>> catagroyFreeMap;
			 
			 if(typeMap.get(TYPE_FREE)!=null)
			 {
				 catagroyFreeMap=typeMap.get(TYPE_FREE);
			 }
			 else
			 {
				 catagroyFreeMap =new HashMap<String, ArrayList<AppInfo>>();
			 }
			 
		/*	 ArrayList<AppInfo> postionAppinfos_f=new ArrayList<AppInfo>() ;
			 ArrayList<AppInfo> timeAppinfos_f=new ArrayList<AppInfo>();
			 ArrayList<AppInfo> weatherAppinfos_f=new ArrayList<AppInfo>();*/
			 
			 ArrayList<AppInfo> postionAppinfos_f=appService.getFreePositionApps(allApps);
			 ArrayList<AppInfo> timeAppinfos_f=appService.getFreeTimeApps(allApps);
			 ArrayList<AppInfo> weatherAppinfos_f=appService.getFreeWeatherApps(allApps);

			 typeMap.put(TYPE_FREE, catagroyFreeMap);
			 catagroyFreeMap.put(CATEGORY_POSITION, postionAppinfos_f);
			 catagroyFreeMap.put(CATEGORY_TIME, timeAppinfos_f);
			 catagroyFreeMap.put(CATEGORY_WEATHER, weatherAppinfos_f);

		}
	}
 
	private void setPaidData(ArrayList<AppInfo> allApps) {
		if(typeMap.get(TYPE_PAID)==null)
		{
			 HashMap<String, ArrayList<AppInfo>> catagroyFreeMap=new HashMap<String, ArrayList<AppInfo>>();
		/*	 ArrayList<AppInfo> postionAppinfos_p=new ArrayList<AppInfo>();
			 ArrayList<AppInfo> timeAppinfos_p=new ArrayList<AppInfo>();
			  ArrayList<AppInfo> weatherAppinfos_p=new ArrayList<AppInfo>();*/

				 ArrayList<AppInfo> postionAppinfos_p=appService.getPaidPositionApps(allApps);
				 ArrayList<AppInfo> timeAppinfos_p=appService.getPaidTimeApps(allApps);
				 ArrayList<AppInfo> weatherAppinfos_p=appService.getPaidWeatherApps(allApps);
			  
			 typeMap.put(TYPE_PAID, catagroyFreeMap);
			 catagroyFreeMap.put(CATEGORY_POSITION, postionAppinfos_p);
			 catagroyFreeMap.put(CATEGORY_TIME, timeAppinfos_p);
			 catagroyFreeMap.put(CATEGORY_WEATHER, weatherAppinfos_p);

	     /*   for(int i=0;i<postionAppinfos_p.size();i++)
	        {
	        	getEachImage(postionAppinfos_p.get(i),TYPE_PAID,MSG_WHAT_POSITION);
	        }
	        for(int i=0;i<timeAppinfos_p.size();i++)
	        {
	        	getEachImage(timeAppinfos_p.get(i),TYPE_PAID,MSG_WHAT_TIME);
	        }
	        for(int i=0;i<weatherAppinfos_p.size();i++)
	        {
	        	getEachImage(weatherAppinfos_p.get(i),TYPE_PAID,MSG_WHAT_WEATHER);
	        }*/
		}
	}
	
	private void setAllData(ArrayList<AppInfo> allApps) {
		if(typeMap.get(TYPE_ALL)==null)
		{
			HashMap<String, ArrayList<AppInfo>> catagroyFreeMap=new HashMap<String, ArrayList<AppInfo>>();
			 ArrayList<AppInfo> postionAppinfos_a=appService.getPositionApps(allApps);
			 ArrayList<AppInfo> timeAppinfos_a=appService.getTimeApps(allApps);
			  ArrayList<AppInfo> weatherAppinfos_a=appService.getWeatherApps(allApps);
			  
			 typeMap.put(TYPE_ALL, catagroyFreeMap);
			 catagroyFreeMap.put(CATEGORY_POSITION, postionAppinfos_a);
			 catagroyFreeMap.put(CATEGORY_TIME, timeAppinfos_a);
			 catagroyFreeMap.put(CATEGORY_WEATHER, weatherAppinfos_a);
	    
	        /*for(int i=0;i<postionAppinfos_a.size();i++)
	        {
	        	getEachImage(postionAppinfos_a.get(i),TYPE_ALL,MSG_WHAT_POSITION);
	        }
	        for(int i=0;i<timeAppinfos_a.size();i++)
	        {
	        	getEachImage(timeAppinfos_a.get(i),TYPE_ALL,MSG_WHAT_TIME);
	        }
	        for(int i=0;i<weatherAppinfos_a.size();i++)
	        {
	        	getEachImage(weatherAppinfos_a.get(i),TYPE_ALL,MSG_WHAT_WEATHER);
	        }*/
		}
	}
	
	private void findView() {
		ibtnFree=(ImageButton) findViewById(R.id.ibtnFree);
        ibtnPaid=(ImageButton) findViewById(R.id.ibtnPaid);
        ibtnAll=(ImageButton) findViewById(R.id.ibtnAll);
        lvCategory1=(ListView)findViewById(R.id.lvCategory1);
        lvCategory2=(ListView)findViewById(R.id.lvCategory2);
        lvCategory3=(ListView)findViewById(R.id.lvCategory3);
        tvPosition=(TextView)findViewById(R.id.tvPosition);
        tvCurrentTime=(TextView)findViewById(R.id.tvCurrentTime);
        tvWeather=(TextView)findViewById(R.id.tvWeather);		
	}
	
	private void setImageButtonBackgroud()
	{
		  ibtnFree.setBackgroundResource(R.drawable.ac_applist_btnfree);
		  ibtnPaid.setBackgroundResource(R.drawable.ac_applist_btnpaid);
		  ibtnAll.setBackgroundResource(R.drawable.ac_applist_btnall);
	}
	
	private class ImageButtonClickListener implements OnClickListener
	{
		public void onClick(View v) {
			setImageButtonBackgroud();
			switch (v.getId()) {
	
			case R.id.ibtnFree:
				//setAdapterFromHashmap(TYPE_FREE);
				new GetAppsTask(ApplistActivity.this).execute(TYPE_FREE);
                 ibtnFree.setBackgroundResource(R.drawable.ac_applist_btnfree_press);
                 
				break;
			case R.id.ibtnPaid:
			//	setAdapterFromHashmap(TYPE_PAID);
				new GetAppsTask(ApplistActivity.this).execute(TYPE_PAID);
				ibtnPaid.setBackgroundResource(R.drawable.ac_applist_btnpaid_press); 
				break;
			case R.id.ibtnAll:
				//setAdapterFromHashmap(TYPE_ALL);
				new GetAppsTask(ApplistActivity.this).execute(TYPE_ALL);
				ibtnAll.setBackgroundResource(R.drawable.ac_applist_btnall_press);
				break;
			default:
				break;
			}
		}
	}
	
	
	private class AppAdapter  extends BaseAdapter
	{
        private Context context;
        private ArrayList<AppInfo> appInfos;
        private int size;
        
        private ImageView imageView_item1;
        private TextView tvAppName1;
        private TextView tvAppDetail1;
        
        private ImageView imageView_item2;
        private TextView tvAppName2;
        private TextView tvAppDetail2;
        
        private ImageView imageView_item3;
        private TextView tvAppName3;
        private TextView tvAppDetail3;
        
        private LinearLayout llItem1;
        private LinearLayout llItem2;
        private LinearLayout llItem3;
        
        private RelativeLayout logoBg_item1;
        private RelativeLayout logoBg_item2;
        private RelativeLayout logoBg_item3;
 
		public AppAdapter (Context context,ArrayList<AppInfo> appInfos)
		{
			this.context=context;
			this.appInfos=appInfos;
			 size=appInfos.size();
			
		}
		
		public int getCount() {
			int result= 0;
		//	int size=this.appInfos.size();
			if(size>0 && size <4)
			{
				result=1;
			}
			else if(size>3)
			{
			   result=(int) Math.ceil( 1.0 * size / 3 );
			}
			return result;
		}

		public Object getItem(int position) {
			
			return null;
		}

		public long getItemId(int position) {
			
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			//if(convertView==null)
			{
				convertView=LayoutInflater.from(context).inflate(R.layout.ac_pplist_list_item, null);
				
				llItem1=(LinearLayout)convertView.findViewById(R.id.llItem1);
				llItem2=(LinearLayout)convertView.findViewById(R.id.llItem2);
				llItem3=(LinearLayout)convertView.findViewById(R.id.llItem3);
				
				logoBg_item1=(RelativeLayout)convertView.findViewById(R.id.logoBg_item1);
				logoBg_item2=(RelativeLayout)convertView.findViewById(R.id.logoBg_item2);
				logoBg_item3=(RelativeLayout)convertView.findViewById(R.id.logoBg_item3);
				
				imageView_item1=(ImageView)convertView.findViewById(R.id.imageView_item1);
				tvAppName1=(TextView)convertView.findViewById(R.id.tvAppName1);
				tvAppDetail1=(TextView)convertView.findViewById(R.id.tvAppDetail1);
				
				imageView_item2=(ImageView)convertView.findViewById(R.id.imageView_item2);
				tvAppName2=(TextView)convertView.findViewById(R.id.tvAppName2);
				tvAppDetail2=(TextView)convertView.findViewById(R.id.tvAppDetail2);
				
				imageView_item3=(ImageView)convertView.findViewById(R.id.imageView_item3);
				tvAppName3=(TextView)convertView.findViewById(R.id.tvAppName3);
				tvAppDetail3=(TextView)convertView.findViewById(R.id.tvAppDetail3);
			}
			int index=position*3;
			int sum=0;
			while(index<size)
			{
				sum++;
				final AppInfo info=appInfos.get(index);
				
				if(sum==1)
				{
					logoBg_item1.setBackgroundResource(R.drawable.apk_bg);
				     setLogoImage(info.getLogo(),imageView_item1);
					
					tvAppName1.setText(info.getName());
					tvAppDetail1.setText(info.getCaption());
					
					llItem1.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {

				
							toAppInfoActivity(context,info);
						}
					});
				}
				else if(sum==2)
				{
					logoBg_item2.setBackgroundResource(R.drawable.apk_bg);
					setLogoImage(info.getLogo(),imageView_item2);
					tvAppName2.setText(info.getName());
					tvAppDetail2.setText(info.getCaption());
					llItem2.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {
							// TODO Auto-generated method stub
				
							toAppInfoActivity(context,info);
						}
					});
				}
				else if(sum==3)
				{
					logoBg_item3.setBackgroundResource(R.drawable.apk_bg);
					setLogoImage(info.getLogo(),imageView_item3);
					tvAppName3.setText(info.getName());
					tvAppDetail3.setText(info.getCaption());
					llItem3.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {
						
							toAppInfoActivity(context,info);
						}
					});
				}
				else
				{
					break;
				}
				index++;
			}
			return convertView;
		}

		private void setLogoImage(final String logoUrl,final ImageView imageView ) {
			 Drawable d= asynImageLoader.loadDrawable(logoUrl,imageView,new LogoImageCallback() {
					
					@Override
					public void imageLoaded(Drawable imageDrawable, String imageUrl,
							ImageView imageView) {
					
						if(imageDrawable!=null)
						{
							imageView.setImageDrawable(imageDrawable);
						}
					}
				});
				if(d!=null)
				{
				
					imageView.setImageDrawable(d);
				}

		}
	}

	private class GetAppsTask  extends AsyncTask<String, Void, Void>
	{
	   private ProgressDialog progressDialog;
	   Context context;
	   public GetAppsTask(Context context)
	   {
		   this.context=context;
	   }
		@Override
		protected void onPostExecute(Void result) {
	
			lvCategory1.setAdapter(positionAdapter);
			lvCategory2.setAdapter(timeAdapter);
			lvCategory3.setAdapter(weatherAdapter);		
			
			Utility.setListViewHeightBasedOnChildren(lvCategory1);
			Utility.setListViewHeightBasedOnChildren(lvCategory2);
			Utility.setListViewHeightBasedOnChildren(lvCategory3);
			try
			{
				progressDialog.dismiss();
				progressDialog=null;
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
		    progressDialog=new ProgressDialog(context);
		    progressDialog.setMessage("Loading");
		    progressDialog.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			//AppService appService=new AppService();

			if(!isReady)
			{
				try {
					
					allApps= appService.getAppListByCat(cat,time,weather,position);

					isReady=true;
					
				} 
				catch (IOException e) {
					isReady=false;
					e.printStackTrace();
				}
			}

		    String type=params[0];
			if(type.equals(TYPE_FREE))
			{
			    setFreeData(allApps);  
				HashMap<String, ArrayList<AppInfo>> categoryMap=typeMap.get(TYPE_FREE);			
				positionAdapter=new AppAdapter(context, categoryMap.get(CATEGORY_POSITION));
		        timeAdapter= new AppAdapter(context, categoryMap.get(CATEGORY_TIME));
		        weatherAdapter=  new AppAdapter(context, categoryMap.get(CATEGORY_WEATHER));
			}
			else if(type.equals(TYPE_PAID))
			{
				setPaidData(allApps);
				HashMap<String, ArrayList<AppInfo>> categoryMap=typeMap.get(TYPE_PAID);
				
				positionAdapter=new AppAdapter(context, categoryMap.get(CATEGORY_POSITION));
		        timeAdapter= new AppAdapter(context, categoryMap.get(CATEGORY_TIME));
		        weatherAdapter=  new AppAdapter(context, categoryMap.get(CATEGORY_WEATHER));
			}
			else if(type.equals(TYPE_ALL))
			{
				setAllData(allApps);
				HashMap<String, ArrayList<AppInfo>> categoryMap=typeMap.get(TYPE_ALL);
				positionAdapter=new AppAdapter(context, categoryMap.get(CATEGORY_POSITION));
		        timeAdapter= new AppAdapter(context, categoryMap.get(CATEGORY_TIME));
		        weatherAdapter=  new AppAdapter(context, categoryMap.get(CATEGORY_WEATHER));
			}
			return null;
		}
		
	}

	private void toAppInfoActivity(Context context, AppInfo info) {
		
		ArrayList<AppInfo> infos=new ArrayList<AppInfo>();
		if(info.getType().equals(AppInfo.TYPE_POSITION))
		{
			infos=appService.getPositionApps(allApps);
		}
		else if(info.getType().equals(AppInfo.TYPE_TIME))
		{
			infos=appService.getTimeApps(allApps);
		}
		else if(info.getType().equals(AppInfo.TYPE_WEATHER))
		{
			infos=appService.getWeatherApps(allApps);
		}
		
		Intent intent=new Intent();
		intent.putExtra("AppInfo", info);

        intent.putExtra("AppInfos", infos);
         
		intent.setClass(context,  GalleryActivity.class);
		startActivity(intent);
	}
	
}
