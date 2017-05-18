package com.wang.wheeldemo;

import com.wang.wheel.adapters.ArrayWheelAdapter;
import com.wang.wheel.widget.WheelView;

/**
 * Created on 2017/5/18.
 * Author: wang
 */

public class PickActivity extends BaseWheelActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_pick;
    }

    @Override
    protected void afterView() {
        WheelView wheelView = (WheelView) findViewById(R.id.picker_view);
        wheelView.setAdapter(new ArrayWheelAdapter<>(this, mProvinceDatas));
    }
}
