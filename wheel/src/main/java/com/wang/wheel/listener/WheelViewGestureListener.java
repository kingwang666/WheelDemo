package com.wang.wheel.listener;

import android.view.MotionEvent;

import com.wang.wheel.widget.IOSWheelView;

public class WheelViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    final IOSWheelView loopView;

    public WheelViewGestureListener(IOSWheelView loopview) {
        loopView = loopview;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        loopView.scrollBy(velocityY);
        return true;
    }
}
