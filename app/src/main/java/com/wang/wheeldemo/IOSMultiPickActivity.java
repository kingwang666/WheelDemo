package com.wang.wheeldemo;

import android.widget.Toast;

import com.wang.wheel.listener.OnButtonClickListener;
import com.wang.wheel.model.DataModel;
import com.wang.wheel.widget.IOSCharacterPickerView;

/**
 * Created on 2017/5/18.
 * Author: wang
 */

public class IOSMultiPickActivity extends BaseWheelActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ios_multi_pick;
    }

    @Override
    protected void afterView() {
        IOSCharacterPickerView pickerView = (IOSCharacterPickerView) findViewById(R.id.picker_view);
        pickerView.setDefault(20, 20002, 1320);
        pickerView.setProvince(mProvinceDatas);
        pickerView.setCity(mCitisDatasMap);
        pickerView.setArea(mAreaDatasMap);
        pickerView.setButtonClickListener(new OnButtonClickListener() {
            @Override
            public void onClick(DataModel province, DataModel city, DataModel area) {
                Toast.makeText(IOSMultiPickActivity.this, province.Name + " " + city.Name + " " + area.Name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(IOSMultiPickActivity.this, "cancel", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
