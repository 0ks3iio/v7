package net.zdsoft.bigdata.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 数据模型参数
 * Created by wangdongdong on 2018/8/28 10:34.
 */
@Entity
@Table(name = "bg_model_param")
public class DataModelParam extends BaseEntity<String> {

	private static final long serialVersionUID = 815756488083338548L;

	private String code;

    private String type;

    private String name;

    private String useTable;

    private String useField;

    private String orderField;
    
    private String orderJson;

    private String dimForeignId;

    private String factForeignId;

    private String measures;

    private Integer orderId;

    private Integer isFilter;

    private String parentId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUseTable() {
        return useTable;
    }

    public void setUseTable(String useTable) {
        this.useTable = useTable;
    }

    public String getUseField() {
        return useField;
    }

    public void setUseField(String useField) {
        this.useField = useField;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getOrderJson() {
		return orderJson;
	}

	public void setOrderJson(String orderJson) {
		this.orderJson = orderJson;
	}

	public String getDimForeignId() {
        return dimForeignId;
    }

    public void setDimForeignId(String dimForeignId) {
        this.dimForeignId = dimForeignId;
    }

    public String getFactForeignId() {
        return factForeignId;
    }

    public void setFactForeignId(String factForeignId) {
        this.factForeignId = factForeignId;
    }

    public String getMeasures() {
        return measures;
    }

    public void setMeasures(String measures) {
        this.measures = measures;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getIsFilter() {
        return isFilter;
    }

    public void setIsFilter(Integer isFilter) {
        this.isFilter = isFilter;
    }

    @Override
    public String fetchCacheEntitName() {
        return "modelParam";
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }


}
