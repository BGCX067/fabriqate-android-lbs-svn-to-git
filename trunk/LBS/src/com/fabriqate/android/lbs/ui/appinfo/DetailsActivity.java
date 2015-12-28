package com.fabriqate.android.lbs.ui.appinfo;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fabriqate.android.lbs.C2DMReceiver;
import com.fabriqate.android.lbs.R;
import com.fabriqate.android.lbs.bean.AppInfo;
import com.fabriqate.android.lbs.utils.AsynImageLoader;
import com.fabriqate.android.lbs.utils.AsynImageLoader.LogoImageCallback;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class DetailsActivity extends Activity {
    /** Called when the activity is first created. */
	private TextView tv;
	private ScrollView scrollView;
	
	private ImageButton get;
	private ImageView apkLogo;
	private TextView apk_name;
	private TextView apk_price;
	private RatingBar small_ratingbar;
	private TextView pingfen;
	private TextView tvDetail;
	private AsynImageLoader asynImageLoader;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//ǿ��Ϊ����
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.details);
        scrollView = (ScrollView) this.findViewById(R.id.scroll);
		scrollView.setFadingEdgeLength(0);
		
		get = (ImageButton) findViewById(R.id.get);
		
		apkLogo=(ImageView)findViewById(R.id.apk);
		apk_name=(TextView)findViewById(R.id.apk_name);
		apk_price=(TextView)findViewById(R.id.apk_price);
		small_ratingbar=(RatingBar)findViewById(R.id.small_ratingbar);
		pingfen=(TextView)findViewById(R.id.pingfen);
		
		tvDetail=(TextView)findViewById(R.id.tvDetail);
		asynImageLoader=new AsynImageLoader(this);
		Intent intent=getIntent();
		AppInfo tempAppInfo = (AppInfo) intent.getSerializableExtra("AppInfo");
		//tempAppInfo == null 为推送的情况
		if(tempAppInfo == null){
			tempAppInfo = C2DMReceiver.appInfo;;
		}
	   final AppInfo appInfo = tempAppInfo;
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
			Float rate=Float.valueOf(appInfo.getRate());
			small_ratingbar.setMax(5);
			small_ratingbar.setStepSize(0.1f);
			small_ratingbar.setRating(rate);
			pingfen.setText(" ("+rate+")");
			
			tvDetail.setText( Html.fromHtml( appInfo.getDescription()));
			tvDetail.setTextSize(18f);
			
			get.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
		
					  Intent intent = new Intent(Intent.ACTION_VIEW);
			    	  intent .setData(Uri.parse(appInfo.getApp_url()));
			    	  startActivity(intent);
					
				}
			});
			
    }
   
}