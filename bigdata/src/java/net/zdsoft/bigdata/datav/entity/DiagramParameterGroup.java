package net.zdsoft.bigdata.datav.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author shenke
 * @since 2018/9/26 10:58
 * 每种diagram的参数表
 */
@Entity
@Table(name = "bg_diagram_parameter_group")
public class DiagramParameterGroup extends BaseEntity<String> {
    /**
     * 对应的图表类型
     */
    private Integer diagramType;
    /**
     * 是否根分组
     */
    private Integer root;
    /**
     * 分组key
     */
    private String groupKey;
    /**
     * 分组名称
     */
    private String groupName;
    @Transient
    private String parentGroupId;
    /**
     * 参数名称
     */
    private String name;
    /**
     * 参数key
     */
    private String key;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 是否根据系列
     */
    private Integer array;
    /**
     * 数据系列前缀
     */
    private String arrayNamePrefix;
    /**
     * 默认的数据系列数量
     */
    private Integer defaultArraySize;
    /**
     * 参数类型
     */
    private Integer valueType;
    /**
     * 在分组之后再增加Category的概念
     * 分组之后再分组
     */
    private String category;
    /**
     * 排序号
     */
    private Integer orderNumber;

    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public Integer getDiagramType() {
        return diagramType;
    }

    public void setDiagramType(Integer diagramType) {
        this.diagramType = diagramType;
    }

    public Integer getRoot() {
        return root;
    }

    public void setRoot(Integer root) {
        this.root = root;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getArray() {
        return array;
    }

    public void setArray(Integer array) {
        this.array = array;
    }

    public Integer getDefaultArraySize() {
        return defaultArraySize;
    }

    public void setDefaultArraySize(Integer defaultArraySize) {
        this.defaultArraySize = defaultArraySize;
    }

    public Integer getValueType() {
        return valueType;
    }

    public void setValueType(Integer valueType) {
        this.valueType = valueType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArrayNamePrefix() {
        return arrayNamePrefix;
    }

    public void setArrayNamePrefix(String arrayNamePrefix) {
        this.arrayNamePrefix = arrayNamePrefix;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
}
