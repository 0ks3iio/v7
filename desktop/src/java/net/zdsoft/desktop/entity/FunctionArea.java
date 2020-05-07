package net.zdsoft.desktop.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

import java.util.List;

@Entity
@Table(name = "desktop_function_area")
public class FunctionArea extends BaseEntity<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 状态 正常
	 */
	public static final Integer STATE_NORMAL = 1;
	/**
	 * 状态 异常
	 */
	public static final Integer STATE_ILLEGAL = -1;

	@Override
	public String fetchCacheEntitName() {
		return "functionArea";
	}

	private String templateId;
	/**支持一些参数，支持{unitId}, {userId}, {ownerId}， 详见{@link FunctionAreaDataUrlEnum}*/
	private String dataUrl;
	private String name;
	/**类型*/
	@ColumnInfo(mcodeId="DM-ZMGNQ-LX")
	private String type;
	/**默认列数（最大12）*/
	private Integer columns;
	/**类型，1=显示，2=隐藏*/
	private Integer state;
	
	/**缓存时间， 秒为单位*/
	private Integer cacheTime;
	
	private Integer unitClass;
	
	private String userType;
    /** 功能区模块图片*/    
	private String templateImageUrl;
	
    @Transient
    private String isAdd;  // 1 -- 已经添加  ;0 --没有添加

	/**
	 * 动态参数，有别于dataUrl固定参数，根据不同的功能区决定，可为空
	 */
	//@Transient
	private String dynamicParam;

	public String getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(String isAdd) {
		this.isAdd = isAdd;
	}
	
	
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

	public Integer getColumns() {
		return columns;
	}

	public void setColumns(Integer columns) {
		this.columns = columns;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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

	public Integer getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(Integer cacheTime) {
		this.cacheTime = cacheTime;
	}

	public Integer getUnitClass() {
		return unitClass;
	}

	public void setUnitClass(Integer unitClass) {
		this.unitClass = unitClass;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getDynamicParam() {
		return dynamicParam;
	}

	public void setDynamicParam(String dynamicParam) {
		this.dynamicParam = dynamicParam;
	}
    
	public String getTemplateImageUrl() {
		return templateImageUrl;
	}

	public void setTemplateImageUrl(String templateImageUrl) {
		this.templateImageUrl = templateImageUrl;
	}
	
	public List<FunctionAreaDynamicParam> getDynamicParamType() {
		return FunctionAreaDynamicParam.parse(this.dynamicParam);
	}
}
