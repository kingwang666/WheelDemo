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
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kankan.wheel.R;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.lintener.OnWheelChangedListener;
import kankan.wheel.widget.model.AllLocationsMode;
import kankan.wheel.widget.model.CityModel;
import kankan.wheel.widget.model.DataModel;
import kankan.wheel.widget.wheel2.WheelView2;

/**
 * Created by wang
 * on 2016/5/12
 */
public class IOSCharacterPickerView extends LinearLayout implements OnWheelChangedListener, View.OnClickListener {

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
    private WheelView2 mProvince;
    /**
     * 市的WheelView控件
     */
    private WheelView2 mCity;
    /**
     * 区的WheelView控件
     */
    private WheelView2 mArea;

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



    public IOSCharacterPickerView(Context context) {
        super(context);
        init(context, null);
    }

    public IOSCharacterPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IOSCharacterPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IOSCharacterPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.provice_city_area_ios, this, true);

        mCancel = (Button) findViewById(R.id.cancel_btn);
        mConfirm = (Button) findViewById(R.id.confirm_btn);
        mProvince = (WheelView2) findViewById(R.id.id_province);
        mCity = (WheelView2) findViewById(R.id.id_city);
        mArea = (WheelView2) findViewById(R.id.id_area);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CharacterPickerView, 0, 0);
        try {

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

        mProvince.setOnWheelChangedListener(this);
        mCity.setOnWheelChangedListener(this);
        mArea.setOnWheelChangedListener(this);
//        setVisibleItem(mVisibleItems);
        setListCount(/*mListCount*/3);
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


    public int getTextSize() {
        return mTextSize;
    }

    public boolean isCycle() {
        return mIsCycle;
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



    @Override
    public void onChanged(View wheel, int oldValue, int newValue) {
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
            mAreaAdapter.setTextSize(mTextSize);
            mArea.setAdapter(mAreaAdapter);
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
            mCityAdapter.setTextSize(mTextSize);
            mCity.setAdapter(mCityAdapter);
            mCity.setCurrentItem(0);
            updateAreas();
        } else {
            if (mOnlyOneListDatas != null){
//                mOneData = mOnlyOneListDatas.get(pCurrent);
            }
        }
    }

    @Override
    public void onClick(View v) {
//        if (mListCount == 1){
//
//        }
//        else if (listener != null) {
//            if (v.getId() == R.id.confirm_btn) {
//                listener.onClick(mCurrentProvince, mCurrentCity, mCurrentArea);
//            } else if (v.getId() == R.id.cancel_btn) {
//                listener.onClick(null, null, null);
//            }
//        }
    }

}
