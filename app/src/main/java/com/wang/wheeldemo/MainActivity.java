package com.wang.wheeldemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kankan.wheel.widget.model.AllLocationsMode;
import kankan.wheel.widget.model.DataModel;
import kankan.wheel.widget.provinceCityArea.CharacterPickerView;


public class MainActivity extends AppCompatActivity {

    private JSONObject mJsonObj;


    private List<DataModel> mProvinceDatas;

    private Map<String, List<DataModel>> mCitisDatasMap = new HashMap<>();

    private Map<String, List<DataModel>> mAreaDatasMap = new HashMap<>();

    List<AllLocationsMode> responses = new LinkedList<>();

    private final Object lock = new Object();



    private CharacterPickerView view;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (CharacterPickerView) findViewById(R.id.test);
        initJsonData();

        initDatas();


		view.setDefault(2, 102, 1005);
        view.setProvince(mProvinceDatas);
        view.setCity(mCitisDatasMap);
        view.setArea(mAreaDatasMap);

        view.setListener(new CharacterPickerView.OnButtonClickListener() {
            @Override
            public void onClick(DataModel province, DataModel city, DataModel area) {
                Log.d("test",province.Name + "  " + city.Name + "  " + area.Name);
            }
        });


    }



    private void initDatas()
    {
        try {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            mProvinceDatas = new LinkedList<>();
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonP = jsonArray.getJSONObject(i);
                String province = jsonP.getString("p");

                mProvinceDatas.add(new DataModel(i, province));

                JSONArray jsonCs = null;
                try
                {
                    /**
                     * Throws JSONException if the mapping doesn't exist or is
                     * not a JSONArray.
                     */
                    jsonCs = jsonP.getJSONArray("c");
                } catch (Exception e1)
                {
                    continue;
                }
                List<DataModel> mCitiesDatas = new LinkedList<>();
                for (int j = 0; j < jsonCs.length(); j++)
                {
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    String city = jsonCity.getString("n");// ������
                    mCitiesDatas.add(new DataModel(100 + j, city));
                    JSONArray jsonAreas = null;
                    try
                    {
                        /**
                         * Throws JSONException if the mapping doesn't exist or
                         * is not a JSONArray.
                         */
                        jsonAreas = jsonCity.getJSONArray("a");
                    } catch (Exception e)
                    {
                        continue;
                    }

                    List<DataModel> mAreasDatas = new LinkedList<>();// ��ǰ�е�������
                    for (int k = 0; k < jsonAreas.length(); k++)
                    {
                        String area = jsonAreas.getJSONObject(k).getString("s");// ���������
                        mAreasDatas.add(new DataModel(1000 + k, area));
                    }
                    mAreaDatasMap.put(100 + j + "", mAreasDatas);
                }

                mCitisDatasMap.put(i + "", mCitiesDatas);
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        mJsonObj = null;
    }


    private void initJsonData()
    {
        try
        {
            StringBuffer sb = new StringBuffer();
            InputStream is = getAssets().open("city.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1)
            {
                sb.append(new String(buf, 0, len, "gbk"));
            }
            is.close();
            mJsonObj = new JSONObject(sb.toString());
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

}
