package net.zdsoft.bigdata.data.echarts;

import net.zdsoft.echarts.enu.LeftEnum;
import net.zdsoft.echarts.enu.TopEnum;

/**
 * @author shenke
 * @since 2018/8/16 13:56
 */
public class OptionExposeTitle {

    public static OptionExposeTitle getDefault() {
        OptionExposeTitle exposeTitle = new OptionExposeTitle();
        exposeTitle.setShowTitle(Boolean.FALSE);
        exposeTitle.setTitleFontSize(18);
        exposeTitle.setTitleFontBold(Boolean.TRUE);
        exposeTitle.setTitleFontItalic(Boolean.FALSE);
        exposeTitle.setTitleFontColor("#333");
        exposeTitle.setTitleLinkTarget("blank");
        exposeTitle.setTitleTop(TopEnum.top.name());
        exposeTitle.setTitleLeft(LeftEnum.left.name());
        return exposeTitle;
    }

    private Boolean showTitle;
    private Integer titleFontSize;
    private Boolean titleFontBold;
    private Boolean titleFontItalic;
    private String titleFontColor;
    private String titleLink;
    private String titleLinkTarget;
    private String titleTop;
    private String titleLeft;

    public Boolean getShowTitle() {
        return showTitle;
    }

    public void setShowTitle(Boolean showTitle) {
        this.showTitle = showTitle;
    }

    public Integer getTitleFontSize() {
        return titleFontSize;
    }

    public void setTitleFontSize(Integer titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    public Boolean getTitleFontBold() {
        return titleFontBold;
    }

    public void setTitleFontBold(Boolean titleFontBold) {
        this.titleFontBold = titleFontBold;
    }

    public Boolean getTitleFontItalic() {
        return titleFontItalic;
    }

    public void setTitleFontItalic(Boolean titleFontItalic) {
        this.titleFontItalic = titleFontItalic;
    }

    public String getTitleFontColor() {
        return titleFontColor;
    }

    public void setTitleFontColor(String titleFontColor) {
        this.titleFontColor = titleFontColor;
    }

    public String getTitleLink() {
        return titleLink;
    }

    public void setTitleLink(String titleLink) {
        this.titleLink = titleLink;
    }

    public String getTitleLinkTarget() {
        return titleLinkTarget;
    }

    public void setTitleLinkTarget(String titleLinkTarget) {
        this.titleLinkTarget = titleLinkTarget;
    }

    public String getTitleTop() {
        return titleTop;
    }

    public void setTitleTop(String titleTop) {
        this.titleTop = titleTop;
    }

    public String getTitleLeft() {
        return titleLeft;
    }

    public void setTitleLeft(String titleLeft) {
        this.titleLeft = titleLeft;
    }
}
