package net.zdsoft.bigdata.datav.vo;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/27 18:23
 */
public class DiagramEnumGroup {

    private String group;
    private String iconUrl;
    private List<DiagramEnumCategory> categories;


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<DiagramEnumCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<DiagramEnumCategory> categories) {
        this.categories = categories;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
