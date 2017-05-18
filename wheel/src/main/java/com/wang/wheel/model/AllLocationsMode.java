package com.wang.wheel.model;

import java.util.List;

/**
 * Created on 2016/1/4.
 * Author: wang
 */
public class AllLocationsMode extends DataModel{
    /**
     * 城市信息，数据为数组类型
     */
    public List<CityModel> Child;

    public AllLocationsMode(int id, String name) {
        super(id, name);
    }

    public AllLocationsMode(){

    }
}
