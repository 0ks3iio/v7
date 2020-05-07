package net.zdsoft.bigdata.datav.dto;

/**
 * @author shenke
 * @since 2019/5/20 下午5:42
 */
public class SimpleDiagram implements Level {

    private String name;
    private String id;
    private Integer level;
    //private String layerGroupId;
    private Integer diagramType;

    public SimpleDiagram() {
    }

    public SimpleDiagram(String name, String id, Integer level, /*String layerGroupId,*/ Integer diagramType) {
        this.name = name;
        this.id = id;
        this.level = level;
        //this.layerGroupId = layerGroupId;
        this.diagramType = diagramType;
    }

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

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public Integer getLevel() {
        return level;
    }

    public Integer getDiagramType() {
        return diagramType;
    }

    public void setDiagramType(Integer diagramType) {
        this.diagramType = diagramType;
    }
}
