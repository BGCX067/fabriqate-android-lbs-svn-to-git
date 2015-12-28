package com.fabriqate.android.lbs.ui.go;

import java.util.Calendar;

import com.fabriqate.android.lbs.R;

import android.R.integer;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MyClockView extends View implements OnTouchListener{

	private static final String TAG = "MyClockView";
	

	private Bitmap mClockBitmap;	// 时钟底盘
	private Bitmap mHourBitmap;		// 时针
	private Bitmap mMinuteBitmap;	// 分针

	// 时钟的位置（相对于视图）
	private int clockX = 0, clockY = 0;	
	
	// 时钟中心点位置（相对于视图）
	private int clockCenterX = 0, clockCenterY = 0;
	
	// 指针指向12点钟方向时指针向下的偏移量
	private int mHourOffsetY = 0, mMinuteOffsetY = 0;
	
	// 时针位置（相对于时钟中心点）
	private int mHourPosX = 0, mHourPosY = 0;
	
	// 分针位置（相对于时钟中心点）
	private int mMinutePosX = 0, mMinutePosY = 0;
	
	// 是否初始化完毕
	private boolean bInitComplete = false;
	
	// 时钟当前时间
	private MyTime mCurTime;
	private boolean isLeft = false;
	private boolean isUsing = false;
	private ColClockAdjustActivity colClockAdjustActivity;
	
    static ImageView imageView;
    
/*	 Bitmap resizedBitmap ;
	 Bitmap bitmapOrg;
	
	 final int widthOrig ;
	 final int heightOrig ;
	 int newWidth ;
	 int newHeight;
		static float scaleWidth ;
		static float scaleHeight;
		 BitmapDrawable bitmapDrawable;*/
		 
		 
	public ColClockAdjustActivity getColClockAdjustActivity() {
		return colClockAdjustActivity;
	}

	public void setColClockAdjustActivity(
			ColClockAdjustActivity colClockAdjustActivity) {
		this.colClockAdjustActivity = colClockAdjustActivity;
	}

	public MyClockView(Context context,ImageView imageView) {
		super(context);
		// TODO Auto-generated constructor stub
		mCurTime = new MyTime();
		this.imageView=imageView;
		setOnTouchListener(this);
	  
/*		  bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.go_focus_bg);
	     widthOrig= bitmapOrg.getWidth(); 
	     heightOrig = bitmapOrg.getHeight(); 
		 newWidth = widthOrig * 1; 
		 newHeight = heightOrig * 1; 
		 scaleWidth = ((float) newWidth);
	     scaleHeight = ((float) newHeight)  ;*/
		
		
	    // resizedBitmap= scaleBitmap(bitmapOrg);

	    
	}
	
	/**
	 * @param clock
	 * @param hour
	 * @param minute
	 * 
	 * 初始化视图
	 */
	public void init(  Bitmap minute){
		
		mMinuteBitmap = minute;
		
		calcPointPosition();
		
		calcCenter();
		
		bInitComplete = true;
		
		
	}
	
	/**
	 * @param cx	中心点相对于时钟图片的X偏移量
	 * @param cy    中心点相对于时钟图片的Y偏移量
	 * 
	 * 设置时钟中心点位置（指针要绕着这个点旋转）
	 */
	public void setCenter(int cx, int cy){
		clockCenterX = clockX + cx;
		clockCenterY = clockY + cy;
	}
	
	/**
	 * @param hourOffset
	 * @param minuteOffset
	 * 
	 * 设置指针12点钟方向时向下的偏移量
	 */
	public void setPointOffset(int hourOffset, int minuteOffset){
		mHourOffsetY = hourOffset;
		mMinuteOffsetY = minuteOffset;
		
		calcPointPosition();
	}
	
	/**
	 * 计算时钟中心点位置（没有用setCenter设置时采用此默认值）
	 */
	public void calcCenter(){
		if (mClockBitmap != null){
			clockCenterX = clockX + mClockBitmap.getWidth()/2;
			clockCenterY = clockY + mClockBitmap.getHeight()/2;
		}
	}

	/**
	 * 计算指针位置
	 */
	public void calcPointPosition(){
		if (mHourBitmap != null){
			int w = mHourBitmap.getWidth();
			int h = mHourBitmap.getHeight();
			
			mHourPosX =  -w/2;
			mHourPosY = -h + mHourOffsetY;
			
		}
		
		if (mMinuteBitmap != null){
			int w = mMinuteBitmap.getWidth();
			int h = mMinuteBitmap.getHeight();
			
			mMinutePosX = -w/2;
			mMinutePosY = -h + mMinuteOffsetY;
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		
		//canvas.drawColor(Color.WHITE);
		
		if (!bInitComplete){
			return ;
		}
     
		
		drawMinute(canvas);
		
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		
	//	canvas.drawLine(0, clockCenterY, canvas.getWidth(), clockCenterY, paint);
		
		//canvas.drawLine(clockCenterX, 0, clockCenterX, canvas.getHeight(), paint);
	
	}

	/**
	 * @param canvas
	 * 绘制分针
	 */
	public void drawMinute(Canvas canvas){
		if (mMinuteBitmap == null){
			return ;
		}
		
		canvas.save();
		
		canvas.translate(clockCenterX, clockCenterY);
		

		canvas.rotate(mCurTime.mMinuteDegree,2,11);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		canvas.drawBitmap(mMinuteBitmap, mMinutePosX, mMinutePosY, paint);
		
		canvas.restore();
	}
	

	/**
	 * @author genius
	 * 管理当前时钟所表示的时间
	 */
	class MyTime{
		
		
		int mHour = 0;				
		int mMinute = 0;

		int mHourDegree = 0;				//  时针偏移量（相对于Y轴正半轴顺时针夹角，参考坐标系原点为时钟中心点，Y轴向上）
		int mMinuteDegree = 0;				//  分针偏移量（相对于Y轴正半轴顺时针夹角，参考坐标系原点为时钟中心点，Y轴向上）
		int mPreDegree = 0;					//  上次分针偏移量
		
		private Calendar mCalendar;

		
		/**
		 * 根据mHour，mMinute计算指针偏移量
		 */
		public void calcDegreeByTime(){
			mMinuteDegree = mMinute * 6;
			mPreDegree = mMinuteDegree;
			mHourDegree = (mHour % 12) * 30 + mMinuteDegree / 12;
		}
			/**
		 * @return
		 * ACTION_MOVE时判断是否为顺时针旋转
		 */
		public boolean deasil(){
			if (mMinuteDegree >= mPreDegree){
				if (mMinuteDegree - mPreDegree < 180){
					return true;
				}
				return false;
			}else{
				if (mPreDegree - mMinuteDegree > 180){
					return true;
				}
				
				return false;
			}
		}

	}


	/**
	 * @param x     	
	 * @param y
	 * @param flag			是否校正指针角度（ACTION_UP 时要校正）
	 * 
	 * 根据事件坐标更新表示时间
	 */
	public void calcDegree(int x, int y, boolean flag){
		int rx = x - clockCenterX;
		int ry = - (y - clockCenterY);
		
		Point point = new Point(rx, ry);
		if(140<x&& x < 370 && y < 470){
			mCurTime.mMinuteDegree = MyDegreeAdapter.GetRadianByPos(point);
			Log.d(TAG, "mCurTime.mMinuteDegree ="+mCurTime.mMinuteDegree );
			
			Log.d("action", "x = " + x);
			if(x < 240){
				isLeft = true;
				colClockAdjustActivity.lifttv(isLeft);
			}else{
				isLeft = false;
				colClockAdjustActivity.righttv(isLeft);
			}
		}
		if(flag){
			if(190 < x && x < 335&& 500 < y && y < 670){
				if(!isUsing){
					if(isLeft){
						
						Log.d("action", "111111111");
						colClockAdjustActivity.lift(isLeft);
					}else{
						 
						Log.d("action", "222222222");
						colClockAdjustActivity.right(isLeft);
					}
				}
			}
		}
	
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
			   if(event.getY()>100)
			   {
				   if(190 < event.getX() && event.getX() < 335&& 500 < event.getY() && event.getY()< 670)
				   {
					   
					   this.imageView.setVisibility(View.VISIBLE);
				   }
			
					calcDegree((int)event.getX(), (int)event.getY(), false);
					postInvalidate();
					Log.v(TAG, "ACTION_DOWN "+event.getY());
					

					//break;
					return true;
			   }
			
			case MotionEvent.ACTION_MOVE:
				 if(event.getY()>100)
				   {
				     this.imageView.setVisibility(View.GONE);
					calcDegree((int)event.getX(), (int)event.getY(), false);
					postInvalidate();
					
					Log.v(TAG, "ACTION_MOVE "+event.getY());
					//break;

					/* //旋转图片动作
					 matrix.postRotate(mCurTime.mMinuteDegree);
					 //创建新的图片
					 
					 if(null!=resizedBitmap&&!resizedBitmap.isRecycled())
					 {
						 resizedBitmap.recycle();
					 }
				
					 resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, widthOrig, heightOrig, matrix, true);
					 
				
					 //将上面创建的Bitmap转换成Drawable对象，使得其可以使用在imageView，imageButton上。
					  bitmapDrawable = new BitmapDrawable(resizedBitmap);
					 //将imageView的图片设置为上面转换的图片
					  this.imageView.setImageDrawable(bitmapDrawable);*/
					
					
				//	Bitmap scale=	scaleBitmap(bitmapOrg);
					  
				/*	Bitmap bm=rotateBitmap(resizedBitmap,mCurTime.mMinuteDegree);
					this.imageView.setImageBitmap(bm);
					 imageView.setScaleType(ScaleType.CENTER); 
					 if(bm!=null)
					 {
						 bm.recycle();
					 }*/
					return true;
					
				   }
			case MotionEvent.ACTION_UP:
				if(event.getY()>100)
				{
				
					calcDegree((int)event.getX(), (int)event.getY(), true);
					postInvalidate();
				
				 if(190 < event.getX() && event.getX() < 335&& 500 < event.getY() && event.getY()< 670)
				 {
					   
					 this.imageView.setVisibility(View.GONE);
			      }
					Log.v(TAG, "ACTION_UP "+event.getY());
					
					//break;
					return true;
				}
		}
	
		return super.onTouchEvent(event);
		//return false;
	}

/*	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
	}
	*/
	
/*	   public static Bitmap rotateBitmap(Bitmap bmp, float degree) {  
	       // Matrix matrix = new Matrix();
	        Matrix matrix = imageView.getImageMatrix();
	       // matrix.postScale(bmp.getWidth(), bmp.getHeight());
	        matrix.postRotate(degree);  
	        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);  
	    }  
	   public static Bitmap scaleBitmap(Bitmap bmp) {  
	      //  Matrix matrix = new Matrix();  
	        Matrix matrix = imageView.getImageMatrix();
	        matrix.postScale(80, 80);
	        Bitmap temp = Bitmap.createBitmap(bmp, 0, 0, 80, 80, matrix, true);
	        if(bmp != null){
	        	bmp.recycle();
	        	bmp = null;
	        }
	        return temp;  
	    }*/

}
