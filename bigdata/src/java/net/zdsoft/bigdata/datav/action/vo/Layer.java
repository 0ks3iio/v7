package net.zdsoft.bigdata.datav.action.vo;

import net.zdsoft.bigdata.datav.dto.SimpleDiagram;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author shenke
 * @since 2019/5/20 下午7:03
 */
public class Layer implements Comparable<Layer> {

    private String name;
    private String id;
    private Integer level;
    private String iconUrl;

    private List<Layer> children;

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<Layer> getChildren() {
        return children;
    }

    public void setChildren(List<Layer> children) {
        this.children = children;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Layer convert(SimpleDiagram diagram) {
        Layer layer = new Layer();
        BeanUtils.copyProperties(diagram, layer, "layerGroupId", "diagramType");
        layer.setIconUrl("/bigdata/v3/static/datav/images/chart-" + diagram.getDiagramType() + ".png");
        return layer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Layer)) return false;
        Layer layer = (Layer) o;
        return Objects.equals(id, layer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Layer o) {
        return level.compareTo(o.getLevel());
    }
}
