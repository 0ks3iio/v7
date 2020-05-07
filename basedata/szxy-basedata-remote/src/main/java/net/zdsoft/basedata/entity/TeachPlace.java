/* 
 * @(#)TeachPlace.java    Created on 2017-2-27
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_teach_place")
public class TeachPlace extends BaseEntity<String>{

    private static final long serialVersionUID = 2257032897003612058L;

    private String unitId;// 单位编号
    private String placeCode;// 场地编号，单位内唯一
    private String placeName;// 场地名称
    private String teachAreaId;// 分校区id
    private Integer placeNum;// 场地容纳人数
    private Date creationTime;// 创建时间
    private Date modifyTime;// 修改时间
    private boolean isDeleted;// 是否删除，默认0
    private String instituteId;// 学院id,适用中职平台
    private String placeAddress;// 场地详细地址
    private Double placeArea;// 场地面积，单位平方米，保留两位小数
    private String placeType;// 场地类型，参考微代码DM-CDLX;多选，半角逗号分隔
    @Column(name = "controllerid")
    private String controllerId;// 门禁控制器编号
    @Column(name = "doorno")
    private String doorNo;// 门牌号
    private String teachBuildingId;// 楼层信息
    private Integer floorNumber;// 楼层序号
    private Integer floorDisplayOrder;//
    private String groupName;
    private String machineCode; // 考勤机编码
    private Integer classNumber;// 场地容纳班级数
    @Transient
    private String teachBuildingName;

	@Override
    public String fetchCacheEntitName() {
        return "teachPlace";
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getPlaceCode() {
        return placeCode;
    }

    public void setPlaceCode(String placeCode) {
        this.placeCode = placeCode;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getTeachAreaId() {
        return teachAreaId;
    }

    public void setTeachAreaId(String teachAreaId) {
        this.teachAreaId = teachAreaId;
    }

    public Integer getPlaceNum() {
        return placeNum;
    }

    public void setPlaceNum(Integer placeNum) {
        this.placeNum = placeNum;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public Double getPlaceArea() {
        return placeArea;
    }

    public void setPlaceArea(Double placeArea) {
        this.placeArea = placeArea;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public String getTeachBuildingId() {
        return teachBuildingId;
    }

    public void setTeachBuildingId(String teachBuildingId) {
        this.teachBuildingId = teachBuildingId;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public Integer getFloorDisplayOrder() {
        return floorDisplayOrder;
    }

    public void setFloorDisplayOrder(Integer floorDisplayOrder) {
        this.floorDisplayOrder = floorDisplayOrder;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public Integer getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(Integer classNumber) {
        this.classNumber = classNumber;
    }

	public String getTeachBuildingName() {
		return teachBuildingName;
	}

	public void setTeachBuildingName(String teachBuildingName) {
		this.teachBuildingName = teachBuildingName;
	}
    
}
