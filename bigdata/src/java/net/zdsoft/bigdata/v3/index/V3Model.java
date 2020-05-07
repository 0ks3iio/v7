package net.zdsoft.bigdata.v3.index;

import java.util.List;

/**
 * @author shenke
 * @since 2019/2/20 下午4:07
 */
public final class V3Model {

    private String id;
    private String name;
    private String pinyinName;
    private String type;
    private String url;
    private Integer openType;
    private String icon;
    private String description;
    private List<V3Model> children;

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getOpenType() {
        return openType;
    }

    public void setOpenType(Integer openType) {
        this.openType = openType;
    }

    public List<V3Model> getChildren() {
        return children;
    }

    public void setChildren(List<V3Model> children) {
        this.children = children;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPinyinName() {
        return pinyinName;
    }

    public void setPinyinName(String pinyinName) {
        this.pinyinName = pinyinName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
