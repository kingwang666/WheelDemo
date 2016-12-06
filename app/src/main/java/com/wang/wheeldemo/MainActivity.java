package com.wang.wheeldemo;

import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.lintener.OnButtonClickListener;
import kankan.wheel.widget.model.AllLocationsMode;
import kankan.wheel.widget.model.DataModel;
import kankan.wheel.widget.provinceCityArea.CharacterPickerView;
import kankan.wheel.widget.provinceCityArea.IOSCharacterPickerView;
import kankan.wheel.widget.wheel1.WheelView;
import kankan.wheel.widget.wheel2.WheelView2;


public class MainActivity extends AppCompatActivity {

    private JSONObject mJsonObj;


    private List<DataModel> mProvinceDatas;

    private SparseArrayCompat<List<DataModel>> mCitisDatasMap = new SparseArrayCompat<>();

    private SparseArrayCompat<List<DataModel>> mAreaDatasMap = new SparseArrayCompat<>();

    List<AllLocationsMode> responses = new LinkedList<>();

    private final Object lock = new Object();


    private CharacterPickerView view;

    private IOSCharacterPickerView mIOS;

    private WheelView mWheel;

    private WheelView2 mWheel2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (CharacterPickerView) findViewById(R.id.test);
        mIOS = (IOSCharacterPickerView) findViewById(R.id.test2);
        mWheel = (WheelView) findViewById(R.id.wheel_test);
        mWheel2 = (WheelView2) findViewById(R.id.wheel2_test);
        initJsonData();

        initDatas();
        view.setDefault(2, 202, 2202);
        view.setProvince(mProvinceDatas);
        view.setCity(mCitisDatasMap);
        view.setArea(mAreaDatasMap);

        mIOS.setDefault(2, 202, 2202);
        mIOS.setProvince(mProvinceDatas);
        mIOS.setCity(mCitisDatasMap);
        mIOS.setArea(mAreaDatasMap);


        mWheel2.setAdapter(new ArrayWheelAdapter<>(this, mProvinceDatas));
        mWheel2.setCurrentItem(2);

        ArrayWheelAdapter adapter = new ArrayWheelAdapter<>(this, mProvinceDatas);
        mWheel.setAdapter(adapter);
        mWheel.setCurrentItem(0);

        view.setButtonClickListener(new OnButtonClickListener() {
            @Override
            public void onClick(DataModel province, DataModel city, DataModel area) {
                Log.d("test", province.Name + "  " + city.Name + "  " + area.Name);
            }

            @Override
            public void onCancel() {
                Log.d("test", "cancel");
            }
        });

        mIOS.setButtonClickListener(new OnButtonClickListener() {
            @Override
            public void onClick(DataModel province, DataModel city, DataModel area) {
                Log.d("test", province.Name + "  " + city.Name + "  " + area.Name);
            }

            @Override
            public void onCancel() {
                Log.d("test", "cancel");
            }
        });

    }


    private void initDatas() {
        try {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            mProvinceDatas = new LinkedList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonP = jsonArray.getJSONObject(i);
                String province = jsonP.getString("p");

                mProvinceDatas.add(new DataModel(i, province));

                JSONArray jsonCs = null;
                try {
                    /**
                     * Throws JSONException if the mapping doesn't exist or is
                     * not a JSONArray.
                     */
                    jsonCs = jsonP.getJSONArray("c");
                } catch (Exception e1) {
                    continue;
                }
                List<DataModel> mCitiesDatas = new LinkedList<>();
                for (int j = 0; j < jsonCs.length(); j++) {
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    String city = jsonCity.getString("n");// ������
                    mCitiesDatas.add(new DataModel(i * 100 + j, city));
                    JSONArray jsonAreas = null;
                    try {
                        /**
                         * Throws JSONException if the mapping doesn't exist or
                         * is not a JSONArray.
                         */
                        jsonAreas = jsonCity.getJSONArray("a");
                    } catch (Exception e) {
                        continue;
                    }

                    List<DataModel> mAreasDatas = new LinkedList<>();// ��ǰ�е�������
                    for (int k = 0; k < jsonAreas.length(); k++) {
                        String area = jsonAreas.getJSONObject(k).getString("s");// ���������
                        mAreasDatas.add(new DataModel(i * 1000 + j * 100 + k, area));
                    }
                    mAreaDatasMap.put(i * 100 + j, mAreasDatas);
                }

                mCitisDatasMap.put(i, mCitiesDatas);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
    }


    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = getAssets().open("city.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "gbk"));
            }
            is.close();
            mJsonObj = new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
