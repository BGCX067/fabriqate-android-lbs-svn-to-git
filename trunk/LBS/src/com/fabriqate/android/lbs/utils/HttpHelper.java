package com.fabriqate.android.lbs.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import android.util.Log;

public final class HttpHelper {

	 private  final static String TAG = "HttpRequest";  
	 
	 /**get Json
	  */
	    public static String getDataFromRequestUrl(String path, Map<String, String> params, String enc) throws IOException   {  
	        /* 
	         * http://127.0.0.1/AndroidService/android/upload?title=aaa&timelength=90的形式 
	         */  
	        StringBuilder sb = new StringBuilder(path);  
	        if(params!=null)
	        {
		        sb.append('?');  
		        for(Map.Entry<String, String> entry : params.entrySet()) {  
		            sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), enc)).append('&');    
		        }  
	        sb.deleteCharAt(sb.length()-1);  
	        }
	          
	        try {  
	            URL url = new URL(sb.toString());  
	            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	            conn.setRequestMethod("GET");    
	            conn.setReadTimeout(5 * 1000);  
	            if(conn.getResponseCode() == 200) {  
	            	
	            	InputStream inStream = conn.getInputStream();
	        	
	        		byte[] data = StreamTool.readInputStream(inStream);
	        		String result = new String(data);

	                return result;  
	            }   
	        } catch (MalformedURLException e) {  
	            e.printStackTrace();  
	            Log.e(TAG, e.toString());  
	            return null;  
	        }  
	        return null;  
	    }  
	    
	    public static String getDataFromRequestUrl(String path, Map<String, String> params) throws IOException   {  
	        /* 
	         * http://127.0.0.1/AndroidService/android/upload?title=aaa&timelength=90的形式 
	         */
	        StringBuilder sb = new StringBuilder(path);  
	        if (params!=null)
	        {
		        sb.append('?');  
		        for(Map.Entry<String, String> entry : params.entrySet()) {  
		            sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), "UTF-8")).append('&');    
		        }  
		        sb.deleteCharAt(sb.length()-1);  
	        }
	        try {  
	            URL url = new URL(sb.toString());  
	            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	            conn.setRequestMethod("GET");    
	            conn.setReadTimeout(5 * 1000);  
	            if(conn.getResponseCode() == 200) {  
	            	InputStream inStream = conn.getInputStream();
	        		byte[] data = StreamTool.readInputStream(inStream);
	        		String result = new String(data);
	        		Log.v(TAG, "result: "+result);
	                return result;  
	            }
	        } catch (MalformedURLException e) {  
	            e.printStackTrace();  
	            Log.e(TAG, e.toString());  
	            return null;  
	        }  
	        return null;  
	    }  

	   
	 
}
