package net.zdsoft.bigdata.datav.vo;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/27 19:31
 */
public class DiagramEnumCategory {

    private String categoryName;
    private String iconUrl;
    private List<DiagramEnumView> views;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<DiagramEnumView> getViews() {
        return views;
    }

    public void setViews(List<DiagramEnumView> views) {
        this.views = views;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
