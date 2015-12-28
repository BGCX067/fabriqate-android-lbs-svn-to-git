package com.fabriqate.android.lbs.ui.applist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import com.fabriqate.android.lbs.R;
import com.fabriqate.android.lbs.bean.AppInfo;
import com.fabriqate.android.lbs.service.AppService;
import com.fabriqate.android.lbs.utils.AsynImageLoader;
import com.fabriqate.android.lbs.utils.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;




public class MissApplistActivity extends Activity
{
    private final String TAG="MissApplistActivity";

    
	private ListView lvCategory1;
	private TextView tvPosition;
	
	private AsynImageLoader asynImageLoader;

	private HashMap<String, ArrayList<AppInfo>> catagroyMap;

	private AppAdapter positionAdapter;

	  String time;
	  String weather;
	  String position;
	  String cat;
	  
	  private AppService appService;
	  private  ArrayList<AppInfo> allApps;
	  private Boolean isReady=false;
	 	  

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.missapplist_activity);
        findView();
        asynImageLoader = new AsynImageLoader(this);
        appService=new AppService();

	/*	Intent intent=getIntent();

		allApps=(ArrayList<AppInfo>) intent.getSerializableExtra("missInfos");


	    if(allApps!=null)
	    {
	    	Log.v(TAG, "app count =  "+ allApps.size());
	    	positionAdapter=new AppAdapter(MissApplistActivity.this, allApps);
	    	lvCategory1.setAdapter(positionAdapter);
	    	Utility.setListViewHeightBasedOnChildren(lvCategory1);
	    }*/
		
		new GetMissAppsTask(this).execute("");
    }


	private void findView() {

        lvCategory1=(ListView)findViewById(R.id.lvCategory1);

        tvPosition=(TextView)findViewById(R.id.tvPosition);
	}
	

	private class GetMissAppsTask  extends AsyncTask<String, Void, Void>
	{
	   private ProgressDialog progressDialog;
	   Context context;
	   public GetMissAppsTask(Context context)
	   {
		   this.context=context;
	   }
		@Override
		protected void onPostExecute(Void result) {
	
			Log.v(TAG, "##########allapp="+allApps);
		    if(allApps!=null)
		    {
		    	Log.v(TAG, "app count =  "+ allApps.size());
		    	//positionAdapter=new AppAdapter(MissApplistActivity.this, allApps);
		    	lvCategory1.setAdapter(positionAdapter);
		    	Utility.setListViewHeightBasedOnChildren(lvCategory1);
		    }

			try
			{
				progressDialog.dismiss();
				progressDialog=null;
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
		    progressDialog=new ProgressDialog(context);
		    progressDialog.setMessage("Loading");
		    progressDialog.show();
		}

		@Override
		protected Void doInBackground(String... params) {

			String url = "http://lbs.pm.fabriqate.com/api/miss_app?device_type=android";
			try {
				allApps = appService.getMissedConnectionApps(url, null);
			 	positionAdapter=new AppAdapter(MissApplistActivity.this, allApps);
			 	
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			//Intent intent=getIntent();

			//allApps=(ArrayList<AppInfo>) intent.getSerializableExtra("missInfos");
			
			
			return null;
		}
		
	}
 
	
}
