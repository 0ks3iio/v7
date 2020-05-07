package net.zdsoft.gkelective.data.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="gkelective_subject")
public class GkSubject extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	/**
	 * 不走班
	 */
	public static final int TEACH_TYPE0=0;
	/**
	 * 走班
	 */
	public static final int TEACH_TYPE1=1;
	/**
	 * 不开课
	 */
	public static final int TEACH_TYPE2=2;
	
	private String roundsId;
	private String subjectId;
	@Column(columnDefinition="教学方式(走班,不走班,不开课)")
	private Integer teachModel;
	private int punchCard;//是否需要电子班排打卡

	@Transient
	private String subjectName;
	@Transient
	private String[] teacherIds;

	public String[] getTeacherIds() {
		return teacherIds;
	}

	public void setTeacherIds(String[] teacherIds) {
		this.teacherIds = teacherIds;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getRoundsId() {
		return roundsId;
	}

	public void setRoundsId(String roundsId) {
		this.roundsId = roundsId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getTeachModel() {
		return teachModel;
	}

	public void setTeachModel(Integer teachModel) {
		this.teachModel = teachModel;
	}

	@Override
	public String fetchCacheEntitName() {
		return "gKSubject";
	}

	public int getPunchCard() {
		return punchCard;
	}

	public void setPunchCard(int punchCard) {
		this.punchCard = punchCard;
	}

}
