package com.wang.wheeldemo;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import com.wang.wheel.model.AllLocationsMode;
import com.wang.wheel.model.CityModel;
import com.wang.wheel.model.DataModel;

/**
 * Created on 2017/5/18.
 * Author: wang
 */

public abstract class BaseWheelActivity extends AppCompatActivity {

    protected List<AllLocationsMode> mLocations;

    protected List<DataModel> mProvinceDatas = new LinkedList<>();

    protected SparseArrayCompat<List<DataModel>> mCitisDatasMap = new SparseArrayCompat<>();

    protected SparseArrayCompat<List<DataModel>> mAreaDatasMap = new SparseArrayCompat<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        String json = initJsonData();
        Gson gson = new Gson();
        mLocations = gson.fromJson(json, TypeToken.getParameterized(List.class, AllLocationsMode.class).getType());
        for (AllLocationsMode location : mLocations) {
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
        afterView();
    }

    protected abstract int getLayoutId();

    protected abstract void afterView();

    private String initJsonData() {
        try {
            StringBuilder sb = new StringBuilder();
            InputStream is = getAssets().open("test.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
