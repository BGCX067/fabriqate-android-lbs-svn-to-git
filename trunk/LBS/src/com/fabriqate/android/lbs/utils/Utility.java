package com.fabriqate.android.lbs.utils;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.fabriqate.android.lbs.R;
import com.fabriqate.android.lbs.ui.select.GoogleWeatherHandler;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utility {  
    public static void setListViewHeightBasedOnChildren(ListView listView) {  
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            return;  
        }  
  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        listView.setLayoutParams(params);  
    } 
    
	public static String getWeather(String city) {
		String result = "";
		String queryString = "http://www.google.com/ig/api?hl=en&weather="
				+ city;

		GoogleWeatherHandler gwh;
		try {
			URL aURL = new URL(queryString.replace(" ", "%20"));

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			XMLReader xr = sp.getXMLReader();

			gwh = new GoogleWeatherHandler();
			xr.setContentHandler(gwh);

			xr.parse(new InputSource(aURL.openStream()));
			result = gwh.getCurrentCondition();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}
    
    
    /**
     * convert google Weather to  sunny or rain or snow
     * @return
     */
    public static String convertWeather(String result)
    {
    	String weather="sunny";
    	 
    	if(result.equalsIgnoreCase("light rain") ||result.equalsIgnoreCase("rain") || result.equalsIgnoreCase("rain showers") 
				||result.equalsIgnoreCase("showers")||result.equalsIgnoreCase("thunderstorm") || result.equalsIgnoreCase("chance of showers") 
				||result.equalsIgnoreCase("chance of storm")|| result.equalsIgnoreCase("drizzle") 
				||result.equalsIgnoreCase("chance of rain")||result.equalsIgnoreCase("storm") || result.equalsIgnoreCase("thunders")
				||result.equalsIgnoreCase("clear")||result.equalsIgnoreCase("SCATTERED THUNDERSTORMS"))
		{
			weather="rain";
		}
		else if(result.equalsIgnoreCase("chance of snow") ||result.equalsIgnoreCase("rain and snow") || result.equalsIgnoreCase("light snow") 
				|| result.equalsIgnoreCase("overcast") 	|| result.equalsIgnoreCase("LIGHT SNOW") || result.equalsIgnoreCase("ICE/SNOW") 
				|| result.equalsIgnoreCase("SNOW") || result.equalsIgnoreCase("ICE"))
		{
			weather="snow";
		}
		else if(result.equalsIgnoreCase("Clear Cloudy") ||result.equalsIgnoreCase("fog") || result.equalsIgnoreCase("haze") 
				||result.equalsIgnoreCase("mostly cloudy")||result.equalsIgnoreCase("partly cloudy") || result.equalsIgnoreCase("partly sunny") 
				||result.equalsIgnoreCase("sunny")|| result.equalsIgnoreCase("mist")|| result.equalsIgnoreCase("windy")
				|| result.equalsIgnoreCase("smoke") || result.equalsIgnoreCase("PARTLY SUNNY"))
		{
			weather="sunny";
		}
	  return weather;
	  
    }
}  