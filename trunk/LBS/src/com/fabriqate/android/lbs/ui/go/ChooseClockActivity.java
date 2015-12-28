package com.fabriqate.android.lbs.ui.go;


import com.fabriqate.android.lbs.R;
import com.fabriqate.android.lbs.anim.CloseAppAnimation;
import com.fabriqate.android.lbs.anim.NextAnimation;
import com.fabriqate.android.lbs.anim.OpenAppAnimation;
import com.fabriqate.android.lbs.anim.PrevAnimation;
import com.fabriqate.android.lbs.anim.ShowAnimation;
import com.fabriqate.android.lbs.utils.LBSConfig;
import com.fabriqate.android.lbs.view.BackgroundRendererView;
import com.fabriqate.android.lbs.view.NavigationItem;
import com.fabriqate.android.lbs.view.NavigationRendererView;
import com.fabriqate.android.surface.IRendererInterface;
import com.fabriqate.android.surface.RendererView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

public class ChooseClockActivity extends Activity implements IRendererInterface {
	
    private int[] clockArray={5,180,150,120,90,60,45,30,25,20,15,10}; 
    
	private BackgroundRendererView mBackgroundRendererView = null;
	private NavigationRendererView mNavigationRendererView = null;
	
	private int index = 0;
	private boolean doOpen = false;
	private boolean back = false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        
        final Window window = getWindow();
       // window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		mBackgroundRendererView = new BackgroundRendererView(this);
		mNavigationRendererView = new NavigationRendererView(this);
		mBackgroundRendererView.setRendererInterface(this);
		mNavigationRendererView.setRendererInterface(this);
        this.addContentView(mBackgroundRendererView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));   
        this.addContentView(mNavigationRendererView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
        
         final ImageButton ibtnDone = new ImageButton(this);
        ibtnDone.setBackgroundResource(R.drawable.clock_done);
        ibtnDone.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        ibtnDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setAction(LBSConfig.ACTION_NEWFINDING_TIME);
				intent.putExtra("minutes", clockArray[index]);
				sendBroadcast(intent);

				finish();
			}
		});
         
         FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
         80,50);
         params.topMargin = 5;
         params.gravity = Gravity.RIGHT;
         params.rightMargin=30;
         this.addContentView(ibtnDone, params);
        
        
        mBackgroundRendererView.startRenderer();
		
		for(int i=0; i< 12; i++)
		{
		    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.b0 + i);
			NavigationItem item = new NavigationItem(bitmap,"MIN");
			mNavigationRendererView.addNavigationItem(item);
		}
    }

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		
	    if(mNavigationRendererView.doingRenderer() || mBackgroundRendererView.doingRenderer())
	    {
	        return true;
	    }
	    if(doOpen)
	    {
	    	mNavigationRendererView.doAnimation(new CloseAppAnimation());
	    	doOpen = false;
	 		return true;
	    }
	   
		if(keyCode == KeyEvent.KEYCODE_MENU)
		{
		   /* if(index == 0)
		    {
		        mBackgroundRendererView.startRenderer();
		        index ++;
		    }else  */
			
		/*	if(index >= 0 && back == false){
		        mNavigationRendererView.doAnimation(new NextAnimation());
		        index ++;
		        if(index >= 11)back = true;
		    }
		    else  if(index >= 1 && back){
                mNavigationRendererView.doAnimation(new PrevAnimation());
                index --;
                if(index <= 1){
                    back = false;
                    index = 1;
                }
            }
			return true;*/
		}else if(keyCode == KeyEvent.KEYCODE_ENTER && doOpen == false){
			mNavigationRendererView.doAnimation(new OpenAppAnimation());
			doOpen = true;
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	float firstX ;
	float firstY;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		int action = event.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			
			 firstX = event.getX();
			firstY=event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			if(event.getY()-firstY>0)
			{

				    mNavigationRendererView.doAnimation(new NextAnimation());
			 
			        index ++;
			        if(index>11)
					 {
						 index=0;
					 }
			}
			else if(event.getY()-firstY<0)
			{			 
				  mNavigationRendererView.doAnimation(new PrevAnimation());
				  index --;
				  if(index<0)
				    {
						index=11;
				   }	 
			}
			
			break;
		default:
			break;
		}
	   return true;
		//return super.onTouchEvent(event);
	}
	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		Intent intent=new Intent();
		intent.setClass(ChooseClockActivity.this, ColClockAdjustActivity.class);
		startActivity(intent);
		finish();
	}

	protected void onResume() {
		super.onResume();
	}
	protected void onStart() {
		super.onStart();
	}


	public void doRendererFinished(RendererView view, int tag) {
		if(view == mBackgroundRendererView)
		{
			mNavigationRendererView.doAnimation(new ShowAnimation());
		}else{
		}
		
		
	}
}