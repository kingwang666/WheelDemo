package kankan.wheel.widget.model;

import java.util.List;

/**
 * Created by wang
 * on 2016/1/4
 */
public class CityModel {
    /**
     * 城市id
     */
    public int Id;
    /**
     * 市名称
     */
    public String Name;
    /**
     * 地区信息，数据为数组类型
     */
    public List<DataModel> Child;

}
