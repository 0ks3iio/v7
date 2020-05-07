package net.zdsoft.newgkelective.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
/**
 * 表示某此选课的高二学考信息
 * @author wangyy
 *
 */
@Entity
@Table(name = "newgkelective_choice_ex")
public class NewGkChoiceEx extends BaseEntity<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String choiceId;
	private String studentId;
	private String subjectId;
	
	@Override
	public String fetchCacheEntitName() {
		return "newGkChoiceEx";
	}
	public String getChoiceId() {
		return choiceId;
	}
	public void setChoiceId(String choiceId) {
		this.choiceId = choiceId;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
}
