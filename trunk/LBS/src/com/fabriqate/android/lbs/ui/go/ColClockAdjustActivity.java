package com.fabriqate.android.lbs.ui.go;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fabriqate.android.lbs.LoginActivity;
import com.fabriqate.android.lbs.R;
import com.fabriqate.android.lbs.bean.AppInfo;
import com.fabriqate.android.lbs.service.AppService;
import com.fabriqate.android.lbs.ui.applist.MissApplistActivity;
import com.fabriqate.android.lbs.ui.login.Facebook;
import com.fabriqate.android.lbs.ui.select.PathMenuActivity;

public class ColClockAdjustActivity extends Activity {
	/** Called when the activity is first created. */

	MyClockView myClockView;
	private TextView tv1;
	private TextView tv2;
	private ImageView imga;
	private ArrayList<AppInfo> appInfos;
	 private LinearLayout lv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 强制为竖屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.go);
		//handleMissConnection();
		new GetMissConnectionTask().execute(null);
		init();
		getInstanceCount();
		Log.d("action", "###########################"+getInstanceCount());
		
	}

	public void init() {
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		
		
        ImageView imageView=new ImageView(this);

		myClockView = new MyClockView(this,imageView);
		myClockView.setColClockAdjustActivity(this);
		// Bitmap minuteBitmap = BitmapFactory.decodeResource(getResources(),
		// R.drawable.go);
		Bitmap minuteBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.go);
		myClockView.init(minuteBitmap);
		myClockView.setCenter(240, 544); // 本例时钟中心点相对于视图坐标为（163， 163）
		myClockView.setPointOffset(35, 133); // 12点钟方向指针向下偏移20像素
		//myClockView.

		// setContentView(R.layout.main);
		
		addContentView(myClockView, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		ImageView logout=(ImageView)findViewById(R.id.logout);
		logout.setFocusable(true);
		logout.bringToFront();
		logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new AlertDialog.Builder(ColClockAdjustActivity.this)
				.setTitle("Do you want to log out?")
				
				.setPositiveButton("YES", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences mPreferences=ColClockAdjustActivity.this
								.getSharedPreferences("com.fabriqate.android.lbs", Context.MODE_PRIVATE);
						SharedPreferences.Editor editor=mPreferences.edit();
						editor.putString("access_token", null);
						editor.putLong("access_expires", 0);
						editor.commit();
						
						Facebook facebook = new Facebook("223893791058699");
						try {
							facebook.logout(getApplicationContext());
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						Intent intent=new Intent(ColClockAdjustActivity.this, LoginActivity.class);
						startActivity(intent);
						finish();
						
					}
				})
				.setNegativeButton("NO", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				})
				.show();
			
			}
		});
		 
		  LinearLayout. LayoutParams layoutParams=new LinearLayout.LayoutParams(30, 30);
		   
		 //  AbsoluteLayout.LayoutParams layoutParams=new AbsoluteLayout.LayoutParams(100, 100, 140, 465);
		   
		    
		    imageView.setLayoutParams(layoutParams);
		   
		    imageView.setImageResource(R.drawable.go_focus_bg);
		    imageView.setVisibility(View.GONE);
		    imageView.setFocusable(false);

		    imageView.setPadding(138,464, 135, 115);
		 //   imageView.layout(180,500 , 0, 0);
		    
		   // imageView.setBackgroundColor(Color.RED);
		    addContentView(imageView, new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
		
	}

	public void lift(boolean isLeft) {
		//Intent intent = new Intent(this, ChooseTiemActivity.class);
		Intent intent = new Intent(this, ChooseClockActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
		startActivity(intent);
		
		finish();
	}
	public void right(boolean isLeft) {
		Intent intent = new Intent(this, PathMenuActivity.class);
		startActivity(intent);
	}
	public void lifttv(boolean isLeft) {
		tv1.setVisibility(View.GONE);
		  tv2.setVisibility(View.VISIBLE);
	}
	public void righttv(boolean isLeft) {
		tv2.setVisibility(View.GONE);
		  tv1.setVisibility(View.VISIBLE);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		myClockView.onKeyDown(keyCode, event);

		return super.onKeyDown(keyCode, event);
	}
	
	private void handleMissConnection(){
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				AppService appService = new AppService();
				String url = "http://lbs.pm.fabriqate.com/api/miss_app?device_type=android";
				try {
					 appInfos = appService.getMissedConnectionApps(url, null);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		handler.sendEmptyMessage(0);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Missed Connections");
		builder.setMessage("You missed some amazing apps when you closed.");
		builder.setPositiveButton("Go to see", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				Intent intent = new Intent();
				intent.putExtra("missInfos", appInfos);
				intent.setClass(ColClockAdjustActivity.this,MissApplistActivity.class );
				startActivity(intent);
			}
		});
		builder.setNegativeButton("No thanks", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.create().show();
	}

    private class GetMissConnectionTask extends  AsyncTask<Object,Object,Object >
    {
    	private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			progressDialog=new ProgressDialog(ColClockAdjustActivity.this);
			progressDialog.setTitle("Missed Connections");
			progressDialog.setMessage("Loading");
			progressDialog.show();
			
			//super.onPreExecute();
		}

		@Override
		protected Object doInBackground(Object... params) {
			
			/*AppService appService = new AppService();
			String url = "http://lbs.pm.fabriqate.com/api/miss_app?device_type=android";
			try {
				 appInfos = appService.getMissedConnectionApps(url, null);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}*/
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			if(progressDialog!=null)
			{
				progressDialog.dismiss();
			}
			if(ColClockAdjustActivity.this.isFinishing())
			{
				return;
			}
			//super.onPostExecute(result);
			AlertDialog.Builder builder = new AlertDialog.Builder(ColClockAdjustActivity.this);
			builder.setTitle("Missed Connections");
			builder.setMessage("You missed some amazing apps when you closed.");
			builder.setPositiveButton("Go to see", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					Intent intent = new Intent();
					//intent.putExtra("missInfos", appInfos);
					intent.setClass(ColClockAdjustActivity.this,MissApplistActivity.class );
					startActivity(intent);
				}
			});
			builder.setNegativeButton("No thanks", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			builder.create().show();
		}
		
		
    	
    }

}