package net.zdsoft.gkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 *	手动调班记录  备份学生调班记录（行政班调班）
 */
@Entity
@Table(name="gkelective_student_conversion")
public class GkStuConversion extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private String roundsId;
	private String studentId;
	private String oldClassId;
	private String toClassId;
	private String operator;
	private Date creationTime;
	private Date modifyTime;

	public String getRoundsId() {
		return roundsId;
	}

	public void setRoundsId(String roundsId) {
		this.roundsId = roundsId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getOldClassId() {
		return oldClassId;
	}

	public void setOldClassId(String oldClassId) {
		this.oldClassId = oldClassId;
	}

	public String getToClassId() {
		return toClassId;
	}

	public void setToClassId(String toClassId) {
		this.toClassId = toClassId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
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

	@Override
	public String fetchCacheEntitName() {
		return "gkStuConversion";
	}

}
