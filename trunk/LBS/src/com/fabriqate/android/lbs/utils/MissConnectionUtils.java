package com.fabriqate.android.lbs.utils;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.maps.GeoPoint;

public class MissConnectionUtils {
	
	public static GeoPoint getGeoByLocation(Location location) {  
        GeoPoint gp = null;  
        try {  
            if (location != null) {  
                double geoLatitude = location.getLatitude() * 1E6;  
                double geoLongitude = location.getLongitude() * 1E6;  
                gp = new GeoPoint((int) geoLatitude, (int) geoLongitude);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return gp;  
    } 
	
	public static Address getAddressbyGeoPoint(Context context,GeoPoint gp) {  
        Address result = null;  
        try {  
            if (gp != null) {  
                Geocoder gc = new Geocoder(context, Locale.US);  
                double geoLatitude = (int) gp.getLatitudeE6() / 1E6;  
                double geoLongitude = (int) gp.getLongitudeE6() / 1E6;  
                List<Address> lstAddress = gc.getFromLocation(geoLatitude, geoLongitude, 1);  
                if (lstAddress.size() > 0) {  
                    result = lstAddress.get(0);  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  

}
