package kankan.wheel.widget.provinceCityArea;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kankan.wheel.R;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.model.AllLocationsMode;
import kankan.wheel.widget.model.CityModel;
import kankan.wheel.widget.model.DataModel;

/**
 * Created by jiudeng009 on 2016/4/12.
 */
public class CharacterPickerView extends FrameLayout implements OnWheelChangedListener, View.OnClickListener {

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

    private List<String> mOnlyOneListDatas;
    /**
     * key - 省 value - 市s
     */
    private Map<String, List<DataModel>> mCitisDatasMap = new HashMap<>();
    /**
     * key - 市 values - 区s
     */
    private Map<String, List<DataModel>> mAreaDatasMap = new HashMap<>();
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

    private String mOneData;
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
     * 是否有顶部和底部阴影
     */
    private boolean mDrawShadows;
    /**
     * 阴影开始颜色
     */
    private int mStartColor;
    /**
     * 阴影中间颜色
     */
    private int mCenterColor;
    /**
     * 阴影结束颜色
     */
    private int mEndColor;
    /**
     * 被选中的背景
     */
    private int mSelectBg;
    /**
     * 背景
     */
    private int mBg;
    /**
     * 字的颜色
     */
    //    private int mTextColor;
    /**
     * the text color
     */
    private int mDefaultColor;
    private int mSelectColor;
    /**
     * 字的大小
     */
    private int mTextSize;
    /**
     * 是否循环
     */
    private boolean mIsCycle;
    /**
     * 几级联动
     */
    private int mListCount;

    /**
     * 列表显示个数
     */
    private int mVisibleItems;

    private OnButtonClickListener listener;

    private OnButtonOneListClickListener mOneListClickListener;

    public CharacterPickerView(Context context) {
        super(context);
        init(context, null);
    }

