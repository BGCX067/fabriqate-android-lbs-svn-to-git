package com.fabriqate.android.lbs.ui.applist;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fabriqate.android.lbs.R;
import com.fabriqate.android.lbs.bean.AppInfo;
import com.fabriqate.android.lbs.ui.appinfo.GalleryActivity;
import com.fabriqate.android.lbs.utils.AsynImageLoader;
import com.fabriqate.android.lbs.utils.AsynImageLoader.LogoImageCallback;

public class AppAdapter extends BaseAdapter {


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
	
	private AsynImageLoader asynImageLoader;
	
	public AppAdapter (Context context,ArrayList<AppInfo> appInfos)
	{
		this.context=context;
		this.appInfos=appInfos;
		size=appInfos.size();
		asynImageLoader=new AsynImageLoader(context);
	}
	
	public int getCount() {
		int result= 0;
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
	
	
	/**
	 * GO TO GalleryActivity
	 * @param context
	 * @param info
	 */
private void toAppInfoActivity(Context context, AppInfo info ) {
		
		Intent intent=new Intent();
		intent.putExtra("AppInfo", info);
        intent.putExtra("AppInfos", this.appInfos);   //Gallery Changed Apps  datasources
		intent.setClass(context,  GalleryActivity.class);
		context.startActivity(intent);
	}
}
