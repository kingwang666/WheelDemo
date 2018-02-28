package com.wang.wheel.widget;

import java.util.TimerTask;

final class InertiaTimerTask extends TimerTask {

    private float max;
    private final float velocityY;
    private final IOSWheelView loopView;

    InertiaTimerTask(IOSWheelView loopview, float velocityY) {
        super();
        loopView = loopview;
        this.velocityY = velocityY;
        max = Integer.MAX_VALUE;
    }

    @Override
    public final void run() {
        if (max == Integer.MAX_VALUE) {
            if (Math.abs(velocityY) > 2000F) {
                if (velocityY > 0.0F) {
                    max = 2000F;
                } else {
                    max = -2000F;
                }
            } else {
                max = velocityY;
            }
        }
        if (Math.abs(max) >= 0.0F && Math.abs(max) <= 20F) {
            loopView.cancelFuture();
            loopView.handler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
            return;
        }
        int i = (int) ((max * 10F) / 1000F);
        loopView.totalScrollY = loopView.totalScrollY - i;
        if (!loopView.isCyclic) {
            float itemHeight = loopView.itemHeight;
            float top = (-loopView.initPosition) * itemHeight;
            float bottom = (loopView.getItemsCount() - 1 - loopView.initPosition) * itemHeight;
            if(loopView.totalScrollY - itemHeight*0.3 < top){
                top = loopView.totalScrollY + i;
            }
            else if(loopView.totalScrollY + itemHeight*0.3 > bottom){
                bottom = loopView.totalScrollY + i;
            }

            if (loopView.totalScrollY <= top){
                max = 40F;
                loopView.totalScrollY = (int)top;
            } else if (loopView.totalScrollY >= bottom) {
                loopView.totalScrollY = (int)bottom;
                max = -40F;
            }
        }
        if (max < 0.0F) {
            max = max + 20F;
        } else {
            max = max - 20F;
        }
        loopView.handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
    }

}
