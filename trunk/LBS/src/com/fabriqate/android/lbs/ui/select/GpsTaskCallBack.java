package com.fabriqate.android.lbs.ui.select;

import com.fabriqate.android.lbs.ui.select.GpsTask.GpsData;


public interface GpsTaskCallBack {

	public void gpsConnected(GpsData gpsdata);
	
	public void gpsConnectedTimeOut();
	
}
