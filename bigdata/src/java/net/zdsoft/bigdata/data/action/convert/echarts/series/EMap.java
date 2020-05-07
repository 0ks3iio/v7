package net.zdsoft.bigdata.data.action.convert.echarts.series;

import net.zdsoft.bigdata.data.action.convert.echarts.enu.SeriesType;

/**
 * @author ke_shen@126.com
 * @since 2018/4/17 下午2:58
 */
final public class EMap extends Series<EMap> {

    private Boolean roam;
    /** 自定义扩展地图类型 */
    private String mapType;
    private Object center;
    private Object nameMap;


    public EMap() {
        this.type(SeriesType.map);
    }

    public EMap(String name) {
        this.type(SeriesType.map);
        this.name(name);
    }

    public Boolean getRoam() {
        return roam;
    }

    public void setRoam(Boolean roam) {
        this.roam = roam;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public Object getCenter() {
        return center;
    }

    public void setCenter(Object center) {
        this.center = center;
    }

    public Object getNameMap() {
        return nameMap;
    }

    public void setNameMap(Object nameMap) {
        this.nameMap = nameMap;
    }
}