    public CharacterPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CharacterPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CharacterPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.provice_city_area, this, true);

        mCancel = (Button) findViewById(R.id.cancel_btn);
        mConfirm = (Button) findViewById(R.id.confirm_btn);
        mProvince = (WheelView) findViewById(R.id.id_province);
        mCity = (WheelView) findViewById(R.id.id_city);
        mArea = (WheelView) findViewById(R.id.id_area);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CharacterPickerView, 0, 0);
        try {
            mDrawShadows = typedArray.getBoolean(R.styleable.CharacterPickerView_cp_drawShadows, false);
            mStartColor = typedArray.getColor(R.styleable.CharacterPickerView_cp_shadowsColorStart, 0xefE9E9E9);
            mCenterColor = typedArray.getColor(R.styleable.CharacterPickerView_cp_shadowsColorCenter, 0xcfE9E9E9);
            mEndColor = typedArray.getColor(R.styleable.CharacterPickerView_cp_shadowsColorEnd, 0x3fE9E9E9);

            mSelectBg = typedArray.getInteger(R.styleable.CharacterPickerView_cp_wheelSelectBackground, R.drawable.jd_wheel_val);
            mBg = typedArray.getInteger(R.styleable.CharacterPickerView_cp_wheelBackground, R.drawable.jd_wheel_bg);

            mDefaultColor = typedArray.getColor(R.styleable.CharacterPickerView_cp_textDefaultColor, 0xFF585858);
            mSelectColor = typedArray.getColor(R.styleable.CharacterPickerView_cp_textSelectColor, 0xFF585858);
//            mTextColor = typedArray.getColor(R.styleable.CharacterPickerView_cp_textColor, 0xFF585858);
            mTextSize = typedArray.getDimensionPixelSize(R.styleable.CharacterPickerView_cp_textSize, getResources().getDimensionPixelSize(R.dimen.default_size));

            mIsCycle = typedArray.getBoolean(R.styleable.CharacterPickerView_cp_cycle, false);
            mListCount = typedArray.getInteger(R.styleable.CharacterPickerView_cp_listCount, 3);
            mVisibleItems = typedArray.getInteger(R.styleable.CharacterPickerView_cp_visibleItems, 5);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            typedArray.recycle();
        }

        initWheel();
        initButton();
    }

    private void initButton() {
        mConfirm.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    private void initWheel() {
        setWheelDrawShadows(mDrawShadows);
        if (mDrawShadows) {
            setShadows(mStartColor, mCenterColor, mEndColor);
        }
        setDefaultColor(mDefaultColor);
        setSelectColor(mSelectColor);
        mProvince.addChangingListener(this);
        mCity.addChangingListener(this);
        mArea.addChangingListener(this);
        setSelectBg(mSelectBg);
        setBg(mBg);
        setVisibleItem(mVisibleItems);
        setListCount(mListCount);
        setCycle(mIsCycle);
    }

    /**
     * 设置是否循环
     *
     * @param cycle 循环
     */
    public void setCycle(boolean cycle) {
        mIsCycle = cycle;
        mProvince.setCyclic(cycle);
        mCity.setCyclic(cycle);
        mArea.setCyclic(cycle);
    }

    public int getSelectColor() {
        return mSelectColor;
    }

    /**
     * 设置选中项颜色
     *
     * @param mSelectColor 颜色
     */
    public void setSelectColor(int mSelectColor) {
        this.mSelectColor = mSelectColor;
        mProvince.setSelectorColor(mSelectColor);
        mCity.setSelectorColor(mSelectColor);
        mArea.setSelectorColor(mSelectColor);
    }

    public int getDefaultColor() {
        return mDefaultColor;
    }

    /**
     * 设置非选中项颜色
     *
     * @param mDefaultColor 颜色
     */
    public void setDefaultColor(int mDefaultColor) {
        this.mDefaultColor = mDefaultColor;
        mProvince.setDefaultColor(mDefaultColor);
        mCity.setDefaultColor(mDefaultColor);
        mArea.setDefaultColor(mDefaultColor);
    }

//    public void setTextColor(int textColor){
//        mTextColor = textColor;
//        if (mProvinceAdapter != null){
//            mProvinceAdapter.setTextColor(textColor);
//        }
//        if (mCityAdapter != null){
//            mCityAdapter.setTextColor(textColor);
//        }
//        if (mAreaAdapter != null){
//            mAreaAdapter.setTextColor(textColor);
//        }
//    }
//
//    public int getTextColor(){
//        return mTextColor;
//    }

    /**
     * 设置字体大小
     *
     * @param textSize 设置字体大小
     */
    public void setTextSize(int textSize) {
        mTextSize = textSize;
        if (mProvinceAdapter != null) {
            mProvinceAdapter.setTextSize(textSize);
        }
        if (mCityAdapter != null) {
            mCityAdapter.setTextSize(textSize);
        }
        if (mAreaAdapter != null) {
            mAreaAdapter.setTextSize(textSize);
        }
    }

    public int getTextSize() {
        return mTextSize;
    }

    public boolean isCycle() {
        return mIsCycle;
    }

    /**
     * 设置表层模糊渐变
     *
     * @param drawShadows 是否需要
     */
    public void setWheelDrawShadows(boolean drawShadows) {
        mDrawShadows = drawShadows;
        mProvince.setDrawShadows(drawShadows);
        mCity.setDrawShadows(drawShadows);
        mArea.setDrawShadows(drawShadows);
    }

    public boolean getWheelDrawShadows() {
        return mDrawShadows;
    }

    /**
     * 设置表层渐变颜色
     *
     * @param start  开始颜色
     * @param center 中间颜色
     * @param end    结束颜色
     */
    public void setShadows(int start, int center, int end) {
        mStartColor = start;
        mCenterColor = center;
        mEndColor = end;
        mProvince.setShadowColor(start, center, end);
        mCity.setShadowColor(start, center, end);
        mArea.setShadowColor(start, center, end);
    }

    public int[] getShadows() {
        return new int[]{mStartColor, mCenterColor, mEndColor};
    }

    /**
     * 设置选中背景
     *
     * @param resId 背景id
     */
    public void setSelectBg(int resId) {
        mSelectBg = resId;
        mProvince.setWheelForeground(resId);
        mCity.setWheelForeground(resId);
        mArea.setWheelForeground(resId);
    }

    public int getSelectBg() {
        return mSelectBg;
    }

    /**
     * 设置背景
     *
     * @param resId 背景id
     */
    public void setBg(int resId) {
        mBg = resId;
        mProvince.setWheelBackground(resId);
        mCity.setWheelBackground(resId);
        mArea.setWheelBackground(resId);

    }

    public int getBg() {
        return mBg;
    }

    /**
     * 设置可见项数目
     *
     * @param count 数目
     */
    public void setVisibleItem(int count) {
        mVisibleItems = count;
        mProvince.setVisibleItems(count);
        mCity.setVisibleItems(count);
        mArea.setVisibleItems(count);
    }

    public int getVisibleItem() {
        return mVisibleItems;
    }

    /**
     * 设置联动链表数目 最大3个， 最小1个
     *
     * @param count 数目
     */
    public void setListCount(int count) {
        count = count < 1 ? 1 : count > 3 ? 3 : count;
        mListCount = count;
        switch (count) {
            case 1:
                mCity.setVisibility(GONE);
                mArea.setVisibility(GONE);
                break;
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

    public void setOnlyOneListDatas(List<String> oneListDatas) {
        mOnlyOneListDatas = oneListDatas;
        mProvinceAdapter = new ArrayWheelAdapter<>(getContext(), oneListDatas);
//        mProvinceAdapter.setTextColor(mTextColor);
        mProvinceAdapter.setTextSize(mTextSize);
        mProvincePosition = oneListDatas.indexOf(mOneData);
        mProvince.setViewAdapter(mProvinceAdapter);
        int pCurrent = mProvince.getCurrentItem();
        mOneData = mOnlyOneListDatas.get(pCurrent);
        mProvince.setCurrentItem(mProvincePosition);
    }

    /**
     * 设置第一个列表数据
     *
     * @param province
     */
    public void setProvince(List<DataModel> province) {
        mProvinceDatas = province;
        mProvinceAdapter = new ArrayWheelAdapter<>(getContext(), mProvinceDatas);
//        mProvinceAdapter.setTextColor(mTextColor);
        mProvinceAdapter.setTextSize(mTextSize);
        for (int i = 0; i < mProvinceDatas.size(); i++) {
            DataModel data = mProvinceDatas.get(i);
            if (data.Id == mProvinceId) {
                mProvincePosition = i;
                break;
            }
        }
        mProvince.setViewAdapter(mProvinceAdapter);
        int pCurrent = mProvince.getCurrentItem();
        mCurrentProvince = mProvinceDatas.get(pCurrent);
        mProvince.setCurrentItem(mProvincePosition);
    }

    /**
     * 设置第二个列表数据
     *
     * @param city
     */
    public void setCity(Map<String, List<DataModel>> city) {
        mCitisDatasMap = city;
        List<DataModel> temp_city = city.get(mProvinceId + "");
        if (temp_city != null) {
            for (int i = 0; i < temp_city.size(); i++) {
                DataModel data = temp_city.get(i);
                if (data.Id == mCityId) {
                    mCityPosition = i;
                    break;
                }
            }
        }
        updateCities();
        mCity.setCurrentItem(mCityPosition);
    }

    /**
     * 设置第三个列表数据
     *
     * @param area
     */
    public void setArea(Map<String, List<DataModel>> area) {
        mAreaDatasMap = area;
        List<DataModel> temp_area = area.get(mCityId + "");
        if (temp_area != null) {
            for (int i = 0; i < temp_area.size(); i++) {
                DataModel data = temp_area.get(i);
                if (data.Id == mAreaId) {
                    mAreaPosition = i;
                    break;
                }
            }
        }
        updateAreas();
        mArea.setCurrentItem(mAreaPosition);
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
     * 设置默认选中(只有一个列表)
     */
    public void setOnlyOneDefault(String oneData) {
        mOneData = oneData;
    }

    /**
     * 设置所有数据
     *
     * @param locations
     */
    public void setAllData(List<AllLocationsMode> locations) {
        for (AllLocationsMode location : locations) {
            mProvinceDatas.add(new DataModel(location.Id, location.Name));
            List<CityModel> cities = location.Cities;
            List<DataModel> temp_cities = new LinkedList<>();
            if (cities != null) {
                for (CityModel city : cities) {
                    temp_cities.add(new DataModel(city.Id, city.Name));
                    mAreaDatasMap.put(city.Id + "", city.Districts);
                }
                mCitisDatasMap.put(location.Id + "", temp_cities);
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
    public void setListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    public OnButtonOneListClickListener getOneListClickListener() {
        return mOneListClickListener;
    }

    public void setOneListClickListener(OnButtonOneListClickListener oneListClickListener) {
        mOneListClickListener = oneListClickListener;
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mProvince) {
            updateCities();
        } else if (wheel == mCity) {
            updateAreas();
        } else if (wheel == mArea) {
            mCurrentArea = new DataModel(0, "");
            if (!TextUtils.isEmpty(mCurrentCity.Name)) {
                List<DataModel> areas = mAreaDatasMap.get(mCurrentCity.Id + "");
                if (areas != null && areas.size() > 0) {
                    mCurrentArea = areas.get(newValue);
                }
            }
        }

    }


    /**
     * 更新第三项
     */
    private void updateAreas() {
        int pCurrent = mCity.getCurrentItem();
        List<DataModel> cities = mCitisDatasMap.get(mCurrentProvince.Id + "");
        List<DataModel> areas;
        if (cities == null) {
            cities = new LinkedList<>();
            cities.add(new DataModel(0, ""));
            areas = new LinkedList<>();
            areas.add(new DataModel(0, ""));
            mCurrentCity = cities.get(pCurrent);
        } else {
            mCurrentCity = cities.get(pCurrent);
            areas = mAreaDatasMap.get(mCurrentCity.Id + "");
            if (areas == null) {
                areas = new LinkedList<>();
                areas.add(new DataModel(0, ""));
            }
        }
        if (mListCount > 2) {
            mCurrentArea = areas.get(0);
            mAreaAdapter = new ArrayWheelAdapter<>(getContext(), areas);
//            mAreaAdapter.setTextColor(mTextColor);
            mAreaAdapter.setTextSize(mTextSize);
            mArea.setViewAdapter(mAreaAdapter);
            mArea.setCurrentItem(0);

        }
    }


    /**
     * 更新第二项
     */
    private void updateCities() {
        int pCurrent = mProvince.getCurrentItem();
        mCurrentProvince = mProvinceDatas.get(pCurrent);
        if (mListCount > 1) {
            List<DataModel> cities = mCitisDatasMap.get(mCurrentProvince.Id + "");
            if (cities == null) {
                cities = new LinkedList<>();
                cities.add(new DataModel(0, ""));
            }
            mCityAdapter = new ArrayWheelAdapter<>(getContext(), cities);
//            mCityAdapter.setTextColor(mTextColor);
            mCityAdapter.setTextSize(mTextSize);
            mCity.setViewAdapter(mCityAdapter);
            mCity.setCurrentItem(0);
            updateAreas();
        } else {
            if (mOnlyOneListDatas != null){
                mOneData = mOnlyOneListDatas.get(pCurrent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (mListCount == 1){
            if (mOneListClickListener != null){
                if (v.getId() == R.id.confirm_btn) {
                    mOneListClickListener.onClick(TextUtils.isEmpty(mOneData) ? mCurrentProvince.Name : mOneData);
                } else if (v.getId() == R.id.cancel_btn) {
                    mOneListClickListener.onClick(null);
                }
            }
        }
        else if (listener != null) {
            if (v.getId() == R.id.confirm_btn) {
                listener.onClick(mCurrentProvince, mCurrentCity, mCurrentArea);
            } else if (v.getId() == R.id.cancel_btn) {
                listener.onClick(null, null, null);
            }
        }
    }


    public interface OnButtonClickListener {
        void onClick(DataModel province, DataModel city, DataModel area);
    }

    public interface OnButtonOneListClickListener {
        void onClick(String oneData);
    }
}
