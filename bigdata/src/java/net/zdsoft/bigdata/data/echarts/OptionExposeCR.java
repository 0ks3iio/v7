package net.zdsoft.bigdata.data.echarts;

/**
 * @author shenke
 * @since 2018/7/27 下午2:48
 */
public class OptionExposeCR {

    private String name;
    private String pieCenter;
    private String pieRadius;
    /**
     * 是否支持选中模式
     * single multiple false true
     */
    private String pieSelectedMode;
    /**
     * 选中扇区的偏移距离
     */
    private Integer pieSelectedOffset;
    private Boolean pieClockWise;
    /**
     * 南丁格尔图
     * 是否展示成南丁格尔图，通过半径区分数据大小。可选择两种模式：
     *
     * 'radius' 扇区圆心角展现数据的百分比，半径展现数据的大小。
     * 'area' 所有扇区圆心角相同，仅通过半径展现数据大小。
     */
    private String pieRoseType;

    public String getPieCenter() {
        return pieCenter;
    }

    public void setPieCenter(String pieCenter) {
        this.pieCenter = pieCenter;
    }

    public String getPieRadius() {
        return pieRadius;
    }

    public void setPieRadius(String pieRadius) {
        this.pieRadius = pieRadius;
    }

    public String getPieSelectedMode() {
        return pieSelectedMode;
    }

    public void setPieSelectedMode(String pieSelectedMode) {
        this.pieSelectedMode = pieSelectedMode;
    }

    public Integer getPieSelectedOffset() {
        return pieSelectedOffset;
    }

    public void setPieSelectedOffset(Integer pieSelectedOffset) {
        this.pieSelectedOffset = pieSelectedOffset;
    }

    public Boolean getPieClockWise() {
        return pieClockWise;
    }

    public void setPieClockWise(Boolean pieClockWise) {
        this.pieClockWise = pieClockWise;
    }

    public String getPieRoseType() {
        return pieRoseType;
    }

    public void setPieRoseType(String pieRoseType) {
        this.pieRoseType = pieRoseType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
