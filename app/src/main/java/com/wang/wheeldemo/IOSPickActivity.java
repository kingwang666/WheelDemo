package com.wang.wheeldemo;

import com.wang.wheel.adapters.ArrayWheelAdapter;
import com.wang.wheel.widget.IOSWheelView;

/**
 * Created on 2017/5/18.
 * Author: wang
 */

public class IOSPickActivity extends BaseWheelActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ios_pick;
    }

    @Override
    protected void afterView() {
        IOSWheelView wheelView = (IOSWheelView) findViewById(R.id.picker_view);
        wheelView.setAdapter(new ArrayWheelAdapter<>(this, mProvinceDatas));
    }
}
