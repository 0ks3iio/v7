package net.zdsoft.gkelective.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 
 * 限选科目
 */
@Entity
@Table(name = "gkelective_limit_subject")
public class GkLimitSubject extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private String subjectArrangeId;
	/**
	 * 使用逗号分隔 ,
	 */
	private String subjectVal;
	
    @Transient
    private String subjectNames;
    
    

	public String getSubjectNames() {
		return subjectNames;
	}


	public void setSubjectNames(String subjectNames) {
		this.subjectNames = subjectNames;
	}


	public String getSubjectArrangeId() {
		return subjectArrangeId;
	}


	public void setSubjectArrangeId(String subjectArrangeId) {
		this.subjectArrangeId = subjectArrangeId;
	}


	public String getSubjectVal() {
		return subjectVal;
	}


	public void setSubjectVal(String subjectVal) {
		this.subjectVal = subjectVal;
	}


	@Override
	public String fetchCacheEntitName() {
		return "GkLimitSubject";
	}
}
