//package net.zdsoft.bigdata.datav.entity;
//
//import net.zdsoft.bigdata.datav.dto.Level;
//import net.zdsoft.framework.entity.BaseEntity;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//
///**
// * @author shenke
// * @since 2019/5/20 下午5:22
// */
//@Entity
//@Table(name = "bg_diagram_layer_group")
//public class DiagramLayerGroup extends BaseEntity<String> implements Level {
//
//    private String name;
//    private String screenId;
//    @Column(name = "layer_level")
//    private Integer level;
//
//    @Override
//    public String fetchCacheEntitName() {
//        return "diagramLayerGroup";
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getScreenId() {
//        return screenId;
//    }
//
//    public void setScreenId(String screenId) {
//        this.screenId = screenId;
//    }
//
//    @Override
//    public Integer getLevel() {
//        return level;
//    }
//
//    public void setLevel(Integer level) {
//        this.level = level;
//    }
//}
