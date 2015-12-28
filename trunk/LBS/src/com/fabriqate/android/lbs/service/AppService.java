package com.fabriqate.android.lbs.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.fabriqate.android.lbs.bean.AppInfo;
import com.fabriqate.android.lbs.utils.HttpHelper;
import com.fabriqate.android.lbs.utils.LBSConfig;

public class AppService {
	 private  final  String TAG = "AppService";  
	
	public ArrayList<AppInfo> getAppList(String url,HashMap<String, String> params) throws IOException
	{
		    ArrayList<AppInfo> result=new ArrayList<AppInfo>();

			String json= getJsonFromUrl(url, params);		
			if(json!=null && !json.equals(""))
			{
				ArrayList<AppInfo> positionApps=getPositionApps(json);
				ArrayList<AppInfo> timeApps=getTimeApps(json);
				ArrayList<AppInfo> weatherApps=	getWeatherApps(json);
				if(positionApps.size()>0)
				{
					result.addAll(positionApps);
				}
				if(timeApps.size()>0)
				{
					result.addAll(timeApps);
				}
				if(weatherApps.size()>0)
				{
					result.addAll(weatherApps);
				}
			}
			return result;
	}

	public String getJsonFromUrl(String url,HashMap<String, String> params) throws IOException
	{
		String data = HttpHelper.getDataFromRequestUrl(url, params);
		//find "the count of app"
		String json= validJSON(data);
		if(json==null)
		{
			return null;
		}
		return json;
	}
	
	private String validJSON(String data) {
		
		if(data==null || data.trim().equals(""))
		{
			return null;
		}
		/*else if(data.indexOf("the count of app")>-1)
		{
			if(data.indexOf("the count of app:0")>-1)
			{
				return null;
			}
			int start=data.indexOf("{");
			data=data.substring(start);
			Log.v(TAG,"data:"+data);
			return data;
		}*/
		else if(data.equals("0"))
		{
			return null;
		}
		return data;
	}
	
