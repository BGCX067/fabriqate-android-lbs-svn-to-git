package com.fabriqate.android.lbs.anim;

import com.fabriqate.android.lbs.ui.go.AnimationConfig;


public class ShowAnimation extends RendererAnimation{
    
	
    public ShowAnimation()
    {
        mStep = 0;
        mMaxStep = 18;
        mTitleBackgroundAlpha = 0;
    }
    
    
    public void readyFrame(int position)
    {
        mRotate = Math.min(mStep * 10,position * AnimationConfig.ITEM_ANGLE);
        mAlpha = (int) (255 *  (1.0f - Math.abs(position * AnimationConfig.ITEM_ANGLE - 90.0f) / 90.0f));
        mScale = RendererAnimation.getScale(position * AnimationConfig.ITEM_ANGLE);
        
        //��֡�������֡������֮һ����ʼ��ʾ���ⱳ��
        if(mStep > mMaxStep / 3){
        	mTitleBackgroundAlpha = 255 * (mStep - mMaxStep / 3) / (mMaxStep - mMaxStep / 3);
        	if(position == AnimationConfig.SHOW_ITEM_COUNT / 2){
        		mTitleVcenter = 0;
        		mTitleAlpha = mTitleBackgroundAlpha;
        	}else{
        		mTitleVcenter = 0;
        		mTitleAlpha   = 0;
        	}
        }
    }

}
