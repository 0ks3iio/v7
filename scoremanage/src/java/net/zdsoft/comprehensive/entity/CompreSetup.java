package net.zdsoft.comprehensive.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 总评科目来源与考试id
 * @author niuchao
 * @date 2018-12-10
 *
 */
@Entity
@Table(name="scoremanage_compre_setup")
public class CompreSetup extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String unitId;
	private String compreInfoId;//总评id
	private String examId;
	private String subjectId;
	private String type;//(1、学科成绩2、英语3、口试4、体育)
	private Date creationTime;
	private Date modifyTime;            

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getCompreInfoId() {
		return compreInfoId;
	}

	public void setCompreInfoId(String compreInfoId) {
		this.compreInfoId = compreInfoId;
	}

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
		return "compreSetup";
	}

}
