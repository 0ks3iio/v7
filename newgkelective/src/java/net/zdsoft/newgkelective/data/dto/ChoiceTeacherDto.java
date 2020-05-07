package net.zdsoft.newgkelective.data.dto;

public class ChoiceTeacherDto {
	private String teacherId;
	private String teacherName;
	private String state;//0代表已经选择 1代表未选择
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

}
