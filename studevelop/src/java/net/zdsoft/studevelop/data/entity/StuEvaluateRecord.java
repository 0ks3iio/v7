package net.zdsoft.studevelop.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 期末评语
 * @author gzjsd
 *
 */
@Entity
@Table(name = "studoc_evaluate_record")
public class StuEvaluateRecord extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private String studentId;// 学生id
	private String acadyear;// 学年
	private String semester;// 学期
	private String evaluateLevel;//评价等级
	private String teacherEvalContent;//老师评价内容
	private String parentEvalContent;//家长评价内容
	private String stuHonorContent;//学生荣誉内容
	private String stuGatherContent;//学生采集内容
	private String stuWishContent;//学生愿望内容
	private String strong;//个性特点
	private String hobby;//爱好

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	//辅助字段
	@Transient
	private String studentName;//学生名字

	public String getStrong() {
		return strong;
	}

	public void setStrong(String strong) {
		this.strong = strong;
	}

	public String getStudentName() {
		return studentName;
	}



	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}



	public String getStudentId() {
		return studentId;
	}



	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}



	public String getAcadyear() {
		return acadyear;
	}



	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}



	public String getSemester() {
		return semester;
	}



	public void setSemester(String semester) {
		this.semester = semester;
	}



	public String getEvaluateLevel() {
		return evaluateLevel;
	}



	public void setEvaluateLevel(String evaluateLevel) {
		this.evaluateLevel = evaluateLevel;
	}



	public String getTeacherEvalContent() {
		return teacherEvalContent;
	}



	public void setTeacherEvalContent(String teacherEvalContent) {
		this.teacherEvalContent = teacherEvalContent;
	}



	public String getParentEvalContent() {
		return parentEvalContent;
	}



	public void setParentEvalContent(String parentEvalContent) {
		this.parentEvalContent = parentEvalContent;
	}



	public String getStuHonorContent() {
		return stuHonorContent;
	}



	public void setStuHonorContent(String stuHonorContent) {
		this.stuHonorContent = stuHonorContent;
	}



	public String getStuGatherContent() {
		return stuGatherContent;
	}



	public void setStuGatherContent(String stuGatherContent) {
		this.stuGatherContent = stuGatherContent;
	}



	public String getStuWishContent() {
		return stuWishContent;
	}



	public void setStuWishContent(String stuWishContent) {
		this.stuWishContent = stuWishContent;
	}



	@Override
	public String fetchCacheEntitName() {
		return "StuEvaluateRecord";
	}

}
