package com.muhanbit.sycrethealth;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by hwjoo on 2017-01-18.
 */

/*
 * CustomViewPager를 사용하려했으나, control할 필요가 없어 사용 안함
 * 하지만, 기록용으로 남겨두고, ViewPager를 상속받았기때문에 모든 기능이 동일함으로,
 * 이 Project에서 CustomViewPager로 사용.
 */

public class CustomViewPager extends ViewPager {
    private boolean swipeEnabled;

    public CustomViewPager(Context context) {
        super(context);
        swipeEnabled = true;
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        swipeEnabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(this.swipeEnabled){
            return super.onTouchEvent(ev);
        }
        return false;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.swipeEnabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setSwipeEnabled(boolean enabled) {
        this.swipeEnabled = enabled;
    }
}
