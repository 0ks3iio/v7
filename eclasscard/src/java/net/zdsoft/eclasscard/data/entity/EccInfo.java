package net.zdsoft.eclasscard.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "eclasscard_info")
public class EccInfo extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	private String name;
	private String ip;
	private String type;
	private String unitId;
	private String classId;
	private String placeId;
	private Integer status;
	private String remark;
	private String url;
	//增加一个厂家类型
	private String factoryType;
	//版本号
	private String wpVersion;

	@Transient
	private String placeName;
	@Transient
	private String className;
	@Transient
	private Integer currentFaceNum = 0;
	@Transient
	private String classIds;//下发班级
	@Transient
	private String classNames;//下发班级名称
	@Transient
	private Integer allNum = 0;

	@Override
	public String fetchCacheEntitName() {
		return "eclasscardInfo";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getCurrentFaceNum() {
		return currentFaceNum;
	}

	public void setCurrentFaceNum(Integer currentFaceNum) {
		this.currentFaceNum = currentFaceNum;
	}

	public String getClassIds() {
		return classIds;
	}

	public void setClassIds(String classIds) {
		this.classIds = classIds;
	}

	public String getClassNames() {
		return classNames;
	}

	public void setClassNames(String classNames) {
		this.classNames = classNames;
	}

	public Integer getAllNum() {
		return allNum;
	}

	public void setAllNum(Integer allNum) {
		this.allNum = allNum;
	}

	public String getFactoryType() {
		return factoryType;
	}

	public void setFactoryType(String factoryType) {
		this.factoryType = factoryType;
	}

	public String getWpVersion() {
		return wpVersion;
	}

	public void setWpVersion(String wpVersion) {
		this.wpVersion = wpVersion;
	}

}
