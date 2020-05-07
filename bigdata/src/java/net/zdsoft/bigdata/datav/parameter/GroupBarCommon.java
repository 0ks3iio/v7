package net.zdsoft.bigdata.datav.parameter;

/**
 * @author shenke
 * @since 2018/9/28 18:18
 */
public class GroupBarCommon {

    private String barGap;
    private String barCategoryGap;
    private Integer barWidth;
    private Integer barTopBorderRadius;
    private Integer barBottomBorderRadius;

    private String topCs;
    private String bottomCs;
    private String leftCs;
    private String rightCs;

    private Boolean sort;
    private Boolean smooth;

    public String getBarGap() {
        return barGap;
    }

    public void setBarGap(String barGap) {
        this.barGap = barGap;
    }

    public String getBarCategoryGap() {
        return barCategoryGap;
    }

    public void setBarCategoryGap(String barCategoryGap) {
        this.barCategoryGap = barCategoryGap;
    }

    public Integer getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(Integer barWidth) {
        this.barWidth = barWidth;
    }

    public Integer getBarTopBorderRadius() {
        return barTopBorderRadius;
    }

    public void setBarTopBorderRadius(Integer barTopBorderRadius) {
        this.barTopBorderRadius = barTopBorderRadius;
    }

    public Integer getBarBottomBorderRadius() {
        return barBottomBorderRadius;
    }

    public void setBarBottomBorderRadius(Integer barBottomBorderRadius) {
        this.barBottomBorderRadius = barBottomBorderRadius;
    }

    public String getTopCs() {
        return topCs;
    }

    public void setTopCs(String topCs) {
        this.topCs = topCs;
    }

    public String getBottomCs() {
        return bottomCs;
    }

    public void setBottomCs(String bottomCs) {
        this.bottomCs = bottomCs;
    }

    public String getLeftCs() {
        return leftCs;
    }

    public void setLeftCs(String leftCs) {
        this.leftCs = leftCs;
    }

    public String getRightCs() {
        return rightCs;
    }

    public void setRightCs(String rightCs) {
        this.rightCs = rightCs;
    }

    public Boolean getSort() {
        return sort;
    }

    public void setSort(Boolean sort) {
        this.sort = sort;
    }

    public Boolean getSmooth() {
        return smooth;
    }

    public void setSmooth(Boolean smooth) {
        this.smooth = smooth;
    }
}
