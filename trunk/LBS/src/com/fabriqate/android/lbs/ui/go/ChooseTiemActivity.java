package com.fabriqate.android.lbs.ui.go;

import com.fabriqate.android.lbs.R;
import com.fabriqate.android.lbs.utils.LBSConfig;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;

import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import android.widget.Toast;

public class ChooseTiemActivity extends Activity {

	private final String TAG = "ChooseTiemActivity";

	private TextView tvTime;

	private ImageButton btnLeft;
	private ImageButton btnRight;
	private ImageButton btnOK;
	private int mProgress;
	private SeekBar mSeekBar;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_choosetime);

		mSeekBar = (SeekBar) findViewById(R.id.seekBar1);

		tvTime = (TextView) findViewById(R.id.tvTime);
		btnLeft = (ImageButton) findViewById(R.id.btnLeft);
		btnRight = (ImageButton) findViewById(R.id.btnRight);
		btnOK = (ImageButton) findViewById(R.id.btnOK);

		MyOnclikListener listener = new MyOnclikListener();
		btnLeft.setOnClickListener(listener);
		btnRight.setOnClickListener(listener);
		btnOK.setOnClickListener(listener);
		
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				seekBar.setProgress(mProgress);
				SetTimeText(mProgress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

				// tvTime.setText(mProgress+" minutes");

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub

				int mod = progress % 5; // 是否整除

				int step = progress / 5;

				if (mod != 0) {
					mProgress = (step + 1) * 5;
				} else {
					mProgress = step * 5;
				}

				SetTimeText(mProgress);
			}
		});

		mProgress = 0;
		SetTimeText(mProgress);
	}

	private void SetTimeText(int progress) {
		tvTime.setText(progress+"");
	}

	private class MyOnclikListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btnLeft:

				Log.d(TAG, "mProgress=" + mProgress);
				if (mProgress - 5 > -1) {
					mProgress = mProgress - 5;
					mSeekBar.setProgress(mProgress);
					SetTimeText(mProgress);
				}
				break;

			case R.id.btnRight:
	
				Log.d(TAG, "mProgress=" + mProgress);
				if (mProgress + 5 < 180 || mProgress + 5 == 180) {
					mProgress = mProgress + 5;
					mSeekBar.setProgress(mProgress);
					SetTimeText(mProgress);
				}

				break;
				
			case R.id.btnOK:
				
				Log.d(TAG, "btnOK..."+mProgress);
				Intent intent=new Intent();
				intent.setAction(LBSConfig.ACTION_NEWFINDING_TIME);
				intent.putExtra("minutes", mProgress);
				sendBroadcast(intent);
				Log.d(TAG, "sendBroadcast");
				finish();
			//	android.os.Process.killProcess(android.os.Process.myPid());
			//	System.exit(0);

				
				break;

			default:
				break;
			}
		}

	}

}