package net.zdsoft.bigdata.datav.render.crete.custom;

/**
 * @author shenke
 * @since 2018/10/14 15:11
 */
public class AbstractNumber {

    private String value;
    private Integer fontSize;
    private String fontColor;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }
}
