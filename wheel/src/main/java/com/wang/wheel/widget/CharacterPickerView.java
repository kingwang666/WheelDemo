package com.wang.wheel.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SparseArrayCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.LinkedList;
import java.util.List;

import kankan.wheel.R;
import com.wang.wheel.adapters.ArrayWheelAdapter;
import com.wang.wheel.listener.OnButtonClickListener;
import com.wang.wheel.listener.OnWheelChangedListener;
import com.wang.wheel.model.AllLocationsMode;
import com.wang.wheel.model.CityModel;
import com.wang.wheel.model.DataModel;

/**
 * Created by wang
 * on 2016/4/12
 */
public class CharacterPickerView extends LinearLayout implements OnWheelChangedListener, View.OnClickListener {

    /**
     * 取消按钮
     */
    private Button mCancel;
    /**
     * 确定按钮
     */
    private Button mConfirm;
    /**
     * 省的WheelView控件
     */
    private WheelView mProvince;
    /**
     * 市的WheelView控件
     */
    private WheelView mCity;
    /**
     * 区的WheelView控件
     */
    private WheelView mArea;

    /**
     * 所有省
     */
    private List<DataModel> mProvinceDatas = new LinkedList<>();

    /**
     * key - 省 value - 市s
     */
    private SparseArrayCompat<List<DataModel>> mCitisDatasMap = new SparseArrayCompat<>();
    /**
     * key - 市 values - 区s
     */
    private SparseArrayCompat<List<DataModel>> mAreaDatasMap = new SparseArrayCompat<>();
    /**
     * 省adapter
     */
    private ArrayWheelAdapter mProvinceAdapter;
    /**
     * 市adapter
     */
    private ArrayWheelAdapter mCityAdapter;
    /**
     * 区adapter
     */
    private ArrayWheelAdapter mAreaAdapter;


    /**
     * 当前省的名称
     */
    private DataModel mCurrentProvince = new DataModel(0, "");

    /**
     * 当前市的名称
     */
    private DataModel mCurrentCity = new DataModel(0, "");
    /**
     * 当前区的名称
     */
    private DataModel mCurrentArea = new DataModel(0, "");
    /**
     * 默认选择
     */
    private int mProvinceId = -1;
    private int mProvincePosition = 0;
    /**
     * 默认选择
     */
    private int mCityId = -1;
    private int mCityPosition = 0;
    /**
     * 默认选择
     */
    private int mAreaId = -1;

    private int mAreaPosition = 0;
    /**
     * 几级联动
     */
    private int mListCount;


    private OnButtonClickListener listener;


    public CharacterPickerView(Context context) {
        super(context);
        init(context, null);
    }

