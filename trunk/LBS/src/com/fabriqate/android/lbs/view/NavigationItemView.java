package com.fabriqate.android.lbs.view;

import android.graphics.Canvas;


import com.fabriqate.android.lbs.anim.CloseAppAnimation;
import com.fabriqate.android.lbs.anim.OpenAppAnimation;
import com.fabriqate.android.lbs.anim.RendererAnimation;
import com.fabriqate.android.lbs.ui.go.AnimationConfig;

public class NavigationItemView {


    private NavigationItem mNavigationItem = null;
    private int mPosition = 0;
    
    public NavigationItemView(NavigationItem item)
    {
        mNavigationItem = item;
    }
    
    public int getHeight()
	{
		return mNavigationItem.getHeight();
	}
	
	public int getWidth()
	{
		return mNavigationItem.getWidth();
	}
    
    public void setPosition(int position)
    {
        mPosition = position;
    }
    
    public void draw(Canvas canvas,RendererAnimation animation)
    {
        animation.readyFrame(mPosition);
        int count = canvas.save();
        mNavigationItem.drawTitle(canvas, animation.getTitleVcenter(), animation.getTitleAlpha());
        if(mPosition == AnimationConfig.SHOW_ITEM_COUNT / 2 && (animation.getClass() == OpenAppAnimation.class
        		|| animation.getClass() == CloseAppAnimation.class)){
        	mNavigationItem.draw(canvas, animation.getAlpha(), animation.getRotate(),animation.getRadius(), animation.getRect());
        }else{
        	mNavigationItem.draw(canvas, animation.getScale(), animation.getAlpha(), animation.getRadius(),animation.getRotate());
        }
        canvas.restoreToCount(count);
    }
    
}
