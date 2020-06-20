package com.flys.architecture.core;

import android.content.Context;

import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;
import android.view.MotionEvent;

import com.flys.service.SwipeDirection;

public class MyPager extends ViewPager {

    // contrôle le swipe
    private boolean isSwipeEnabled;
    // contrôle le scrolling
    private boolean isScrollingEnabled;
    //Direction
    private SwipeDirection direction;
    private float initialXValue;

    // constructeurs
    public MyPager(Context context) {
        super(context);
    }

    public MyPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.direction = SwipeDirection.all;
    }

    // méthodes à redéfinir pour gérer le swipe
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // swipe autorisé ?
        if (this.IsSwipeAllowed(event)) {
            return super.onInterceptTouchEvent(event);
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // swipe autorisé ?
        if (this.IsSwipeAllowed(event)) {
            return super.onTouchEvent(event);
        } else {
            return false;
        }
    }

    // contrôle du scrolling
    @Override
    public void setCurrentItem(int position) {
        super.setCurrentItem(position, isScrollingEnabled);
    }

    // setters
    public void setSwipeEnabled(boolean isSwipeEnabled) {
        this.isSwipeEnabled = isSwipeEnabled;
    }

    public void setScrollingEnabled(boolean scrollingEnabled) {
        isScrollingEnabled = scrollingEnabled;
    }

    private boolean IsSwipeAllowed(MotionEvent event) {
        if (this.direction == SwipeDirection.all) return true;

        if (direction == SwipeDirection.none)//disable any swipe
            return false;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            initialXValue = event.getX();
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            try {
                float diffX = event.getX() - initialXValue;
                if (diffX > 0 && direction == SwipeDirection.right) {
                    // swipe from left to right detected
                    return false;
                } else if (diffX < 0 && direction == SwipeDirection.left) {
                    // swipe from right to left detected
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }

    public void setAllowedSwipeDirection(SwipeDirection direction) {
        this.direction = direction;
    }

}