    public CharacterPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CharacterPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CharacterPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.provice_city_area, this, true);

        mCancel = (Button) findViewById(R.id.cancel_btn);
        mConfirm = (Button) findViewById(R.id.confirm_btn);
        mProvince = (WheelView) findViewById(R.id.id_province);
        mCity = (WheelView) findViewById(R.id.id_city);
        mArea = (WheelView) findViewById(R.id.id_area);
        /**
         * 是否有顶部和底部阴影
         */
        boolean drawShadows = true;

        int startColor = 0;
        int centerColor = 0;
        int endColor = 0;

        int wheelBg = 0;
        int dividerColor = 0;
        boolean dividerEnable = true;

        int defaultColor = 0;
        int selectColor = 0;
        int textSize = 0;
        boolean isCycle = false;

        int cancelTextBg = 0;
        int confirmTextBg = 0;

        int visibleItems = 3;

        String cancelText = null;

        String confirmText = null;

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CharacterPickerView, 0, 0);
        try {
            drawShadows = typedArray.getBoolean(R.styleable.CharacterPickerView_cp_drawShadows, true);
            startColor = typedArray.getColor(R.styleable.CharacterPickerView_cp_shadowsColorStart, ContextCompat.getColor(context, R.color.wheel_start_color));
            centerColor = typedArray.getColor(R.styleable.CharacterPickerView_cp_shadowsColorCenter, ContextCompat.getColor(context, R.color.wheel_center_color));
            endColor = typedArray.getColor(R.styleable.CharacterPickerView_cp_shadowsColorEnd, ContextCompat.getColor(context, R.color.wheel_end_color));

            wheelBg = typedArray.getResourceId(R.styleable.CharacterPickerView_cp_wheelBackground, R.color.transparent);
            dividerColor = typedArray.getColor(R.styleable.CharacterPickerView_cp_dividerColor, ContextCompat.getColor(context, R.color.wheel_color_divider));
            dividerEnable = typedArray.getBoolean(R.styleable.CharacterPickerView_cp_dividerEnable, true);

            cancelTextBg = typedArray.getResourceId(R.styleable.CharacterPickerView_cp_cancelTextColor, R.drawable.wheel_btn_bg);
            cancelText = typedArray.getString(R.styleable.CharacterPickerView_cp_cancelText);
            confirmTextBg = typedArray.getResourceId(R.styleable.CharacterPickerView_cp_confirmTextColor, R.drawable.wheel_btn_bg);
            confirmText = typedArray.getString(R.styleable.CharacterPickerView_cp_confirmText);

            defaultColor = typedArray.getColor(R.styleable.CharacterPickerView_cp_textColorUnselected, ContextCompat.getColor(context, R.color.wheel_text_color_out));
            selectColor = typedArray.getColor(R.styleable.CharacterPickerView_cp_textColorSelected, ContextCompat.getColor(context, R.color.wheel_text_color_center));
            textSize = typedArray.getDimensionPixelSize(R.styleable.CharacterPickerView_cp_textSize, getResources().getDimensionPixelSize(R.dimen.default_size));

            isCycle = typedArray.getBoolean(R.styleable.CharacterPickerView_cp_cycle, false);
            mListCount = typedArray.getInteger(R.styleable.CharacterPickerView_cp_listCount, 3);
            visibleItems = typedArray.getInteger(R.styleable.CharacterPickerView_cp_visibleItems, visibleItems);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            typedArray.recycle();
        }

        initWheel(drawShadows, startColor, centerColor, endColor, dividerColor, dividerEnable, wheelBg, defaultColor, selectColor, isCycle, visibleItems, textSize);
        initButton(cancelTextBg, cancelText, confirmTextBg, confirmText);
    }

    private void initButton(int cancelTextBg, String cancelText, int confirmTextBg, String confirmText) {
        mConfirm.setOnClickListener(this);
        mConfirm.setTextColor(ContextCompat.getColorStateList(getContext(), confirmTextBg));
        if (!TextUtils.isEmpty(confirmText)) {
            mConfirm.setText(confirmText);
        }
        mCancel.setOnClickListener(this);
        mCancel.setTextColor(ContextCompat.getColorStateList(getContext(), cancelTextBg));
        if (!TextUtils.isEmpty(cancelText)) {
            mCancel.setText(cancelText);
        }
    }

    private void initWheel(boolean drawShadows, int startColor, int centerColor, int endColor, int dividerColor, boolean dividerEnable, int bg, int defaultColor, int selectColor, boolean isCycle, int visibleItems, int textSize) {
        setWheelDrawShadows(drawShadows);
        if (drawShadows) {
            setShadows(startColor, centerColor, endColor);
        }
        setTextSize(textSize);
        setColorUnselected(defaultColor);
        setColorSelected(selectColor);
        mProvince.addChangingListener(this);
        mCity.addChangingListener(this);
        mArea.addChangingListener(this);
        setDividerColor(dividerColor);
        setDividerEnable(dividerEnable);
        setWheelBackground(bg);
        setVisibleItem(visibleItems);
        setListCount(mListCount);
        setCycle(isCycle);

    }

    /**
     * 设置是否循环
     *
     * @param cycle 循环
     */
    public void setCycle(boolean cycle) {
        mProvince.setCyclic(cycle);
        mCity.setCyclic(cycle);
        mArea.setCyclic(cycle);
    }

    /**
     * 设置选中项颜色
     *
     * @param colorSelected 颜色
     */
    public void setColorSelected(int colorSelected) {
        mProvince.setColorSelected(colorSelected);
        mCity.setColorSelected(colorSelected);
        mArea.setColorSelected(colorSelected);
    }

    /**
     * 设置非选中项颜色
     *
     * @param colorUnselected 颜色
     */
    public void setColorUnselected(int colorUnselected) {
        mProvince.setColorUnselected(colorUnselected);
        mCity.setColorUnselected(colorUnselected);
        mArea.setColorUnselected(colorUnselected);
    }


    /**
     * 设置字体大小
     *
     * @param textSize 设置字体大小
     */
    public void setTextSize(int textSize) {
        mProvince.setTextSize(textSize);
        mCity.setTextSize(textSize);
        mArea.setTextSize(textSize);

    }

    public int getTextSize() {
        return mProvince.getTextSize();
    }

    public boolean isCycle() {
        return mProvince.isCyclic();
    }

    /**
     * 设置表层模糊渐变
     *
     * @param drawShadows 是否需要
     */
    public void setWheelDrawShadows(boolean drawShadows) {
        mProvince.setDrawShadows(drawShadows);
        mCity.setDrawShadows(drawShadows);
        mArea.setDrawShadows(drawShadows);
    }

    public boolean getWheelDrawShadows() {
        return mProvince.isDrawShadows();
    }

    /**
     * 设置表层渐变颜色
     *
     * @param start  开始颜色
     * @param center 中间颜色
     * @param end    结束颜色
     */
    public void setShadows(int start, int center, int end) {
        mProvince.setShadowColor(start, center, end);
        mCity.setShadowColor(start, center, end);
        mArea.setShadowColor(start, center, end);
    }

    public void setDividerColor(int dividerColor) {
        mProvince.setDividerColor(dividerColor);
        mCity.setDividerColor(dividerColor);
        mArea.setDividerColor(dividerColor);
    }

    public void setDividerEnable(boolean dividerEnable) {
        mProvince.setDividerEnable(dividerEnable);
        mCity.setDividerEnable(dividerEnable);
        mArea.setDividerEnable(dividerEnable);
    }


    /**
     * 设置背景
     *
     * @param resId 背景id
     */
    public void setWheelBackground(int resId) {
        mProvince.setBackgroundResource(resId);
        mCity.setBackgroundResource(resId);
        mArea.setBackgroundResource(resId);

    }


    /**
     * 设置可见项数目
     *
     * @param count 数目
     */
    public void setVisibleItem(int count) {
        mProvince.setVisibleItems(count);
        mCity.setVisibleItems(count);
        mArea.setVisibleItems(count);
    }

    public int getVisibleItem() {
        return mProvince.getVisibleItems();
    }

    /**
     * 设置联动链表数目 最大3个， 最小1个
     *
     * @param count 数目
     */
    public void setListCount(int count) {
        if (count < 2 || count > 3) {
            throw new IllegalStateException("this list count must is 2 or 3");
        }
        mListCount = count;
        switch (count) {
            case 2:
                mCity.setVisibility(VISIBLE);
                mArea.setVisibility(GONE);
                break;
            case 3:
                mCity.setVisibility(VISIBLE);
                mArea.setVisibility(VISIBLE);
                break;
        }
    }

    public int getListCount() {
        return mListCount;
    }

    /**
     * 设置第一个列表数据
     *
     * @param province
     */
    public void setProvince(List<DataModel> province) {
        mProvinceDatas = province;
        mProvinceAdapter = new ArrayWheelAdapter<>(getContext(), mProvinceDatas);
        for (int i = 0; i < mProvinceDatas.size(); i++) {
            DataModel data = mProvinceDatas.get(i);
            if (data.Id == mProvinceId) {
                mProvincePosition = i;
                break;
            }
        }
        mProvince.setAdapter(mProvinceAdapter);
        int pCurrent = mProvince.getCurrentItem();
        mCurrentProvince = mProvinceDatas.get(pCurrent);
        mProvince.setCurrentItem(mProvincePosition);
    }

    /**
     * 设置第二个列表数据
     *
     * @param city
     */
    public void setCity(SparseArrayCompat<List<DataModel>> city) {
        mCitisDatasMap = city;
        List<DataModel> temp_city = city.get(mProvinceId);
        if (temp_city != null) {
            for (int i = 0; i < temp_city.size(); i++) {
                DataModel data = temp_city.get(i);
                if (data.Id == mCityId) {
                    mCityPosition = i;
                    break;
                }
            }
        }
        updateCities(mCityPosition);
//        mCity.setCurrentItem(mCityPosition);
    }

    /**
     * 设置第三个列表数据
     *
     * @param area
     */
    public void setArea(SparseArrayCompat<List<DataModel>> area) {
        mAreaDatasMap = area;
        List<DataModel> temp_area = area.get(mCityId);
        if (temp_area != null) {
            for (int i = 0; i < temp_area.size(); i++) {
                DataModel data = temp_area.get(i);
                if (data.Id == mAreaId) {
                    mAreaPosition = i;
                    break;
                }
            }
        }
        updateAreas(mAreaPosition);
//        mArea.setCurrentItem(mAreaPosition);
    }

    /**
     * 设置默认选中
     *
     * @param provinceId 第一项id
     * @param cityId     第二项id
     * @param areaId     第三项id
     */
    public void setDefault(int provinceId, int cityId, int areaId) {
        mProvinceId = provinceId;
        mCityId = cityId;
        mAreaId = areaId;
    }


    /**
     * 设置所有数据
     *
     * @param locations
     */
    public void setAllData(List<AllLocationsMode> locations) {
        for (AllLocationsMode location : locations) {
            mProvinceDatas.add(new DataModel(location.Id, location.Name));
            List<CityModel> cities = location.Child;
            List<DataModel> temp_cities = new LinkedList<>();
            if (cities != null) {
                for (CityModel city : cities) {
                    temp_cities.add(new DataModel(city.Id, city.Name));
                    mAreaDatasMap.put(city.Id, city.Child);
                }
                mCitisDatasMap.put(location.Id, temp_cities);
            }
        }
        setProvince(mProvinceDatas);
        setCity(mCitisDatasMap);
        setArea(mAreaDatasMap);
    }

    /**
     * 设置按钮监听
     *
     * @param listener
     */
    public void setButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onChanged(View wheel, int oldValue, int newValue) {
        if (wheel == mProvince) {
            updateCities(0);
        } else if (wheel == mCity) {
            updateAreas(0);
        } else if (wheel == mArea) {
            mCurrentArea = new DataModel(0, "");
            if (!TextUtils.isEmpty(mCurrentCity.Name)) {
                List<DataModel> areas = mAreaDatasMap.get(mCurrentCity.Id);
                if (areas != null && areas.size() > 0) {
                    mCurrentArea = areas.get(newValue);
                }
            }
        }

    }


    /**
     * 更新第三项
     */
    private void updateAreas(int position) {
        int pCurrent = mCity.getCurrentItem();
        List<DataModel> cities = mCitisDatasMap.get(mCurrentProvince.Id);
        List<DataModel> areas;
        if (cities == null) {
            cities = new LinkedList<>();
            cities.add(new DataModel(0, ""));
            areas = new LinkedList<>();
            areas.add(new DataModel(0, ""));
            mCurrentCity = cities.get(pCurrent);
        } else {
            mCurrentCity = cities.get(pCurrent);
            areas = mAreaDatasMap.get(mCurrentCity.Id);
            if (areas == null) {
                areas = new LinkedList<>();
                areas.add(new DataModel(0, ""));
            }
        }
        if (mListCount > 2) {
            mCurrentArea = areas.get(position);
            mAreaAdapter = new ArrayWheelAdapter<>(getContext(), areas);
            mArea.setAdapter(mAreaAdapter);
            mArea.setCurrentItem(position);

        }
    }


    /**
     * 更新第二项
     */
    private void updateCities(int position) {
        int pCurrent = mProvince.getCurrentItem();
        mCurrentProvince = mProvinceDatas.get(pCurrent);
        List<DataModel> cities = mCitisDatasMap.get(mCurrentProvince.Id);
        if (cities == null) {
            cities = new LinkedList<>();
            cities.add(new DataModel(0, ""));
        }
        mCityAdapter = new ArrayWheelAdapter<>(getContext(), cities);
        mCity.setAdapter(mCityAdapter);
        mCity.setCurrentItem(position);
        updateAreas(0);

    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            if (v.getId() == R.id.confirm_btn) {
                listener.onClick(mCurrentProvince, mCurrentCity, mCurrentArea);
            } else if (v.getId() == R.id.cancel_btn) {
                listener.onCancel();
            }
        }
    }

}

