package net.zdsoft.savedata.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 考勤数据总表
 * @author yangsj  2018年8月10日上午11:09:39
 */
@Entity
@Table(name = "base_syn_check_work")
public class CheckWorkData extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	@Override
	public String fetchCacheEntitName() {
		return "checkWorkData";
	}
	private String unitIdent; //单位标识
	private String userIdent; //用户标识 如：卡号
	private String placeIdent; //场地标识
	private String typeIdent;  //具体业务类型 如：进校，出校
	private Date attendanceDate;  // 考勤时间
	private String areaSource;  //地区来源
//	private Integer sourceType; //来源类型   1--学生上课考勤  2--进出校考勤  3--全部考勤
	public String getUnitIdent() {
		return unitIdent;
	}
	public void setUnitIdent(String unitIdent) {
		this.unitIdent = unitIdent;
	}
	public String getUserIdent() {
		return userIdent;
	}
	public void setUserIdent(String userIdent) {
		this.userIdent = userIdent;
	}
	public String getPlaceIdent() {
		return placeIdent;
	}
	public void setPlaceIdent(String placeIdent) {
		this.placeIdent = placeIdent;
	}
	public String getTypeIdent() {
		return typeIdent;
	}
	public void setTypeIdent(String typeIdent) {
		this.typeIdent = typeIdent;
	}
	public Date getAttendanceDate() {
		return attendanceDate;
	}
	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}
	public String getAreaSource() {
		return areaSource;
	}
	public void setAreaSource(String areaSource) {
		this.areaSource = areaSource;
	}
}
