package com.wang.wheel.listener;

import com.wang.wheel.model.DataModel;

/**
 * Created by wang
 * on 2016/12/6
 */

public interface OnButtonClickListener {
    void onClick(DataModel province, DataModel city, DataModel area);

    void onCancel();
}
