package net.zdsoft.bigdata.datav.render.crete.custom;

/**
 * @author shenke
 * @since 2018/10/14 13:01
 */
public class TitleInnerOption {

    private String textFontColor;
    private Integer textFontSize;
    private String textFontWeight;
    private String value;
    private String url;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTextFontColor() {
        return textFontColor;
    }

    public void setTextFontColor(String textFontColor) {
        this.textFontColor = textFontColor;
    }

    public Integer getTextFontSize() {
        return textFontSize;
    }

    public void setTextFontSize(Integer textFontSize) {
        this.textFontSize = textFontSize;
    }

    public String getTextFontWeight() {
        return textFontWeight;
    }

    public void setTextFontWeight(String textFontWeight) {
        this.textFontWeight = textFontWeight;
    }
}