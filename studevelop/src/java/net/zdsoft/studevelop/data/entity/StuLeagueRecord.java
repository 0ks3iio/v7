package net.zdsoft.studevelop.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
/**
 * 学生社团活动登记
 * @author gzjsd
 *
 */

@Entity
@Table(name = "studoc_league_record")
public class StuLeagueRecord extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;

	private String studentId;// 学生id
	private String acadyear;// 学年
	private String semester;// 学期
	private String leagueName;//社团名称
	private String leagueContent;//活动内容
	private Date joinDate;//加入日期
	private String remark;//备注
	
	@Transient
	private String studentName;
	
	
	
	public String getStudentName() {
		return studentName;
	}



	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}



	public String getStudentId() {
		return studentId;
	}



	public void setStudentId(String studentId) {
		this.studentId = studentId;
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



	public String getLeagueName() {
		return leagueName;
	}



	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}



	public String getLeagueContent() {
		return leagueContent;
	}



	public Date getJoinDate() {
		return joinDate;
	}



	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}



	public void setLeagueContent(String leagueContent) {
		this.leagueContent = leagueContent;
	}


	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	@Override
	public String fetchCacheEntitName() {
		return "StuLeagueRecord";
	}
	
}
