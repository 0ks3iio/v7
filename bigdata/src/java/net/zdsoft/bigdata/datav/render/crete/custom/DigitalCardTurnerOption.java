package net.zdsoft.bigdata.datav.render.crete.custom;

/**
 * @author shenke
 * @since 2018/12/3 上午9:49
 */
public class DigitalCardTurnerOption {

    private String number;
    private DigitalCardTurnerTitleStyle titleStyle;
    private DigitalCardTurner turner;

    public DigitalCardTurnerTitleStyle getTitleStyle() {
        return titleStyle;
    }

    public void setTitleStyle(DigitalCardTurnerTitleStyle titleStyle) {
        this.titleStyle = titleStyle;
    }

    public DigitalCardTurner getTurner() {
        return turner;
    }

    public void setTurner(DigitalCardTurner turner) {
        this.turner = turner;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public static class DigitalCardTurner {
        //前缀
        private String prefixContent;
        private Integer prefixFontSize;
        private String prefixFontColor;
        private String prefixFontBold;

        //数字
        private Integer numberFontSize;
        private String numberFontColor;
        private String numberFontBold;

        //后缀
        private String suffixContent;
        private Integer suffixFontSize;
        private String suffixFontColor;
        private String suffixFontBold;

        private String contentPosition;

        public String getPrefixContent() {
            return prefixContent;
        }

        public void setPrefixContent(String prefixContent) {
            this.prefixContent = prefixContent;
        }

        public Integer getPrefixFontSize() {
            return prefixFontSize;
        }

        public void setPrefixFontSize(Integer prefixFontSize) {
            this.prefixFontSize = prefixFontSize;
        }

        public String getPrefixFontColor() {
            return prefixFontColor;
        }

        public void setPrefixFontColor(String prefixFontColor) {
            this.prefixFontColor = prefixFontColor;
        }

        public String getPrefixFontBold() {
            return prefixFontBold;
        }

        public void setPrefixFontBold(String prefixFontBold) {
            this.prefixFontBold = prefixFontBold;
        }

        public Integer getNumberFontSize() {
            return numberFontSize;
        }

        public void setNumberFontSize(Integer numberFontSize) {
            this.numberFontSize = numberFontSize;
        }

        public String getNumberFontColor() {
            return numberFontColor;
        }

        public void setNumberFontColor(String numberFontColor) {
            this.numberFontColor = numberFontColor;
        }

        public String getNumberFontBold() {
            return numberFontBold;
        }

        public void setNumberFontBold(String numberFontBold) {
            this.numberFontBold = numberFontBold;
        }

        public String getSuffixContent() {
            return suffixContent;
        }

        public void setSuffixContent(String suffixContent) {
            this.suffixContent = suffixContent;
        }

        public Integer getSuffixFontSize() {
            return suffixFontSize;
        }

        public void setSuffixFontSize(Integer suffixFontSize) {
            this.suffixFontSize = suffixFontSize;
        }

        public String getSuffixFontColor() {
            return suffixFontColor;
        }

        public void setSuffixFontColor(String suffixFontColor) {
            this.suffixFontColor = suffixFontColor;
        }

        public String getSuffixFontBold() {
            return suffixFontBold;
        }

        public void setSuffixFontBold(String suffixFontBold) {
            this.suffixFontBold = suffixFontBold;
        }

        public String getContentPosition() {
            return contentPosition;
        }

        public void setContentPosition(String contentPosition) {
            this.contentPosition = contentPosition;
        }
    }

    public static class DigitalCardTurnerTitleStyle {

        private String titleContent;
        private Integer titleFontSize;
        private String titleFontColor;
        private String titleFontBold;
        private String titlePosition;
        private Integer titleBottom;

        public String getTitleContent() {
            return titleContent;
        }

        public void setTitleContent(String titleContent) {
            this.titleContent = titleContent;
        }

        public Integer getTitleFontSize() {
            return titleFontSize;
        }

        public void setTitleFontSize(Integer titleFontSize) {
            this.titleFontSize = titleFontSize;
        }

        public String getTitleFontColor() {
            return titleFontColor;
        }

        public void setTitleFontColor(String titleFontColor) {
            this.titleFontColor = titleFontColor;
        }

        public String getTitleFontBold() {
            return titleFontBold;
        }

        public void setTitleFontBold(String titleFontBold) {
            this.titleFontBold = titleFontBold;
        }

        public String getTitlePosition() {
            return titlePosition;
        }

        public void setTitlePosition(String titlePosition) {
            this.titlePosition = titlePosition;
        }

        public Integer getTitleBottom() {
            return titleBottom;
        }

        public void setTitleBottom(Integer titleBottom) {
            this.titleBottom = titleBottom;
        }
    }
}
