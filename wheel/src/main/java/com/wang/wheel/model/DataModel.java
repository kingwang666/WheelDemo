package com.wang.wheel.model;

/**
 * Created by wang
 * on 2016/4/12
 */
public class DataModel {
    /**
     * 地区id
     */
    public int Id;
    /**
     * 地区名称
     */
    public String Name;

    public DataModel(int id, String name) {
        this.Id = id;
        this.Name = name;
    }

    public DataModel(){

    }
}
