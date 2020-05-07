package net.zdsoft.gkelective.data.dto;

public class ClassChangeDto {

	/**
	 * 1：groupclass
	 */
	public static final String TYPE_1="1";
	/**
	 * 2：teachclass
	 */
	public static final String TYPE_2="2";
	
	private String classId;
	private String name;
	/**
	 * 1：groupclass 2：teachclass
	 */
	private String type;
	private String subjectIds;
	private Integer batch;
	private String classType;//A选考 B学考
	
	private int num;//人数
	
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public Integer getBatch() {
		return batch;
	}
	public void setBatch(Integer batch) {
		this.batch = batch;
	}
	public String getSubjectIds() {
		return subjectIds;
	}
	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
}
