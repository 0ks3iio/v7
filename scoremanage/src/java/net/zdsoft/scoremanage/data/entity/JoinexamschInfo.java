package net.zdsoft.scoremanage.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "scoremanage_joinexamsch_info")
public class JoinexamschInfo extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;

	private String schoolId;
	private String examInfoId;
	
	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getExamInfoId() {
		return examInfoId;
	}

	public void setExamInfoId(String examInfoId) {
		this.examInfoId = examInfoId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "joinexamschInfo";
	}
	
}
