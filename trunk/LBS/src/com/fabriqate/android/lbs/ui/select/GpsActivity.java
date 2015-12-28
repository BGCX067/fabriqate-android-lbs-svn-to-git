



package com.fabriqate.android.lbs.ui.select;


import com.fabriqate.android.lbs.ui.select.GpsTask.GpsData;
import com.fabriqate.android.lbs.ui.select.IAddressTask.MLocation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class GpsActivity extends Activity  {

	private TextView gps_tip = null;
	private AlertDialog dialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
		
	//	gps_tip = (TextView) findViewById(R.id.tv);
		

		dialog = new ProgressDialog(GpsActivity.this);
		dialog.setTitle("���Ե�...");
		dialog.setMessage("���ڶ�λ...");
		
		GpsTask gpstask = new GpsTask(GpsActivity.this,
				new GpsTaskCallBack() {

					@Override
					public void gpsConnectedTimeOut() {
				//		gps_tip.setText("GPSû�д򿪣���ȡ��ַʧ�ܣ����GPS");
					}

					@Override
					public void gpsConnected(GpsData gpsdata) {
						do_gps(gpsdata);
					}

				}, 3000);
		gpstask.execute();

					
	}
	private void do_gps(final GpsData gpsdata) {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				MLocation location = null;
				try {
					Log.i("do_gpspost", "经纬度：" + gpsdata.getLatitude() + "----" + gpsdata.getLongitude());
					location = new AddressTask(GpsActivity.this,
							AddressTask.DO_GPS).doGpsPost(gpsdata.getLatitude(),
									gpsdata.getLongitude());
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(location == null)
					return "GPS信息获取错误";
				return location.toString();
			}

			@Override
			protected void onPreExecute() {
				dialog.show();
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(String result) {
				gps_tip.setText(result);
				dialog.dismiss();
				super.onPostExecute(result);
			}
			
		}.execute();
	}

	

}