package kankan.wheel.widget.lintener;

import android.view.MotionEvent;

import kankan.wheel.widget.wheel2.WheelView2;

public class LoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    final WheelView2 loopView;

    public LoopViewGestureListener(WheelView2 loopview) {
        loopView = loopview;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        loopView.scrollBy(velocityY);
        return true;
    }
}
