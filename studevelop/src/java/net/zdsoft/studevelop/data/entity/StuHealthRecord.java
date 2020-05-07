package net.zdsoft.studevelop.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "studoc_health_record")
public class StuHealthRecord extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;

	private String studentId;// 学生id
	private String acadyear;// 学年
	private String semester;// 学期
	private double height;// 身高
	private double weight;// 体重
	private double leftEye;// 左眼视力
	private double rightEye;// 右眼视力
	private double physique;// 体质测试
	private double groupRead;// 成长阅读
	private double socialPractice;// 社会实践
	private double selfControl;// 自制力
	private double confidence;// 自信心
	private double contact;// 合作交往


	private String attention;//注意
	private String observation;//观察
	private String memory;//记忆
	private String thinking;//思维
	private String mood;//情绪
	private String will;//意志
	
	
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


	public double getHeight() {
		return height;
	}


	public void setHeight(double height) {
		this.height = height;
	}


	public double getWeight() {
		return weight;
	}


	public void setWeight(double weight) {
		this.weight = weight;
	}


	public double getLeftEye() {
		return leftEye;
	}


	public void setLeftEye(double leftEye) {
		this.leftEye = leftEye;
	}


	public double getRightEye() {
		return rightEye;
	}


	public void setRightEye(double rightEye) {
		this.rightEye = rightEye;
	}


	public double getPhysique() {
		return physique;
	}


	public void setPhysique(double physique) {
		this.physique = physique;
	}


	public double getGroupRead() {
		return groupRead;
	}


	public void setGroupRead(double groupRead) {
		this.groupRead = groupRead;
	}


	public double getSocialPractice() {
		return socialPractice;
	}


	public void setSocialPractice(double socialPractice) {
		this.socialPractice = socialPractice;
	}


	public double getSelfControl() {
		return selfControl;
	}


	public void setSelfControl(double selfControl) {
		this.selfControl = selfControl;
	}


	public double getConfidence() {
		return confidence;
	}


	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}


	public double getContact() {
		return contact;
	}


	public void setContact(double contact) {
		this.contact = contact;
	}


	public String getAttention() {
		return attention;
	}


	public void setAttention(String attention) {
		this.attention = attention;
	}


	public String getObservation() {
		return observation;
	}


	public void setObservation(String observation) {
		this.observation = observation;
	}


	public String getMemory() {
		return memory;
	}


	public void setMemory(String memory) {
		this.memory = memory;
	}


	public String getThinking() {
		return thinking;
	}


	public void setThinking(String thinking) {
		this.thinking = thinking;
	}


	public String getMood() {
		return mood;
	}


	public void setMood(String mood) {
		this.mood = mood;
	}


	public String getWill() {
		return will;
	}


	public void setWill(String will) {
		this.will = will;
	}


	@Override
	public String fetchCacheEntitName() {
		return "StuHealthRecord";
	}

}
