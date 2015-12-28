package com.fabriqate.android.lbs.anim;


import com.fabriqate.android.lbs.ui.go.AnimationConfig;

import android.graphics.RectF;

public abstract class RendererAnimation {
    
    public interface AnimationNotification
    {
        public void doAnimation(RendererAnimation animation,int step,int maxStep);
    }
    
    
    
    protected int    mStep     = 0;  //��ǰ֡��
    protected int    mMaxStep  = 0;  //���֡��
    protected float  mRotate   = 0f;  //����ͼƬ��ת�Ƕ�
    protected float  mRadius   = AnimationConfig.RADIUS; //����ͼƬ������Բ���ĵľ���
    protected float  mScale    = 100f;                     //����ͼƬ��С�ı���
    protected int    mAlpha    = 0;                      //����ͼƬ��͸����
    protected int    mTitleBackgroundAlpha = 255;        //���ⱳ��ͼƬ��͸����
    protected RectF  mRect     = new RectF();            //����ͼƬ�Ļ��ƾ��ο���OpenAppAnimation �� CloseAppAnimation���õ�
    protected int    mTitleAlpha   = 0;
	protected float  mTitleVcenter = 0;
    
    protected AnimationNotification mNotification = null;

    public RendererAnimation()
    {
    	mTitleBackgroundAlpha = 255;
    }

    public void setAnimationNotification(AnimationNotification notification)
    {
        mNotification = notification;
    }
    
    public void nextFrame()
    {
        mStep ++;
        if(mStep >= mMaxStep) mStep = mMaxStep;
        if(mNotification != null) {
            mNotification.doAnimation(this,mStep, mMaxStep);
        }
    }
    
    public abstract void readyFrame(int position);

    
    public boolean isEnd()
    {
        return (mStep >= mMaxStep) || (mStep < 0);
    }

    public int getTitleBackgroundAlpha() {
        return mTitleBackgroundAlpha;
    }
    public float getRadius() {
        return mRadius;
    }

    public float getRotate() {
        return mRotate;
    }

    public float getScale() {
        return mScale;
    }

    public int getAlpha() {
        if(mAlpha < 0 || mAlpha > 255) return 0;
        return mAlpha;
    }
    
    public int getTitleAlpha() {
		return mTitleAlpha;
	}

	public float getTitleVcenter() {
		return mTitleVcenter;
	}

    public RectF getRect() {
        return mRect;
    }
    
    
    private static float UNSELECT_MAX_SCALE = 90;
    private static float UNSELECT_MIN_SCALE = 80;
    
    private static float SELECTED_MAX_SCALE = 100;
    private static float SELECTED_MIN_SCALE = 90;
    
    public static float getScale(float rotate)
    {
        if(Math.abs(rotate - 90) < 18)
        {
            float maxValue = 18;
            float curValue = Math.abs(rotate - 90);
            float scale = 1.0f - curValue / maxValue;
            return (SELECTED_MIN_SCALE + (SELECTED_MAX_SCALE - SELECTED_MIN_SCALE) *  scale) / 100.0f;
        }else{
            float maxValue = 90 - 18;
            float curValue = Math.abs(Math.abs(rotate - 90) - 18);
            float scale = 1.0f - curValue / maxValue;
            return (UNSELECT_MIN_SCALE + (UNSELECT_MAX_SCALE - UNSELECT_MIN_SCALE) *  scale) / 100.0f;
        }
    }
}
