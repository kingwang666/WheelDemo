package com.wang.wheeldemo;

import android.widget.Toast;

import com.wang.wheel.listener.OnButtonClickListener;
import com.wang.wheel.model.DataModel;
import com.wang.wheel.widget.CharacterPickerView;

/**
 * Created on 2017/5/18.
 * Author: wang
 */

public class MultiPickActivity extends BaseWheelActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_multi_pick;
    }

    @Override
    protected void afterView() {
        CharacterPickerView pickerView = (CharacterPickerView) findViewById(R.id.picker_view);
        pickerView.setDefault(20, 0, 0);
        pickerView.setAllData(mLocations);
        pickerView.setButtonClickListener(new OnButtonClickListener() {
            @Override
            public void onClick(DataModel province, DataModel city, DataModel area) {
                Toast.makeText(MultiPickActivity.this, province.Name + " " + city.Name + " " + area.Name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MultiPickActivity.this, "cancel", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
