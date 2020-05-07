package net.zdsoft.gkelective.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "gkelective_stu_remark")
public class GkStuRemark extends BaseEntity<String>{
	private static final long serialVersionUID = 5312504711219291596L;
	
	public static final String TYPE_REMARK = "1"; //备注
	public static final String TYPE_SCORE = "2"; //读取每个科目成绩 
	public static final String TYPE_SCORE_YSY = "3"; //读取语数英总成绩
	
	public static final String YSY_SUBID="11111111111111111111111111111111";//语数英的subjectId值
	public static final String YSY_SUBNAME="语数英";
	
	private String subjectArrangeId;
	private String studentId;
	private String remark;
	private String type;
	private String subjectId;
	private Double score;
	
	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSubjectArrangeId() {
		return subjectArrangeId;
	}

	public void setSubjectArrangeId(String subjectArrangeId) {
		this.subjectArrangeId = subjectArrangeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String fetchCacheEntitName() {
		return "gkStuRemark";
	}

}
