package com.fabriqate.android.lbs.service;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.fabriqate.android.lbs.utils.HttpHelper;
import com.fabriqate.android.lbs.utils.LBSConfig;

public class CityImageService {

	/**
	 * city 全拼
	 */
	public String getCityImageUrl(String city)
	{
		String result="";
		
		HashMap<String, String> cityMap=new HashMap<String, String>();
		cityMap.put("city", city);
		try {
			String json= HttpHelper.getDataFromRequestUrl(LBSConfig.URL_CITY_PHOTO, cityMap);
			JSONObject obj=new JSONObject(json);
			result=obj.getString("url");
		} catch (IOException e) {

			result="";
			Log.d("CityImageService", "getCityImageUrl fail  IOException");
		} catch (JSONException e) {
			result="";
			Log.d("CityImageService", "getCityImageUrl fail  JSONException ");
		}
		return  result;
	}

}
