package net.zdsoft.diathesis.data.dto;

import java.io.Serializable;

public class DiatheisStudentDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String studentName;//姓名
	private String idCard;//身份证号
	private String sex;//性别
	private String schoolName;
	private String nation;
	private String stuCode;//学考号---studentCode
	private String birthDate;
	private String className;
	//福建增加
	private String unitiveCode;//学籍号--unitiveCode
	private String linkPhone;//联系电话
	private String toSchoolDate;//入学年份
	private String linkAddress;//住址
	private String strong;//特长
	private String background;//政治面貌---DM-ZZMM
	private String imgurl;//照片--暂定
	//自我陈述报告
	private String selfContent;
	//教师评语
	private String teacherContent;
	private String stuPicUrl;//头像路径
	
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getStuCode() {
		return stuCode;
	}
	public void setStuCode(String stuCode) {
		this.stuCode = stuCode;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getLinkPhone() {
		return linkPhone;
	}
	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}
	public String getToSchoolDate() {
		return toSchoolDate;
	}
	public void setToSchoolDate(String toSchoolDate) {
		this.toSchoolDate = toSchoolDate;
	}
	public String getLinkAddress() {
		return linkAddress;
	}
	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}
	public String getStrong() {
		return strong;
	}
	public void setStrong(String strong) {
		this.strong = strong;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
	public String getUnitiveCode() {
		return unitiveCode;
	}
	public void setUnitiveCode(String unitiveCode) {
		this.unitiveCode = unitiveCode;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getSelfContent() {
		return selfContent;
	}
	public void setSelfContent(String selfContent) {
		this.selfContent = selfContent;
	}
	public String getTeacherContent() {
		return teacherContent;
	}
	public void setTeacherContent(String teacherContent) {
		this.teacherContent = teacherContent;
	}
	public String getStuPicUrl() {
		return stuPicUrl;
	}
	public void setStuPicUrl(String stuPicUrl) {
		this.stuPicUrl = stuPicUrl;
	}
	
	

}
