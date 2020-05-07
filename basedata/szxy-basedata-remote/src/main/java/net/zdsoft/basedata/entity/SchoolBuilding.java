package net.zdsoft.basedata.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2017-1-24下午4:26:24
 */
@Entity
@Table(name = "base_school_building")
public class SchoolBuilding extends BaseEntity<String>{
   
private static final long serialVersionUID = 1L;
	
    @ColumnInfo(displayName = "学校id")
	private String schoolId;
	
    @ColumnInfo(displayName = "所属学段")
	private String section;
	
    @ColumnInfo(displayName = "序号")
	private Long serialNumber;
	
    @ColumnInfo(displayName = "校舍名称")
	private String name;
	
    @ColumnInfo(displayName = "主要用途")
	private String mainUse;
	
    @ColumnInfo(displayName = "结构类型")
	private String buildingType;
	
    @ColumnInfo(displayName = "层数")
	private Integer floors;
	
    @ColumnInfo(displayName = "竣工年份")
	private Integer completeYear;
	
    @ColumnInfo(displayName = "是否危房")
	private String isDilapidatedBuilding;
	
    @ColumnInfo(displayName = "危房鉴定证书文号")
	private String dilapidatedBuildingCode;

    @ColumnInfo(displayName = "危房等级")
	private String dilapidatedBuildingLevel;
	
    @ColumnInfo(displayName = "是否当年新增")
	private String isNewCurrentYear;
	
    @ColumnInfo(displayName = "造价（万）")
	private double cost;
	
//	@SchoolTypeGroupScope(scope={SchoolTypeGroup.PRIMARY, SchoolTypeGroup.PRIMARY_ADULT,
//			SchoolTypeGroup.JUNIOR, SchoolTypeGroup.JUNIOR_ADULT, SchoolTypeGroup.JUNIOR_CAREER,
//			SchoolTypeGroup.SENIOR, SchoolTypeGroup.SENIOR_ADULT, SchoolTypeGroup.REFORM_SCHOOL,
//			SchoolTypeGroup.SPECIAL_EDUCATION})
//	@OrganizerScope(scope={OrganizerType.NONGOVERNMENTAL})
    @ColumnInfo(displayName = "是否租借")
	private String isRental;
	
//	@SchoolTypeGroupScope(scope={SchoolTypeGroup.PRIMARY, SchoolTypeGroup.PRIMARY_ADULT,
//			SchoolTypeGroup.JUNIOR, SchoolTypeGroup.JUNIOR_ADULT, SchoolTypeGroup.JUNIOR_CAREER,
//			SchoolTypeGroup.SENIOR, SchoolTypeGroup.SENIOR_ADULT, SchoolTypeGroup.REFORM_SCHOOL,
//			SchoolTypeGroup.SPECIAL_EDUCATION})
//	@OrganizerScope(scope={OrganizerType.NONGOVERNMENTAL})
    @ColumnInfo(displayName = "租借面积")
	private double rentalArea;
	
//	@SchoolTypeGroupScope(scope={SchoolTypeGroup.PRIMARY, SchoolTypeGroup.PRIMARY_ADULT,
//			SchoolTypeGroup.JUNIOR, SchoolTypeGroup.JUNIOR_ADULT, SchoolTypeGroup.JUNIOR_CAREER,
//			SchoolTypeGroup.SENIOR, SchoolTypeGroup.SENIOR_ADULT, SchoolTypeGroup.REFORM_SCHOOL,
//			SchoolTypeGroup.SPECIAL_EDUCATION})
//	@OrganizerScope(scope={OrganizerType.NONGOVERNMENTAL})
    @ColumnInfo(displayName = "租借面积中：租借公办的基础教育学校的校舍建筑面积")
	private double rentalAreaPublic;
	
	@ColumnInfo(displayName = "教室数目")
	private Integer roomAmount;
	
//	@SchoolTypeGroupScope(scope={SchoolTypeGroup.PRIMARY, SchoolTypeGroup.PRIMARY_ADULT,
//			SchoolTypeGroup.JUNIOR, SchoolTypeGroup.JUNIOR_ADULT, SchoolTypeGroup.JUNIOR_CAREER,
//			SchoolTypeGroup.SENIOR, SchoolTypeGroup.SENIOR_ADULT, SchoolTypeGroup.REFORM_SCHOOL,
//			SchoolTypeGroup.SPECIAL_EDUCATION})
    @ColumnInfo(displayName = "普通教室数")
	private Integer normalRoomAmount;
	
    @ColumnInfo(displayName = "网络多媒体教室数目")
	private Integer multimediaRoomAmount;
	
//	@SchoolTypeGroupScope(scope={SchoolTypeGroup.PRIMARY, SchoolTypeGroup.PRIMARY_ADULT,
//			SchoolTypeGroup.JUNIOR, SchoolTypeGroup.JUNIOR_ADULT, SchoolTypeGroup.JUNIOR_CAREER,
//			SchoolTypeGroup.SENIOR, SchoolTypeGroup.SENIOR_ADULT, SchoolTypeGroup.REFORM_SCHOOL,
//			SchoolTypeGroup.SPECIAL_EDUCATION})
    @ColumnInfo(displayName = "普通教室中多媒体教室数")
	private Integer mutilmediaNormalRoomAmount;
	
