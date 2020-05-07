package net.zdsoft.gkelective.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
/**
 * 新高考7-3一多关系表
 * @author zhouyz
 * 
 */
@Entity
@Table(name="gkelective_relationship")
public class GkRelationship extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String primaryId;
	/**
	 * 01:课程对应多个教师(newgkelective_course_arrange);02组合课程信息(newgkelective_condition);03选课开放科目(gkelective_subject_arrange)
	 */
	private String relationshipType;
	private String relationshipTargetId;
	public GkRelationship(){
	}
	
	public GkRelationship(String primaryId, String relationshipType,
			String relationshipTargetId) {
		this.primaryId = primaryId;
		this.relationshipType = relationshipType;
		this.relationshipTargetId = relationshipTargetId;
	}

	public String getPrimaryId() {
		return primaryId;
	}

	public void setPrimaryId(String primaryId) {
		this.primaryId = primaryId;
	}

	public String getRelationshipType() {
		return relationshipType;
	}

	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}

	public String getRelationshipTargetId() {
		return relationshipTargetId;
	}

	public void setRelationshipTargetId(String relationshipTargetId) {
		this.relationshipTargetId = relationshipTargetId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "gkRelationship";
	}
	
	

}
