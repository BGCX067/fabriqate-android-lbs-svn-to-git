package com.fabriqate.android.lbs.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

import android.R.drawable;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class AsynImageLoader {

	
	private HashMap<String, SoftReference<Drawable>> imageCache;
	private final String TAG="AsynImageLoader";

	private  Context context;
	private final String LOGO_KEY_FILE="LogoKey.txt";
	private final String IMAGES_FOLDER="Images";
	
	
	public AsynImageLoader(Context context) {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
		this.context=context;
	}


	public  Drawable loadImageFromUrl(String url) {
	
		String fileName=StreamTool.ReadKey(context, LOGO_KEY_FILE, url);
		
		if (!fileName.equals(""))
	    {
		    byte[] data= StreamTool. GetImageCache(url,fileName);
		    
		    if(data!=null&&data.length>0)
		    {
		    	Bitmap bitmap=   StreamTool.Bytes2Bimap(data);
		    	Drawable drawable = new BitmapDrawable(bitmap);   
		    	return drawable;
		    }
	    }

	    String path=this.context.getFilesDir().getAbsolutePath()+File.separatorChar+IMAGES_FOLDER+File.separatorChar;

		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			 path= Environment.getExternalStorageDirectory().getAbsolutePath()+LBSConfig.IMAGES_FOLDER;
		}
		
	    if( StreamTool.CreateDirectory(path))
	    { 
	    	fileName=path+ UUID.randomUUID().toString()+".jpg";
	    }
	    
		URL m;
		InputStream inputStream = null;
		Drawable d;
		
		try {
			m = new URL(url);
			inputStream = (InputStream) m.getContent();
			
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		d= Drawable.createFromStream(inputStream, "src");
		
		if(d!=null)
		{
			StreamTool.WriteKeyValue(context, LOGO_KEY_FILE,url ,fileName);
			Bitmap bmp = StreamTool.drawableToBitmap(d);
			StreamTool.SaveImage(bmp, fileName);
		}
		return d;

	}

	
	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback imageCallback) {
		
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				Log.v(TAG, "drawable from SoftReference");
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}
	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}

/*
	public  Bitmap loadBitmapFromUrl(String imageUrl)
	{
		Bitmap bm=null;
		Drawable drawable=null;
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			drawable= softReference.get();
		}
		else
		{
		  drawable = loadImageFromUrl(imageUrl);
		  imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
		}
		if (drawable != null) {
			BitmapDrawable bd = (BitmapDrawable) drawable;
			bm=bd.getBitmap();
		}
		return bm;
	}
	*/
	
	public interface LogoImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl, ImageView imageView);
	}
	
	public Drawable loadDrawable(final String imageUrl,final ImageView imageView,
			final LogoImageCallback imageCallback) {
		
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				Log.v(TAG, "drawable from SoftReference");
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl,imageView);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	
	
}