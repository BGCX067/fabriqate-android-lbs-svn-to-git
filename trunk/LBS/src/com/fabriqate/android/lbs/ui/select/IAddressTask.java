package com.fabriqate.android.lbs.ui.select;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public abstract class IAddressTask {

	protected Context context;
	
	public IAddressTask(Context context) {
		this.context = context;
	}
	
	public abstract HttpResponse execute(JSONObject params) throws Exception;
	

	

	public MLocation doGpsPost(double lat, double lng) throws Exception {
		return transResponse(execute(doGps(lat, lng)));
	}

	private MLocation transResponse(HttpResponse response) {
		MLocation location = null;
		if (response.getStatusLine().getStatusCode() == 200) {
			location = new MLocation();
			HttpEntity entity = response.getEntity();
			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader(
						entity.getContent()));
				StringBuffer sb = new StringBuffer();
				String result = br.readLine();
				while (result != null) {
					sb.append(result);
					result = br.readLine();
				}
				JSONObject json = new JSONObject(sb.toString());
				Log.d("action", "=========" + sb.toString());
				JSONObject lca = json.getJSONObject("location");

				location.Access_token = json.getString("access_token");
				if (lca != null) {
					if(lca.has("accuracy"))
						location.Accuracy = lca.getString("accuracy");
					if(lca.has("longitude"))
						location.Latitude = lca.getDouble("longitude");
					if(lca.has("latitude"))
						location.Longitude = lca.getDouble("latitude");
					if(lca.has("address")) {
						JSONObject address = lca.getJSONObject("address");
						if (address != null) {
							if(address.has("region"))
								location.Region = address.getString("region");
							if(address.has("street_number"))
								location.Street_number = address
										.getString("street_number");
							if(address.has("country_code"))
								location.Country_code = address
										.getString("country_code");
							if(address.has("street"))
								location.Street = address.getString("street");
							if(address.has("city"))
								location.City = address.getString("city");
							if(address.has("country"))
								location.Country = address.getString("country");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				location = null;
			}
		}
		return location;
	}

	private JSONObject doGps(double lat, double lng) throws Exception {
		JSONObject holder = new JSONObject();
		holder.put("version", "1.1.0");
		holder.put("host", "maps.google.com");
		holder.put("address_language", "en");
		holder.put("request_address", true);
		
		JSONObject data = new JSONObject();
		data.put("latitude", lat);
		data.put("longitude", lng);
		holder.put("location", data);

		return holder;
	}
	
	
	public static class MLocation {
		public String Access_token;

		public double Latitude;

		public double Longitude;

		public String Accuracy;

		public String Region;

		public String Street_number;

		public String Country_code;

		public String Street;

		public String City;

		public String Country;

		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			//buffer.append("Access_token:" + Access_token + "\n");
			//buffer.append("Region:" + Region + "\n");
		//	buffer.append("Accuracy:" + Accuracy + "\n");
			//buffer.append("Latitude:" + Latitude + "\n");
			//buffer.append("Longitude:" + Longitude + "\n");
		//	buffer.append("Country_code:" + Country_code + "\n");
			
			//buffer.append(Street_number+" " );
			buffer.append(Street+" "  );
			buffer.append(City+" "  );
			buffer.append(Region +" ");
			buffer.append(Country );
			return buffer.toString();
		}
	}

}
