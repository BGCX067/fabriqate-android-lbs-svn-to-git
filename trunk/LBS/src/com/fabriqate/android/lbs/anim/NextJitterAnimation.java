package com.fabriqate.android.lbs.anim;

import com.fabriqate.android.lbs.ui.go.AnimationConfig;


public class NextJitterAnimation extends RendererAnimation {

	private int[] ROTATE_ANIMATION  = {1,2,1,0,0};
	
	public NextJitterAnimation()
    {
        mStep = 0;
        mMaxStep = ROTATE_ANIMATION.length - 1;
    }
    
    public void readyFrame(int position) {
    	mRotate = position * AnimationConfig.ITEM_ANGLE + ROTATE_ANIMATION[mStep];
        mAlpha = (int) (255 *  (1.0f - Math.abs(position * AnimationConfig.ITEM_ANGLE - 90.0f) / 90.0f));
        mScale = RendererAnimation.getScale(position * AnimationConfig.ITEM_ANGLE);
        
        if(position == AnimationConfig.SHOW_ITEM_COUNT / 2){
    		mTitleVcenter = 0;
    		mTitleAlpha = 255;
    	}else{
    		mTitleVcenter = 0;
    		mTitleAlpha   = 0;
    	}
    }

}
