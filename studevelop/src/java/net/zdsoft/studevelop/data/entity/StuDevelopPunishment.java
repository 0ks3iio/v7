package net.zdsoft.studevelop.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="student_punishment")
public class StuDevelopPunishment extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String schid;			//学校id
    private String stuid;			//学生id
    private String acadyear;
    private String semester;
    
    private String punishname;		//惩处名称
    private String punishType;		//惩处类型
    private Date punishdate;		//惩处日期
    private Date canceldate;		//撤销日期
    private String punishreason;	//惩处原因
    private String punishfilecode;	//惩处文号
    private String cancelfilecode;	//撤销文号
    private String remark;// 备注
    
    private String punishUnit;
    private Integer selfScore;//个人扣分
	private Integer classScore;//班级扣分
	private String operateUserId;//操作员
	@Transient
    private String stuName;
	@Override
	public String fetchCacheEntitName() {
		return "stuDevelopPunishment";
	}
	public String getSchid() {
		return schid;
	}
	public void setSchid(String schid) {
		this.schid = schid;
	}
	public String getStuid() {
		return stuid;
	}
	public void setStuid(String stuid) {
		this.stuid = stuid;
	}
	public String getAcadyear() {
		return acadyear;
	}
	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getPunishname() {
		return punishname;
	}
	public void setPunishname(String punishname) {
		this.punishname = punishname;
	}
	
	public String getPunishType() {
		return punishType;
	}
	public void setPunishType(String punishType) {
		this.punishType = punishType;
	}
	public Date getPunishdate() {
		return punishdate;
	}
	public void setPunishdate(Date punishdate) {
		this.punishdate = punishdate;
	}
	public Date getCanceldate() {
		return canceldate;
	}
	public void setCanceldate(Date canceldate) {
		this.canceldate = canceldate;
	}
	public String getPunishreason() {
		return punishreason;
	}
	public void setPunishreason(String punishreason) {
		this.punishreason = punishreason;
	}
	public String getPunishfilecode() {
		return punishfilecode;
	}
	public void setPunishfilecode(String punishfilecode) {
		this.punishfilecode = punishfilecode;
	}
	public String getCancelfilecode() {
		return cancelfilecode;
	}
	public void setCancelfilecode(String cancelfilecode) {
		this.cancelfilecode = cancelfilecode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPunishUnit() {
		return punishUnit;
	}
	public void setPunishUnit(String punishUnit) {
		this.punishUnit = punishUnit;
	}
	public Integer getSelfScore() {
		return selfScore;
	}
	public void setSelfScore(Integer selfScore) {
		this.selfScore = selfScore;
	}
	public Integer getClassScore() {
		return classScore;
	}
	public void setClassScore(Integer classScore) {
		this.classScore = classScore;
	}
	public String getOperateUserId() {
		return operateUserId;
	}
	public void setOperateUserId(String operateUserId) {
		this.operateUserId = operateUserId;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
    
}
