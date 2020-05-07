package net.zdsoft.gkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.framework.entity.BaseEntity;

/**
 *备份班级表
 */
@Entity
@Table(name="gkelective_class_store")
public class GkClassStore extends BaseEntity<String>{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String classId;//备份班级Id
	private String teachPlacePlanId;//方案id
	private String schoolId;
	private String classCode;
	private String className;
	private String acadyear;
	private Integer isGraduate;
	private Integer schoolingLength;
	private String gradeId;
	private Integer section;
	private String honor;
	private Date buildDate;
	private String classType;
	private Integer artScienceType;
	private Date graduateDate;
	private String teacherId;
	private String studentId;
	private String viceTeacherId;
	private Integer displayOrder;
	private Date creationTime;
	private Date modifyTime;
	private int isDeleted;
	private int eventSource;
	private String campusId;
	private String teachAreaId;
	private String teachPlaceId;
	private Integer state;
	private String partnership;
	private String partners;
	private String contacts;
	private String remark;
	private String lifeGuideTeacherId;
	private String isDuplexClass;
		
	@Override
	public String fetchCacheEntitName() {
		return "gkClassStore";
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public Integer getIsGraduate() {
		return isGraduate;
	}

	public void setIsGraduate(Integer isGraduate) {
		this.isGraduate = isGraduate;
	}

	public Integer getSchoolingLength() {
		return schoolingLength;
	}

	public void setSchoolingLength(Integer schoolingLength) {
		this.schoolingLength = schoolingLength;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public Integer getSection() {
		return section;
	}

	public void setSection(Integer section) {
		this.section = section;
	}

	public String getHonor() {
		return honor;
	}

	public void setHonor(String honor) {
		this.honor = honor;
	}

	public Date getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(Date buildDate) {
		this.buildDate = buildDate;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public Integer getArtScienceType() {
		return artScienceType;
	}

	public void setArtScienceType(Integer artScienceType) {
		this.artScienceType = artScienceType;
	}

	public Date getGraduateDate() {
		return graduateDate;
	}

	public void setGraduateDate(Date graduateDate) {
		this.graduateDate = graduateDate;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getViceTeacherId() {
		return viceTeacherId;
	}

	public void setViceTeacherId(String viceTeacherId) {
		this.viceTeacherId = viceTeacherId;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getEventSource() {
		return eventSource;
	}

	public void setEventSource(int eventSource) {
		this.eventSource = eventSource;
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}


	public String getTeachAreaId() {
		return teachAreaId;
	}

	public void setTeachAreaId(String teachAreaId) {
		this.teachAreaId = teachAreaId;
	}

	public String getTeachPlaceId() {
		return teachPlaceId;
	}

	public void setTeachPlaceId(String teachPlaceId) {
		this.teachPlaceId = teachPlaceId;
	}

		
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}


	public String getPartnership() {
		return partnership;
	}

	public void setPartnership(String partnership) {
		this.partnership = partnership;
	}

	public String getPartners() {
		return partners;
	}

	public void setPartners(String partners) {
		this.partners = partners;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLifeGuideTeacherId() {
		return lifeGuideTeacherId;
	}

	public void setLifeGuideTeacherId(String lifeGuideTeacherId) {
		this.lifeGuideTeacherId = lifeGuideTeacherId;
	}

	public String getIsDuplexClass() {
		return isDuplexClass;
	}

	public void setIsDuplexClass(String isDuplexClass) {
		this.isDuplexClass = isDuplexClass;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getTeachPlacePlanId() {
		return teachPlacePlanId;
	}

	public void setTeachPlacePlanId(String teachPlacePlanId) {
		this.teachPlacePlanId = teachPlacePlanId;
	}

	public void toCopy(Clazz clazz) {
	 this.setClassId(clazz.getId());
	 this.setSchoolId(clazz.getSchoolId());
	 this.setClassCode(clazz.getClassCode());
	 this.setClassName(clazz.getClassName());
	 this.setAcadyear(clazz.getAcadyear());
	 this.setIsGraduate(clazz.getIsGraduate());
	 this.setSchoolingLength(clazz.getSchoolingLength());
	 this.setGradeId(clazz.getGradeId());
	 this.setSection(clazz.getSection());
	 this.setHonor(clazz.getHonor());
	 this.setBuildDate(clazz.getBuildDate());
	 this.setClassType(clazz.getClassType());
	 this.setArtScienceType(clazz.getArtScienceType());
	 this.setGraduateDate(clazz.getGraduateDate());
	 this.setTeacherId(clazz.getTeacherId());
	 this.setStudentId(clazz.getStudentId());
	 this.setViceTeacherId(clazz.getViceTeacherId());
	 this.setDisplayOrder(clazz.getDisplayOrder());
	 this.setCreationTime(clazz.getCreationTime());
	 this.setModifyTime(clazz.getModifyTime());
	 this.setIsDeleted(clazz.getIsDeleted());
	 this.setEventSource(clazz.getEventSource());
	 this.setCampusId(clazz.getCampusId());
	 this.setTeachAreaId(clazz.getTeachAreaId());
	 this.setTeachPlaceId(clazz.getTeachPlaceId());
	 this.setState(clazz.getState());
	 this.setPartnership(clazz.getPartnership());
	 this.setPartners(clazz.getPartners());
	 this.setContacts(clazz.getContacts());
	 this.setRemark(clazz.getRemark());
	 this.setLifeGuideTeacherId(clazz.getLifeGuideTeacherId());
	 this.setIsDuplexClass(clazz.getIsDuplexClass());
	}
}
