package kankan.wheel.widget.model;

import java.util.List;

/**
 * Created on 2016/1/4.
 * Author: wang
 */
public class AllLocationsMode {

    /**
     * 省id
     */
    public int Id;
    /**
     * 省名称
     */
    public String Name;
    /**
     * 城市信息，数据为数组类型
     */
    public List<CityModel> Child;

    public AllLocationsMode(int i, String aa) {
        Id = i;
        Name = aa;
    }
}