	public ArrayList<AppInfo> getPositionApps(String json) 
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		
		try {
			
			JSONObject applist=new JSONObject(json);
			JSONArray postionApps=applist.getJSONArray("position");
			for (int i = 0; i < postionApps.length(); i++) {
				JSONObject positionApp=postionApps.getJSONObject(i);
				
				AppInfo info=jsonObjectToAppInfo(positionApp,AppInfo.TYPE_POSITION);	
		
				result.add(info);
			}
			
		} catch (JSONException e) {


			e.printStackTrace();
		}
		return	result;
	}
	public ArrayList<AppInfo> getTimeApps(String json) 
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		try {
			JSONObject applist=new JSONObject(json);
	
			JSONArray timeApps=applist.getJSONArray("time");
			for (int i = 0; i < timeApps.length(); i++) {
				JSONObject timeApp=timeApps.getJSONObject(i);
				
				AppInfo info=jsonObjectToAppInfo(timeApp,AppInfo.TYPE_TIME);			  
				result.add(info);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return	result;
	}
	public ArrayList<AppInfo> getWeatherApps(String json) 
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		try {
			JSONObject applist=new JSONObject(json);
			JSONArray weatherApps=applist.getJSONArray("weather");
			for (int i = 0; i < weatherApps.length(); i++) {
				JSONObject weatherApp=weatherApps.getJSONObject(i);
				
				AppInfo info=jsonObjectToAppInfo(weatherApp,AppInfo.TYPE_WEATHER);			  
				result.add(info);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return	result;
	}
	private AppInfo jsonObjectToAppInfo(JSONObject objcet,String type) throws JSONException
	{
		AppInfo info=new AppInfo();
		  info.setApp_id(objcet.getInt("app_id"));
		 info.setName(objcet.getString("name"));
		  info.setApp_url(objcet.getString("app_url"));
		  info.setCaption(objcet.getString("caption"));
		  info.setDescription(objcet.getString("description"));
		  info.setLogo(objcet.getString("logo"));
		  info.setPrice(objcet.getString("price"));
		  
		  String rate=objcet.getString("rate");
		  info.setRate((rate==null || rate.equals("")) ? "": objcet.getString("rate").split(",")[0].replace("星",""));
		  
		  info.setType(type);
		  ArrayList<String> image= new ArrayList<String>();
		  
		  JSONArray imageArray= objcet.getJSONArray("image");
		   for (int j = 0; j < imageArray.length(); j++) {
			   image.add(imageArray.getString(j));
		}
		
		info.setImage(image);
		
		return info;
	}
	
	public ArrayList<AppInfo> getFreePositionApps(String json) throws JSONException
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		
		ArrayList<AppInfo> allPosition =getPositionApps(json);
		for (AppInfo appInfo : allPosition) {
			if(appInfo.getPrice().equals("0"))
			{
				if(!result.contains(appInfo))
				{
					result.add(appInfo);
				}
			}
		}
		return result;
	}
	public ArrayList<AppInfo> getPaidPositionApps(String json) throws JSONException
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		
		ArrayList<AppInfo> allPosition =getPositionApps(json);
		for (AppInfo appInfo : allPosition) {
			if(!appInfo.getPrice().equals("0"))
			{
				if(!result.contains(appInfo))
				{
					result.add(appInfo);
				}
			}
		}
		return result;
	}

	
	public ArrayList<AppInfo> getFreeTimeApps(String json) throws JSONException
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		
		ArrayList<AppInfo> allTimeApps =getTimeApps(json);
		for (AppInfo appInfo : allTimeApps) {
			if(appInfo.getPrice().equals("0"))
			{
				if(!result.contains(appInfo))
				{
					result.add(appInfo);
				}
			}
		}
		return result;
	}
	public ArrayList<AppInfo> getPaidTimeApps(String json) throws JSONException
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		
		ArrayList<AppInfo> allTimeApps =getTimeApps(json);
		for (AppInfo appInfo : allTimeApps) {
			if(!appInfo.getPrice().equals("0"))
			{
				if(!result.contains(appInfo))
				{
					result.add(appInfo);
				}
			}
		}
		return result;
	}

	
	public ArrayList<AppInfo> getFreeWeatherApps(String json) throws JSONException
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		
		ArrayList<AppInfo> allWeatherApps =getWeatherApps(json);
		for (AppInfo appInfo : allWeatherApps) {
			if(appInfo.getPrice().equals("0"))
			{
				if(!result.contains(appInfo))
				{
					result.add(appInfo);
				}
			}
		}
		return result;
	}
	public ArrayList<AppInfo> getPaidWeatherApps(String json) throws JSONException
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		ArrayList<AppInfo> allWeatherApps =getWeatherApps(json);
		for (AppInfo appInfo : allWeatherApps) {
			if(!appInfo.getPrice().equals("0"))
			{
				if(!result.contains(appInfo))
				{
					result.add(appInfo);
				}
			}
		}
		return result;
	}

	
	public ArrayList<AppInfo> getFreePositionApps(ArrayList<AppInfo> allAppInfos)
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		for(int i=0;i<allAppInfos.size();i++)
		{
			AppInfo info=allAppInfos.get(i);
			if(info.getPrice().equals("0") && info.getType().equals(AppInfo.TYPE_POSITION))
			{
				if(!result.contains(info))
				{
					result.add(info);
				}
			}
		}
		return result;
	}
	public ArrayList<AppInfo> getPaidPositionApps(ArrayList<AppInfo> allAppInfos)
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		for(int i=0;i<allAppInfos.size();i++)
		{
			AppInfo info=allAppInfos.get(i);
			if(!info.getPrice().equals("0") && info.getType().equals(AppInfo.TYPE_POSITION))
			{
				if(!result.contains(info))
				{
					result.add(info);
				}
			}
		}
		return result;
	}
	public ArrayList<AppInfo> getFreeTimeApps(ArrayList<AppInfo> allAppInfos)
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		for(int i=0;i<allAppInfos.size();i++)
		{
			AppInfo info=allAppInfos.get(i);
			if(info.getPrice().equals("0") && info.getType().equals(AppInfo.TYPE_TIME))
			{
				if(!result.contains(info))
				{
					result.add(info);
				}
			}
		}
		return result;
	}
	public ArrayList<AppInfo> getPaidTimeApps(ArrayList<AppInfo> allAppInfos)
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		for(int i=0;i<allAppInfos.size();i++)
		{
			AppInfo info=allAppInfos.get(i);
			if(!info.getPrice().equals("0") && info.getType().equals(AppInfo.TYPE_TIME))
			{
				if(!result.contains(info))
				{
					result.add(info);
				}
			}
		}
		return result;
	}
	public ArrayList<AppInfo> getFreeWeatherApps(ArrayList<AppInfo> allAppInfos)
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		for(int i=0;i<allAppInfos.size();i++)
		{
			AppInfo info=allAppInfos.get(i);
			if(info.getPrice().equals("0") && info.getType().equals(AppInfo.TYPE_WEATHER))
			{
				if(!result.contains(info))
				{
					result.add(info);
				}
			}
		}
		return result;
	}
	public ArrayList<AppInfo> getPaidWeatherApps(ArrayList<AppInfo> allAppInfos)
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		for(int i=0;i<allAppInfos.size();i++)
		{
			AppInfo info=allAppInfos.get(i);
			if(!info.getPrice().equals("0") && info.getType().equals(AppInfo.TYPE_WEATHER))
			{
				if(!result.contains(info))
				{
					result.add(info);
				}
			}
		}
		return result;
	}
	public ArrayList<AppInfo> getPositionApps(ArrayList<AppInfo> allAppInfos)
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		for(int i=0;i<allAppInfos.size();i++)
		{
			AppInfo info=allAppInfos.get(i);
			if(info.getType().equals(AppInfo.TYPE_POSITION))
			{
				if(!result.contains(info))
				{
					result.add(info);
				}
			}
		}
		return result;
	}
	public ArrayList<AppInfo> getTimeApps(ArrayList<AppInfo> allAppInfos)
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		for(int i=0;i<allAppInfos.size();i++)
		{
			AppInfo info=allAppInfos.get(i);
			if(info.getType().equals(AppInfo.TYPE_TIME))
			{
				if(!result.contains(info))
				{
					result.add(info);
				}
			}
		}
		return result;
	}
	public ArrayList<AppInfo> getWeatherApps(ArrayList<AppInfo> allAppInfos)
	{
		ArrayList<AppInfo> result=new ArrayList<AppInfo>();
		for(int i=0;i<allAppInfos.size();i++)
		{
			AppInfo info=allAppInfos.get(i);
			if(info.getType().equals(AppInfo.TYPE_WEATHER))
			{
				if(!result.contains(info))
				{
					result.add(info);
				}
			}
		}
		return result;
	}
	public ArrayList<AppInfo> getAppListByCat(String cat,String time,String weather ,String position) throws IOException
	{
		HashMap<String, String> params=new HashMap<String, String>();
		params.put("device_type", "android");
		params.put("time", time);
		params.put("weather", weather);
		params.put("position", position);
		params.put("cat", cat);
		return getAppList(LBSConfig.URL_CATEGORY_APP,params);
	}
	
	
	public ArrayList<AppInfo> getMissedConnectionApps(String url,HashMap<String, String> params) throws IOException, JSONException{
		String jsonStr = getJsonFromUrl(url, params);
		if(jsonStr != null && !"".equals(jsonStr)){
			ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();
			JSONArray jsonArray = new JSONArray(jsonStr);
			int length = jsonArray.length();
			int imageLength = 0;
			AppInfo appInfo = null;
			JSONObject object = null;
			JSONArray imageArray = null;
			ArrayList<String> imageList = null;
			for (int i = 0; i < length; i++) {
				appInfo = new AppInfo();
				object = jsonArray.getJSONObject(i);
				appInfo.setApp_id(object.getInt("app_id"));
				appInfo.setName(String.valueOf(object.get("name")));
				appInfo.setDescription(String.valueOf(object.get("description")));
				appInfo.setApp_url(String.valueOf(object.get("app_url")));
				appInfo.setLogo(String.valueOf(object.get("logo")));
				appInfo.setPrice(String.valueOf(object.get("price")));
				
				String rate=String.valueOf(object.get("rate"));
				
				appInfo.setRate( rate!=null&& !rate.equals("")? rate.split(",")[0].replace("星","") : "");
				
				//appInfo.setRate(String.valueOf(object.get("rate")));
				
				
				appInfo.setCaption(String.valueOf(object.get("caption")));
				imageArray = object.getJSONArray("image");
				imageLength = imageArray.length();
				if(imageLength > 0){
					imageList = new ArrayList<String>();
					for (int j = 0; j < imageLength; j++) {
						imageList.add(imageArray.getString(j));
					}
				}
				appInfo.setImage(imageList);
				appInfos.add(appInfo);
			}
			return appInfos;
		}else{
			return null;
		}
	}
	
	/**
	 * time,weather,position
	 * @param RegistrationId
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void getPushApp(String RegistrationId,String weather,String city) throws ClientProtocolException, IOException{
		String result = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("deviceToken", RegistrationId));
		HttpClient httpClient = new DefaultHttpClient();
	//HttpPost httpPost = new HttpPost("http://lbs.pm.fabriqate.com/api/position_app?device_type=android&time=12&weather=good&position=zh");
		SimpleDateFormat  format= new SimpleDateFormat("HH:mm:ss");
		
		Date date=new Date(System.currentTimeMillis());
		String time=format.format(date);
	
		Log.v(TAG, "time="+time);
		
		HttpPost httpPost = new HttpPost("http://lbs.pm.fabriqate.com/api/position_app?device_type=android&time="+time+"&weather="+ weather+"&position="+city );
		
		
		httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		HttpResponse httpResponse = httpClient.execute(httpPost);
		if(httpResponse.getStatusLine().getStatusCode() == 200){
			result = EntityUtils.toString(httpResponse.getEntity());
			Log.d(TAG, "loading data succefully, result = " + result);
		}else{
			result = EntityUtils.toString(httpResponse.getEntity());
			Log.d(TAG, "loading data failed");
		}
	}
	
	public AppInfo getPositionAppById(String appId) throws JSONException, IOException{
		String jsonStr = getJsonFromUrl("http://lbs.pm.fabriqate.com/api/get_app?app_id=" + appId, null);
		Log.d(TAG, "jsonStr = " + jsonStr);
		if(jsonStr != null && !"".equals(jsonStr)){
			JSONArray jsonArray = new JSONArray(jsonStr);
			int length = jsonArray.length();
			Log.d(TAG, "length = " + length);
			int imageLength = 0;
			AppInfo appInfo = null;
			JSONObject object = null;
			JSONArray imageArray = null;
			ArrayList<String> imageList = null;
			for (int i = 0; i < length; i++) {
				appInfo = new AppInfo();
				object = jsonArray.getJSONObject(i);
//				appInfo.setApp_id(object.getInt("app_id"));
				appInfo.setName(String.valueOf(object.get("name")));
				appInfo.setDescription(String.valueOf(object.get("description")));
				appInfo.setApp_url(String.valueOf(object.get("app_url")));
				appInfo.setLogo(String.valueOf(object.get("logo")));
				appInfo.setPrice(String.valueOf(object.get("price")));
				
				String rate=String.valueOf(object.get("rate"));
				Log.v(TAG, "原始rate="+rate +"  ### "+rate.split(",")[0].replace("星",""));
				
				appInfo.setRate( rate!=null&& !rate.equals("")? rate.split(",")[0].replace("星","") : "");
				
				
				appInfo.setCaption(String.valueOf(object.get("caption")));
				imageArray = object.getJSONArray("image");
				imageLength = imageArray.length();
				if(imageLength > 0){
					imageList = new ArrayList<String>();
					for (int j = 0; j < imageLength; j++) {
						imageList.add(imageArray.getString(j));
					}
				}
				appInfo.setImage(imageList);
			}
			return appInfo;
		}else{
			return null;
		}
	}
	
}