    @ColumnInfo(displayName = "建筑面积总计")
	private double totalArea;
	
//	@SchoolTypeGroupScope(scope={SchoolTypeGroup.SECONDARY_VOCATIONAL})
//	TODO 数据库里面没有这个字段
    @ColumnInfo(displayName = "是否借给外单位使用")
	private String isLent;
	
//	@SchoolTypeGroupScope(scope={SchoolTypeGroup.SECONDARY_VOCATIONAL})
    @ColumnInfo(displayName = "产权形式：微代码DM-CQXS")
	private String ownership;
	
//	TODO 数据库里面没有这个字段	
    @ColumnInfo(displayName = "建筑使用情况DM-SYQK")
	private String usageSituation;
	
    @ColumnInfo(displayName = "建筑信息采集人")
	private String infoGatherer;
	
    @ColumnInfo(displayName = "建筑信息采集方式DM-CJFS")
	private String infoGathererMode;
	
	
	
	@Override
	public String fetchCacheEntitName() {
		// TODO Auto-generated method stub
		return "schoolBuilding";
	}



	public String getSchoolId() {
		return schoolId;
	}



	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}



	public String getSection() {
		return section;
	}



	public void setSection(String section) {
		this.section = section;
	}



	public Long getSerialNumber() {
		return serialNumber;
	}



	public void setSerialNumber(Long serialNumber) {
		this.serialNumber = serialNumber;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getMainUse() {
		return mainUse;
	}



	public void setMainUse(String mainUse) {
		this.mainUse = mainUse;
	}



	public String getBuildingType() {
		return buildingType;
	}



	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}



	public Integer getFloors() {
		return floors;
	}



	public void setFloors(Integer floors) {
		this.floors = floors;
	}



	public Integer getCompleteYear() {
		return completeYear;
	}



	public void setCompleteYear(Integer completeYear) {
		this.completeYear = completeYear;
	}



	public String getIsDilapidatedBuilding() {
		return isDilapidatedBuilding;
	}



	public void setIsDilapidatedBuilding(String isDilapidatedBuilding) {
		this.isDilapidatedBuilding = isDilapidatedBuilding;
	}



	public String getDilapidatedBuildingCode() {
		return dilapidatedBuildingCode;
	}



	public void setDilapidatedBuildingCode(String dilapidatedBuildingCode) {
		this.dilapidatedBuildingCode = dilapidatedBuildingCode;
	}



	public String getDilapidatedBuildingLevel() {
		return dilapidatedBuildingLevel;
	}



	public void setDilapidatedBuildingLevel(String dilapidatedBuildingLevel) {
		this.dilapidatedBuildingLevel = dilapidatedBuildingLevel;
	}



	public String getIsNewCurrentYear() {
		return isNewCurrentYear;
	}



	public void setIsNewCurrentYear(String isNewCurrentYear) {
		this.isNewCurrentYear = isNewCurrentYear;
	}



	public double getCost() {
		return cost;
	}



	public void setCost(double cost) {
		this.cost = cost;
	}



	public String getIsRental() {
		return isRental;
	}



	public void setIsRental(String isRental) {
		this.isRental = isRental;
	}



	public double getRentalArea() {
		return rentalArea;
	}



	public void setRentalArea(double rentalArea) {
		this.rentalArea = rentalArea;
	}



	public double getRentalAreaPublic() {
		return rentalAreaPublic;
	}



	public void setRentalAreaPublic(double rentalAreaPublic) {
		this.rentalAreaPublic = rentalAreaPublic;
	}



	public Integer getRoomAmount() {
		return roomAmount;
	}



	public void setRoomAmount(Integer roomAmount) {
		this.roomAmount = roomAmount;
	}



	public Integer getNormalRoomAmount() {
		return normalRoomAmount;
	}



	public void setNormalRoomAmount(Integer normalRoomAmount) {
		this.normalRoomAmount = normalRoomAmount;
	}



	public Integer getMultimediaRoomAmount() {
		return multimediaRoomAmount;
	}



	public void setMultimediaRoomAmount(Integer multimediaRoomAmount) {
		this.multimediaRoomAmount = multimediaRoomAmount;
	}



	public Integer getMutilmediaNormalRoomAmount() {
		return mutilmediaNormalRoomAmount;
	}



	public void setMutilmediaNormalRoomAmount(Integer mutilmediaNormalRoomAmount) {
		this.mutilmediaNormalRoomAmount = mutilmediaNormalRoomAmount;
	}



	public double getTotalArea() {
		return totalArea;
	}



	public void setTotalArea(double totalArea) {
		this.totalArea = totalArea;
	}



	public String getIsLent() {
		return isLent;
	}



	public void setIsLent(String isLent) {
		this.isLent = isLent;
	}



	public String getOwnership() {
		return ownership;
	}



	public void setOwnership(String ownership) {
		this.ownership = ownership;
	}



	public String getUsageSituation() {
		return usageSituation;
	}



	public void setUsageSituation(String usageSituation) {
		this.usageSituation = usageSituation;
	}



	public String getInfoGatherer() {
		return infoGatherer;
	}



	public void setInfoGatherer(String infoGatherer) {
		this.infoGatherer = infoGatherer;
	}



	public String getInfoGathererMode() {
		return infoGathererMode;
	}



	public void setInfoGathererMode(String infoGathererMode) {
		this.infoGathererMode = infoGathererMode;
	}
	
    
	
}
