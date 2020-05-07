package net.zdsoft.eclasscard.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "eclasscard_date_info")
public class EccDateInfo extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	@Column(name = "sch_id")
	private String schoolId;

	@Temporal(TemporalType.DATE)
	private Date infoDate;
	private String gradeId;
	private String remark;

	@Override
	public String fetchCacheEntitName() {
		return "eclasscardDateInfo";
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public Date getInfoDate() {
		return infoDate;
	}

	public void setInfoDate(Date infoDate) {
		this.infoDate = infoDate;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
