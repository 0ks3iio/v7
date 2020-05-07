package net.zdsoft.gkelective.data.dto;

public class GkSubjectDto {
	
	private String gkSubjectId;
	private String subjectId;
	private String subjectName;
	private int punchCard;//是否需要电子班排打卡
	private String[] teacherIds;
	private int teacherNum;
	public String getGkSubjectId() {
		return gkSubjectId;
	}
	public int getPunchCard() {
		return punchCard;
	}
	public void setPunchCard(int punchCard) {
		this.punchCard = punchCard;
	}
	public void setGkSubjectId(String gkSubjectId) {
		this.gkSubjectId = gkSubjectId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String[] getTeacherIds() {
		return teacherIds;
	}
	public void setTeacherIds(String[] teacherIds) {
		this.teacherIds = teacherIds;
	}
	public int getTeacherNum() {
		return teacherNum;
	}
	public void setTeacherNum(int teacherNum) {
		this.teacherNum = teacherNum;
	}
	
}
