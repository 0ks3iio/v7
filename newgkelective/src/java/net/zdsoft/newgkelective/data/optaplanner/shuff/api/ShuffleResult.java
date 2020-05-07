package net.zdsoft.newgkelective.data.optaplanner.shuff.api;

@com.thoughtworks.xstream.annotations.XStreamAlias(value = "shuffleResult2")
public class ShuffleResult {
	private String classId;//组合班id
	private String sameSubjectIds;//开设的科目中需要一起上课的科目
	
	private String nameSubjectIds;//用于该组合名称--与subjectIds区分 为了A,B区分
	
	private String subjectIds;//需要安排的所有科目
	private int studentNum;//学生人数
	
	//结果数据
	private String subjectId;
	private int bath;//批次点
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getSubjectIds() {
		return subjectIds;
	}
	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public int getBath() {
		return bath;
	}
	public void setBath(int bath) {
		this.bath = bath;
	}
	public int getStudentNum() {
		return studentNum;
	}
	public void setStudentNum(int studentNum) {
		this.studentNum = studentNum;
	}
	public String getSameSubjectIds() {
		return sameSubjectIds;
	}
	public void setSameSubjectIds(String sameSubjectIds) {
		this.sameSubjectIds = sameSubjectIds;
	}
	
	public String getNameSubjectIds() {
		return nameSubjectIds;
	}
	public void setNameSubjectIds(String nameSubjectIds) {
		this.nameSubjectIds = nameSubjectIds;
	}
	public String toString() {
		return classId+":"+nameSubjectIds+":"+subjectId+":"+bath;
	}
	
}
