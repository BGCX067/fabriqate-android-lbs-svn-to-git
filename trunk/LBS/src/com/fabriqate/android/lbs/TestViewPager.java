package com.fabriqate.android.lbs;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class TestViewPager extends Activity
{
	private ViewPager myViewPager;

	private MyPagerAdapter myAdapter;

	private LayoutInflater mInflater;
	private List<View> mListViews;
	private View layout1 = null;
	private View layout2 = null;
	private View layout3 = null;
	private View layout4 = null;
	private View layout5 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//ǿ��Ϊ����
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.viewpager_layout);
		myAdapter = new MyPagerAdapter();
		myViewPager = (ViewPager) findViewById(R.id.viewpagerLayout);
		myViewPager.setAdapter(myAdapter);

		mListViews = new ArrayList<View>();
		mInflater = getLayoutInflater();
		layout1 = mInflater.inflate(R.layout.layout1, null);
		layout2 = mInflater.inflate(R.layout.layout2, null);
		layout3 = mInflater.inflate(R.layout.layout3, null);
		layout4 = mInflater.inflate(R.layout.layout4, null);
		layout5 = mInflater.inflate(R.layout.layout5, null);

		mListViews.add(layout1);
		mListViews.add(layout2);
		mListViews.add(layout3);
		mListViews.add(layout4);
		mListViews.add(layout5);

		// ��ʼ����ǰ��ʾ��view
		myViewPager.setCurrentItem(0);

		myViewPager.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int arg0)
			{
				Log.d("action", "onPageSelected - " + arg0);
				if(arg0==4){
					
				
				// activity��1��2������2�����غ���ô˷���
				View v = mListViews.get(arg0);
				ImageButton button = (ImageButton) v.findViewById(R.id.button1);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(TestViewPager.this, LoginActivity.class);
					startActivity(intent); 
					finish();
					
				}
			});
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
				Log.d("action", "onPageScrolled - " + arg0);
				// ��1��2��������1����ǰ����
			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
				Log.d("action", "onPageScrollStateChanged - " + arg0);
				// ״̬�����0���У�1�����ڻ����У�2Ŀ��������
				/**
				 * Indicates that the pager is in an idle, settled state. The
				 * current page is fully in view and no animation is in
				 * progress.
				 */
				// public static final int SCROLL_STATE_IDLE = 0;
				/**
				 * Indicates that the pager is currently being dragged by the
				 * user.
				 */
				// public static final int SCROLL_STATE_DRAGGING = 1;
				/**
				 * Indicates that the pager is in the process of settling to a
				 * final position.
				 */
				// public static final int SCROLL_STATE_SETTLING = 2;

			}
		});

	}

	private class MyPagerAdapter extends PagerAdapter
	{

		// ���arg1λ�õĽ���
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2)
		{
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		// ��ȡ��ǰ���������
		@Override
		public int getCount()
		{
			return mListViews.size();
		}

		// ��ʼ��arg0λ�õĽ���
		@Override
		public Object instantiateItem(View arg0, int arg1)
		{
			Log.d("k", "instantiateItem");
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		// �ж��Ƿ��ɶ�����ɽ���
		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			Log.d("action", "isViewFromObject");
			return arg0 == (arg1);
		}

	}

}