package net.zdsoft.studevelop.data.dto;

import org.apache.commons.lang3.StringUtils;

import net.zdsoft.studevelop.data.entity.StuHealthRecord;

public class StuHealthRecordDto {

	private String id;//id
	private String studentId;// 学生id
	private String acadyear;// 学年
	private String semester;// 学期
	private String height;// 身高
	private String weight;// 体重
	private String leftEye;// 左眼视力
	private String rightEye;// 右眼视力
	private String physique;// 体质测试
	private String groupRead;// 成长阅读
	private String socialPractice;// 社会实践
	private String selfControl;// 自制力
	private String confidence;// 自信心
	private String contact;// 合作交往
	
	private String attention;//注意
	private String observation;//观察
	private String memory;//记忆
	private String thinking;//思维
	private String mood;//情绪
	private String will;//意志
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getLeftEye() {
		return leftEye;
	}
	public void setLeftEye(String leftEye) {
		this.leftEye = leftEye;
	}
	public String getRightEye() {
		return rightEye;
	}
	public void setRightEye(String rightEye) {
		this.rightEye = rightEye;
	}
	public String getPhysique() {
		return physique;
	}
	public void setPhysique(String physique) {
		this.physique = physique;
	}
	public String getGroupRead() {
		return groupRead;
	}
	public void setGroupRead(String groupRead) {
		this.groupRead = groupRead;
	}
	public String getSocialPractice() {
		return socialPractice;
	}
	public void setSocialPractice(String socialPractice) {
		this.socialPractice = socialPractice;
	}
	public String getSelfControl() {
		return selfControl;
	}
	public void setSelfControl(String selfControl) {
		this.selfControl = selfControl;
	}
	public String getConfidence() {
		return confidence;
	}
	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
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
	
	public void toEntity(StuHealthRecord entity, StuHealthRecordDto dto){
		if(StringUtils.isNotBlank(dto.getId())){
			entity.setId(dto.getId());
		}
		if(StringUtils.isNotBlank(dto.getStudentId())){
			entity.setStudentId(dto.getStudentId());
		}
		if(StringUtils.isNotBlank(dto.getAcadyear())){
			entity.setAcadyear(dto.getAcadyear());
		}
		if(StringUtils.isNotBlank(dto.getSemester())){
			entity.setSemester(dto.getSemester());
		}
		if(StringUtils.isNotBlank(dto.getHeight())){
			entity.setHeight(Double.parseDouble(dto.getHeight()));
		}
		if(StringUtils.isNotBlank(dto.getWeight())){
			entity.setWeight(Double.parseDouble(dto.getWeight()));
		}
		if(StringUtils.isNotBlank(dto.getLeftEye())){
			entity.setLeftEye(Double.parseDouble(dto.getLeftEye()));
		}
		if(StringUtils.isNotBlank(dto.getRightEye())){
			entity.setRightEye(Double.parseDouble(dto.getRightEye()));
		}
		if(StringUtils.isNotBlank(dto.getPhysique())){
			entity.setPhysique(Double.parseDouble(dto.getPhysique()));
		}
		if(StringUtils.isNotBlank(dto.getGroupRead())){
			entity.setGroupRead(Double.parseDouble(dto.getGroupRead()));
		}
		if(StringUtils.isNotBlank(dto.getSocialPractice())){
			entity.setSocialPractice(Double.parseDouble(dto.getSocialPractice()));
		}
		if(StringUtils.isNotBlank(dto.getSelfControl())){
			entity.setSelfControl(Double.parseDouble(dto.getSelfControl()));
		}
		if(StringUtils.isNotBlank(dto.getConfidence())){
			entity.setConfidence(Double.parseDouble(dto.getConfidence()));
		}
		if(StringUtils.isNotBlank(dto.getContact())){
			entity.setContact(Double.parseDouble(dto.getContact()));
		}
		if(StringUtils.isNotBlank(dto.getAttention())){
			entity.setAttention(dto.getAttention());
		}
		if(StringUtils.isNotBlank(dto.getObservation())){
			entity.setObservation(dto.getObservation());
		}
		if(StringUtils.isNotBlank(dto.getMemory())){
			entity.setMemory(dto.getMemory());
		}
		if(StringUtils.isNotBlank(dto.getThinking())){
			entity.setThinking(dto.getThinking());
		}
		if(StringUtils.isNotBlank(dto.getMood())){
			entity.setMood(dto.getMood());
		}
		if(StringUtils.isNotBlank(dto.getWill())){
			entity.setWill(dto.getWill());
		}
	}
	
	public void toDto(StuHealthRecord entity, StuHealthRecordDto dto){
		dto.setId(entity.getId());
		dto.setStudentId(entity.getStudentId());
		dto.setAcadyear(entity.getAcadyear());
		dto.setSemester(entity.getSemester());
		dto.setHeight(entity.getHeight()+"");
		dto.setWeight(entity.getWeight()+"");
		dto.setLeftEye(entity.getLeftEye()+"");
		dto.setRightEye(entity.getRightEye()+"");
		dto.setPhysique(entity.getPhysique()+"");
		dto.setGroupRead(entity.getGroupRead()+"");
		dto.setSocialPractice(entity.getSocialPractice()+"");
		dto.setSelfControl(entity.getSelfControl()+"");
		dto.setConfidence(entity.getConfidence()+"");
		dto.setContact(entity.getContact()+"");
		dto.setAttention(entity.getAttention());
		dto.setObservation(entity.getAttention());
		dto.setMemory(entity.getMemory());
		dto.setThinking(entity.getThinking());
		dto.setMood(entity.getMood());
		dto.setWill(entity.getWill());
	}
}
