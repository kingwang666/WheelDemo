package com.wang.wheel.model;

import java.util.List;

/**
 * Created by wang
 * on 2016/1/4
 */
public class CityModel extends DataModel{
    /**
     * 地区信息，数据为数组类型
     */
    public List<DataModel> Child;

    public CityModel(int id, String name) {
        super(id, name);
    }

    public CityModel() {

    }
}
