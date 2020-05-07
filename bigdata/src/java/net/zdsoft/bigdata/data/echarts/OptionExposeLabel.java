package net.zdsoft.bigdata.data.echarts;

/**
 * @author shenke
 * @since 2018/7/26 下午2:45
 */
public class OptionExposeLabel {
    private String name;
    private Boolean showSLabel;

    private Integer sLabelTextFontSize;
    private Boolean sLabelTextFontBold;
    private Boolean sLabelTextFontItalic;
    private String sLabelTextFontColor;

    private String sLabelBackgroundColor;
    private Integer sLabelBackgroundColorTransparent;
    private Integer sLabelBorderWidth;
    private String sLabelBorderColor;
    private Integer sLabelBorderColorTransparent;

    private String sLabelPosition;

    private Boolean showPieLabelLine;
    /**
     * 视觉引导线的光滑度
     */
    private Integer pieLabelLineSmooth;
    //private String pieLabelLineColor;
    private Integer pieLabelLineWidth;
    private String pieLabelLineType;

    public Boolean getShowSLabel() {
        return showSLabel;
    }

    public void setShowSLabel(Boolean showSLabel) {
        this.showSLabel = showSLabel;
    }

    public Integer getsLabelTextFontSize() {
        return sLabelTextFontSize;
    }

    public void setsLabelTextFontSize(Integer sLabelTextFontSize) {
        this.sLabelTextFontSize = sLabelTextFontSize;
    }

    public Boolean getsLabelTextFontBold() {
        return sLabelTextFontBold;
    }

    public void setsLabelTextFontBold(Boolean sLabelTextFontBold) {
        this.sLabelTextFontBold = sLabelTextFontBold;
    }

    public Boolean getsLabelTextFontItalic() {
        return sLabelTextFontItalic;
    }

    public void setsLabelTextFontItalic(Boolean sLabelTextFontItalic) {
        this.sLabelTextFontItalic = sLabelTextFontItalic;
    }

    public String getsLabelTextFontColor() {
        return sLabelTextFontColor;
    }

    public void setsLabelTextFontColor(String sLabelTextFontColor) {
        this.sLabelTextFontColor = sLabelTextFontColor;
    }

    public String getsLabelBackgroundColor() {
        return sLabelBackgroundColor;
    }

    public void setsLabelBackgroundColor(String sLabelBackgroundColor) {
        this.sLabelBackgroundColor = sLabelBackgroundColor;
    }

    public Integer getsLabelBackgroundColorTransparent() {
        return sLabelBackgroundColorTransparent;
    }

    public void setsLabelBackgroundColorTransparent(Integer sLabelBackgroundColorTransparent) {
        this.sLabelBackgroundColorTransparent = sLabelBackgroundColorTransparent;
    }

    public Integer getsLabelBorderWidth() {
        return sLabelBorderWidth;
    }

    public void setsLabelBorderWidth(Integer sLabelBorderWidth) {
        this.sLabelBorderWidth = sLabelBorderWidth;
    }

    public String getsLabelBorderColor() {
        return sLabelBorderColor;
    }

    public void setsLabelBorderColor(String sLabelBorderColor) {
        this.sLabelBorderColor = sLabelBorderColor;
    }

    public Integer getsLabelBorderColorTransparent() {
        return sLabelBorderColorTransparent;
    }

    public void setsLabelBorderColorTransparent(Integer sLabelBorderColorTransparent) {
        this.sLabelBorderColorTransparent = sLabelBorderColorTransparent;
    }

    public String getsLabelPosition() {
        return sLabelPosition;
    }

    public void setsLabelPosition(String sLabelPosition) {
        this.sLabelPosition = sLabelPosition;
    }

    public Boolean getShowPieLabelLine() {
        return showPieLabelLine;
    }

    public void setShowPieLabelLine(Boolean showPieLabelLine) {
        this.showPieLabelLine = showPieLabelLine;
    }

    public Integer getPieLabelLineSmooth() {
        return pieLabelLineSmooth;
    }

    public void setPieLabelLineSmooth(Integer pieLabelLineSmooth) {
        this.pieLabelLineSmooth = pieLabelLineSmooth;
    }

    public Integer getPieLabelLineWidth() {
        return pieLabelLineWidth;
    }

    public void setPieLabelLineWidth(Integer pieLabelLineWidth) {
        this.pieLabelLineWidth = pieLabelLineWidth;
    }

    public String getPieLabelLineType() {
        return pieLabelLineType;
    }

    public void setPieLabelLineType(String pieLabelLineType) {
        this.pieLabelLineType = pieLabelLineType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
