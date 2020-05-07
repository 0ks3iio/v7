package net.zdsoft.basedata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2017-1-24下午5:08:45
 */
@Entity
@Table(name = "base_school_building_area")
public class SchoolBuildingArea extends BaseEntity<String>{
    
private static final long serialVersionUID = 1L;
	
	
    @ColumnInfo(displayName = "学校id")
	private String schoolId;
	
    @ColumnInfo(displayName = "校舍id")
	private String schoolBuildingId;
	
    @ColumnInfo(displayName = "建筑面积类型")
	private String areaType;
	
    @ColumnInfo(displayName = "面积")
	private double area;
	
    @ColumnInfo(displayName = "被外单位借用面积")
//	@SchoolTypeGroupScope(scope={SchoolTypeGroup.SECONDARY_VOCATIONAL})
	private double lendArea;
	
	
	@Override
	public String fetchCacheEntitName() {
		// TODO Auto-generated method stub
		return "schoolBuildingArea";
	}


	public String getSchoolId() {
		return schoolId;
	}


	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}


	public String getSchoolBuildingId() {
		return schoolBuildingId;
	}


	public void setSchoolBuildingId(String schoolBuildingId) {
		this.schoolBuildingId = schoolBuildingId;
	}


	public String getAreaType() {
		return areaType;
	}


	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}


	public double getArea() {
		return area;
	}


	public void setArea(double area) {
		this.area = area;
	}


	public double getLendArea() {
		return lendArea;
	}


	public void setLendArea(double lendArea) {
		this.lendArea = lendArea;
	}
	

}
