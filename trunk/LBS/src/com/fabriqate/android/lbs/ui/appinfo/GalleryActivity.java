package com.fabriqate.android.lbs.ui.appinfo;

import java.util.ArrayList;

import com.fabriqate.android.lbs.C2DMReceiver;
import com.fabriqate.android.lbs.R;
import com.fabriqate.android.lbs.bean.AppInfo;
import com.fabriqate.android.lbs.utils.AsynImageLoader;
import com.fabriqate.android.lbs.utils.AsynImageLoader.ImageCallback;
import com.fabriqate.android.lbs.utils.AsynImageLoader.LogoImageCallback;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GalleryActivity extends Activity {
	/** 
	Gallery类概述(Gallery)此单词翻译过来为画廊，美术馆

	一种view，以水平列表的方式显示在屏幕中央 看一眼图片就知道咋回事了
	此类继承了  AbsSpinner
	 */
	private ImageButton details;
	private ImageButton get;
	private ImageButton front;
	private ImageButton back;
	
	//private ScrollView scrollView;
	private ImageView apkLogo;
	private TextView apk_name;
	private TextView apk_price;
	private RatingBar small_ratingbar;
	private TextView pingfen;
	private  AppInfo appInfo;
	private ArrayList<AppInfo> changAppInfos;
	
	private Gallery gallery;
	private int index;
	
	private AsynImageLoader asynImageLoader;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖屏
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gallery);
		//scrollView = (ScrollView) this.findViewById(R.id.scroll);
		//scrollView.setFadingEdgeLength(0);
		
		details = (ImageButton) findViewById(R.id.details);
		get = (ImageButton) findViewById(R.id.get);
		front = (ImageButton) findViewById(R.id.front);
		back = (ImageButton) findViewById(R.id.back);
		
		apkLogo=(ImageView)findViewById(R.id.apk);
		apk_name=(TextView)findViewById(R.id.apk_name);
		apk_price=(TextView)findViewById(R.id.apk_price);
		small_ratingbar=(RatingBar)findViewById(R.id.small_ratingbar);
		pingfen=(TextView)findViewById(R.id.pingfen);
		
		//获得Gallery对象
		gallery = (Gallery) findViewById(R.id.Gallery01);
		//设置Gallery的背景图片
		gallery.setBackgroundResource(R.drawable.ga_home_bg);
		
		details.setOnClickListener(new Button.OnClickListener()
		    {
		      @Override
		      public void onClick(View v)
		      { /* 当启动后，ImageView立刻换底图 */
		    		Intent intent = new Intent(GalleryActivity.this, DetailsActivity.class);
		    		intent.putExtra("AppInfo", appInfo);
		    		startActivity(intent); 
		      }
		    });
		get.setOnClickListener(new Button.OnClickListener()
		    {
		      @Override
		      public void onClick(View v)
		      { /* 当启动后，ImageView立刻换底图 */
		    	  Intent intent = new Intent(Intent.ACTION_VIEW);
		    	  intent .setData(Uri.parse(appInfo.getApp_url()));
		    	  startActivity(intent);
		      }
		    });

		front.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				Log.v("sean"," front " +(index-1));
				
				if(index-1<0)
				{
					Toast.makeText(GalleryActivity.this, "This is the first APP", Toast.LENGTH_SHORT).show();
				}
				else
				{
					/*AppInfo info=changAppInfos.get(index-1);
					
					Intent intent=new Intent();
					intent.putExtra("AppInfo", info);
					
					intent.putExtra("AppInfos", changAppInfos);
					intent.setClass(GalleryActivity.this,  GalleryActivity.class);
					startActivity(intent);*/
				
					appInfo=changAppInfos.get(index-1);
					index--;
					initData();
				}
			}
		});
	
		back.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.v("sean"," back "+ (index+1) );
				if(index+1>=changAppInfos.size())
				{
					Toast.makeText(GalleryActivity.this, "This is the last APP", Toast.LENGTH_SHORT).show();
				}
				else
				{
					/*AppInfo info=changAppInfos.get(index+1);
					
					Intent intent=new Intent();
					intent.putExtra("AppInfo", info);
					
					intent.putExtra("AppInfos", changAppInfos);
					intent.setClass(GalleryActivity.this,  GalleryActivity.class);
					startActivity(intent);*/
					
					appInfo=changAppInfos.get(index+1);
					index++;
					initData();
				}
			}
		});

		asynImageLoader=new AsynImageLoader(this);
		
		Intent intent=getIntent();
		appInfo = (AppInfo) intent.getSerializableExtra("AppInfo");
		changAppInfos= (ArrayList<AppInfo>) intent.getSerializableExtra("AppInfos");
	
		//appInfo == null 为推送的情况
		if(appInfo==null && changAppInfos==null)
		{
			appInfo=C2DMReceiver.appInfo;
			
			changAppInfos=new ArrayList<AppInfo>();
			changAppInfos.add(appInfo);
			Log.d("sean", " new find ... appInfo="+appInfo);
		}
		
	    for (int i = 0; i < changAppInfos.size(); i++) {
			if(appInfo.equals(changAppInfos.get(i)))
			{
				Log.v("sean", "get change index "+i +" size="+changAppInfos.size());
				index=i;
				break;
			}
		}
	    
		
	   initData();

		//设置Gallery的事件监听
		gallery.setOnItemClickListener(new GalleryItemListener());

	}

	private void initData() {
		
		Drawable drawable= asynImageLoader.loadDrawable(appInfo.getLogo(),apkLogo,new LogoImageCallback() {
			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl,
					ImageView imageView) {
				
				if(imageDrawable!=null)
				{
					apkLogo.setImageDrawable(imageDrawable);
				}
			}
		   });
			if(drawable!=null)
			{
				apkLogo.setImageDrawable(drawable);
			}
			apk_name.setText(appInfo.getName());

			apk_price.setText(appInfo.getPrice().equals("0")? "Free": "$"+appInfo.getPrice());
			
			Log.v("sean", "getRate() :"+appInfo.getRate());
			
			Float rate=(appInfo.getRate()!=null && !appInfo.getRate().equals("")) ? Float.valueOf(appInfo.getRate()) : 2.5f;
			small_ratingbar.setMax(5);
			small_ratingbar.setStepSize(0.1f);
			small_ratingbar.setRating(rate);
	
			pingfen.setText(" ("+rate+")");

			//添加ImageAdapter给Gallery对象 注意哦Gallery类并没有setAdapter这个方法 这个方法是从AbsSpinner类继承的
			gallery.setAdapter(new ImageAdapter(this,appInfo.getImage()));
			
	}

	class GalleryItemListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
		/*	Toast.makeText(GalleryActivity.this, "你选择了" + (position + 1) + " 号图片",
					Toast.LENGTH_SHORT).show();*/

		}
	}
}