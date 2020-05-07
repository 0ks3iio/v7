package net.zdsoft.bigdata.datav.render.crete.custom;

import java.util.List;

/**
 * @author shenke
 * @since 2018/12/4 上午10:37
 */
public class SimpleTabOption {

    private List<Tab> tabs;
    private TabStyle tabStyle;

    public List<Tab> getTabs() {
        return tabs;
    }

    public void setTabs(List<Tab> tabs) {
        this.tabs = tabs;
    }

    public TabStyle getTabStyle() {
        return tabStyle;
    }

    public void setTabStyle(TabStyle tabStyle) {
        this.tabStyle = tabStyle;
    }

    public static class TabStyle {

        private Integer tabWidth;
        private Integer tabHeight;
        private String category;
        private String orient;
        private Integer space;
        private Integer fontSize;
        private String fontColor;
        private String fontBold;
        private String boxBackgroundColor;

        private Integer selectedFontSize;
        private String selectedFontColor;
        private String selectedFontBold;
        private String selectedBoxBackgroundColor;


        public Integer getTabWidth() {
            return tabWidth;
        }

        public void setTabWidth(Integer tabWidth) {
            this.tabWidth = tabWidth;
        }

        public Integer getTabHeight() {
            return tabHeight;
        }

        public void setTabHeight(Integer tabHeight) {
            this.tabHeight = tabHeight;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getOrient() {
            return orient;
        }

        public void setOrient(String orient) {
            this.orient = orient;
        }

        public Integer getSpace() {
            return space;
        }

        public void setSpace(Integer space) {
            this.space = space;
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

        public String getFontBold() {
            return fontBold;
        }

        public void setFontBold(String fontBold) {
            this.fontBold = fontBold;
        }

        public String getBoxBackgroundColor() {
            return boxBackgroundColor;
        }

        public void setBoxBackgroundColor(String boxBackgroundColor) {
            this.boxBackgroundColor = boxBackgroundColor;
        }

        public Integer getSelectedFontSize() {
            return selectedFontSize;
        }

        public void setSelectedFontSize(Integer selectedFontSize) {
            this.selectedFontSize = selectedFontSize;
        }

        public String getSelectedFontColor() {
            return selectedFontColor;
        }

        public void setSelectedFontColor(String selectedFontColor) {
            this.selectedFontColor = selectedFontColor;
        }

        public String getSelectedFontBold() {
            return selectedFontBold;
        }

        public void setSelectedFontBold(String selectedFontBold) {
            this.selectedFontBold = selectedFontBold;
        }

        public String getSelectedBoxBackgroundColor() {
            return selectedBoxBackgroundColor;
        }

        public void setSelectedBoxBackgroundColor(String selectedBoxBackgroundColor) {
            this.selectedBoxBackgroundColor = selectedBoxBackgroundColor;
        }
    }

    public static class Tab {
        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
