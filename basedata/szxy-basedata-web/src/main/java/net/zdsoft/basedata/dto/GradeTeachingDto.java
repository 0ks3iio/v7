package net.zdsoft.basedata.dto;


public class GradeTeachingDto {

	private String id;// 年级开设科目以及班级开设科目使用

	private String subjectId;
	private String subjectName;
	private String belongToSubjectName;
	private String flag; // 是否已选,开设科目使用
	private String subjectType;// 区分已选未选
	private Integer isDeleted;// 是否启用 班级开设科目使用
	private Integer credit;// 学分
	private Integer isTeaCls;// 教学方式

	private Float courseHour;// 周课时
	
	private Integer fullMark;// 满分
	private Integer passMark;// 及格分
	private Integer punchCard;//是否考勤
	private Integer weekType;//单双周

	public Integer getPunchCard() {
		return punchCard;
	}

	public void setPunchCard(Integer punchCard) {
		this.punchCard = punchCard;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public Integer getIsTeaCls() {
		return isTeaCls;
	}

	public void setIsTeaCls(Integer isTeaCls) {
		this.isTeaCls = isTeaCls;
	}

	public Integer getFullMark() {
		return fullMark;
	}

	public Float getCourseHour() {
		return courseHour;
	}

	public void setCourseHour(Float courseHour) {
		this.courseHour = courseHour;
	}

	public void setFullMark(Integer fullMark) {
		this.fullMark = fullMark;
	}

	public Integer getPassMark() {
		return passMark;
	}

	public void setPassMark(Integer passMark) {
		this.passMark = passMark;
	}

	public String getBelongToSubjectName() {
		return belongToSubjectName;
	}

	public void setBelongToSubjectName(String belongToSubjectName) {
		this.belongToSubjectName = belongToSubjectName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
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

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Integer getWeekType() {
		return weekType;
	}

	public void setWeekType(Integer weekType) {
		this.weekType = weekType;
	}

}
