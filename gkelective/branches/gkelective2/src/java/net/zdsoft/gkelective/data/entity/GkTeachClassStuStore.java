package net.zdsoft.gkelective.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "gkelective_teach_stu_store")
public class GkTeachClassStuStore extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String gkClassId;
	private String studentId;
	
	public String getGkClassId() {
		return gkClassId;
	}

	public void setGkClassId(String gkClassId) {
		this.gkClassId = gkClassId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	@Override
	public String fetchCacheEntitName() {
		// TODO Auto-generated method stub
		return "gkTeachClassStuStore";
	}

}
