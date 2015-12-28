package com.fabriqate.android.lbs.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class StreamTool {
	
    public static byte[] readInputStream(InputStream inStream) throws IOException  {  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        while( (len=inStream.read(buffer)) != -1 ){  
            outStream.write(buffer, 0, len);  
        }  
        inStream.close();  
        return outStream.toByteArray();  
    } 

	public static void writeFileData(Context context, String fileName,
			InputStream inputStream) {
		try {
			FileOutputStream fout = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			Log.v("gao","fileName write :"+fileName);
			
		//	FileOutputStream fout = new FileOutputStream(fileName);
			byte[]data=readInputStream(inputStream);
			fout.write(data);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static InputStream readFileData(Context context, String fileName) {
	
		try {
			FileInputStream fin =  context.openFileInput(fileName);
			//FileInputStream fin = new   FileInputStream(fileName);  
	       
			byte[] data =readInputStream(fin);
			return new ByteArrayInputStream(data);  
			
			//return fin; 
		} 
		catch (Exception e) {
			return null;
		}
	}
	
	public static String ReadKey(Context context,String fileName,String key)
	{
		SharedPreferences prefs=context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return prefs.getString(key, "");
	}

	public static void WriteKeyValue(Context context,String fileName,String key,String value)
	{
		SharedPreferences prefs=context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		Editor edit=prefs.edit();
		edit.putString(key, value);
		edit.commit();

	}
	
	public static boolean CreateDirectory(String dir)
	{
		File file=new File(dir);
		boolean b=false;
		if(!file.exists())
		{
			b=file.mkdirs();
			System.out.println("�����ڣ������ļ���"+b+dir);
			return b;
		}
		else
		{
			System.out.println("�����ļ���"+dir);
			return true;
		}
	}

	public static void SaveImage(Bitmap bmp,String filename)
	{
		OutputStream out=null;
		try
		{
		//	out=new FileOutputStream(dir+name);
			out=new FileOutputStream(filename);
			bmp.compress(CompressFormat.JPEG, 100, out);
			out.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(out!=null)
			{
				try
				{
					out.close();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static byte[] GetImageCache(String url,String fileName)
	{
		if(fileName==null || fileName.equals(""))
		{
			return null;
		}
		else
		{
			return ReadData(fileName);
		}
	}
	public static byte[] ReadData(String fileName)
	{
		try
		{
			FileInputStream in=new FileInputStream(fileName);
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			byte[] buffer=new byte[1024];
			int len=in.read(buffer);
			while(len!=-1)
			{
				out.write(buffer, 0, buffer.length);
				len=in.read(buffer);
			}
			in.close();
			return out.toByteArray();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static Bitmap drawableToBitmap(Drawable drawable) {  
        
        Bitmap bitmap = Bitmap  
                        .createBitmap(  
                                        drawable.getIntrinsicWidth(),  
                                        drawable.getIntrinsicHeight(),  
                                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                                                        : Bitmap.Config.RGB_565);  
        Canvas canvas = new Canvas(bitmap);  
        //canvas.setBitmap(bitmap);  
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());  
        drawable.draw(canvas);  
        return bitmap;  
}  

	public static Bitmap Bytes2Bimap(byte[] b){  
        if(b.length!=0){  
            return BitmapFactory.decodeByteArray(b, 0, b.length);  
        }  
        else {  
            return null;  
        }  
  }
}
