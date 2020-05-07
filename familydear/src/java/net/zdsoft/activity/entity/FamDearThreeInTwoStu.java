package net.zdsoft.activity.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name="famdear_threeInTwoStu")
public class FamDearThreeInTwoStu extends BaseEntity<String>{
	
	private String createUserId;
	private Date createTime;
	private String unitId;
	private String teacherId;
	private String stuname;
	private String sex;
	private String nation;//民族
	private String birthday;
	private String school;
	private String sdept;//--系部
	private String dormitoryNum;//宿舍号
	private String politicCountenance;//政治面貌
	private String intake;//入学时间
	private String schoolSystem;//学制
	private String major;//专业
	private String homeAddress;//家庭住址
	private String identityCard;//身份证
	private String linkPhone;//联系电话
	private String leaderPhone;//负责人及联系电话
	private String headmasterPhone;//班主任及联系电话
	private String email;//
	private String resume;//简历
	private String showContent;//主要表现
	private String situation;//家庭情况
	private String isDelete;
	
	@Transient
	private List<FamDearThreeInTwoStuMember> stuTempList;
	@Transient
	private String deptName;
	@Transient
	private String teacherName;

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getStuname() {
		return stuname;
	}

	public void setStuname(String stuname) {
		this.stuname = stuname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSdept() {
		return sdept;
	}

	public void setSdept(String sdept) {
		this.sdept = sdept;
	}

	public String getDormitoryNum() {
		return dormitoryNum;
	}

	public void setDormitoryNum(String dormitoryNum) {
		this.dormitoryNum = dormitoryNum;
	}

	public String getPoliticCountenance() {
		return politicCountenance;
	}

	public void setPoliticCountenance(String politicCountenance) {
		this.politicCountenance = politicCountenance;
	}

	public String getIntake() {
		return intake;
	}

	public void setIntake(String intake) {
		this.intake = intake;
	}

	public String getSchoolSystem() {
		return schoolSystem;
	}

	public void setSchoolSystem(String schoolSystem) {
		this.schoolSystem = schoolSystem;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getLeaderPhone() {
		return leaderPhone;
	}

	public void setLeaderPhone(String leaderPhone) {
		this.leaderPhone = leaderPhone;
	}

	public String getHeadmasterPhone() {
		return headmasterPhone;
	}

	public void setHeadmasterPhone(String headmasterPhone) {
		this.headmasterPhone = headmasterPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public String getShowContent() {
		return showContent;
	}

	public void setShowContent(String showContent) {
		this.showContent = showContent;
	}

	public String getSituation() {
		return situation;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}

	public List<FamDearThreeInTwoStuMember> getStuTempList() {
		return stuTempList;
	}

	public void setStuTempList(List<FamDearThreeInTwoStuMember> stuTempList) {
		this.stuTempList = stuTempList;
	}

	@Override
	public String fetchCacheEntitName() {
		return "famDearThreeInTwoStu";
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

}
