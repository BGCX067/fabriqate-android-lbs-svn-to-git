package com.fabriqate.android.lbs.ui.appinfo;

import java.util.ArrayList;

import com.fabriqate.android.lbs.utils.AsynImageLoader;
import com.fabriqate.android.lbs.utils.AsynImageLoader.ImageCallback;
import com.fabriqate.android.lbs.utils.AsynImageLoader.LogoImageCallback;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;


public class ImageAdapter extends BaseAdapter {

	// 定义Context
	private Context mContext;
	// 定义整型数组 即图片源
	/*private Integer[] mImageIds = { R.drawable.mai, R.drawable.mai,
			R.drawable.mai, R.drawable.icon3,};*/
//	private Integer[] mImageIds = { };

	private  ArrayList<String> mImageUrls ;

	private AsynImageLoader asynImageLoader;
	
	// 声明ImageAdapter
	public ImageAdapter(Context c,ArrayList<String> mImageUrls) {
		mContext = c;
		this.mImageUrls=mImageUrls;
		asynImageLoader=new AsynImageLoader(c);
	}

	// 获取图片的个数
	public int getCount() {
		return mImageUrls.size();
	}

	// 获取图片在库中的位置
	public Object getItem(int position) {
		return position;
	}

	// 获取图片ID
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ImageView imageView = new ImageView(mContext);

		/*Drawable drawable= asynImageLoader.loadDrawable(mImageUrls.get(position),new ImageCallback() {
			
			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				 if(imageDrawable!=null)
				 {
					 imageView.setImageDrawable(imageDrawable);
				 }
			}
		});
		if(drawable!=null)
		{
			imageView.setImageDrawable(drawable);
		}*/
		
	    Drawable drawable= asynImageLoader.loadDrawable(mImageUrls.get(position),imageView,new LogoImageCallback() {
			
			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl,
					ImageView imageView) {
				if(imageDrawable!=null)
				 {
					 imageView.setImageDrawable(imageDrawable);
				 }
				
			}
		});
	    if(drawable!=null)
		{
			imageView.setImageDrawable(drawable);
		}
		
		// 给ImageView设置资源
		//imageView.setImageResource(mImageIds[position]);
				
		// 设置布局 图片120*120
		imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		// 设置显示比例类型
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		return imageView;
	}

}
