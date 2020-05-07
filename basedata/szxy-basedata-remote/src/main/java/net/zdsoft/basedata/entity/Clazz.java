package net.zdsoft.basedata.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.SUtils;

@Entity
@Table(name="base_class")
public class Clazz extends BaseEntity<String>{
	
	public static final String CLASS_TYPE_XZB="1";//行政班
	public static final String CLASS_TYPE_JXB="2";//教学班
	
	private static final long serialVersionUID = 1L;
	@Column(updatable=false)
	private String schoolId;
	private String classCode;
	private String className;
	private String acadyear;
	private Integer isGraduate;
	private Integer schoolingLength;
//	@Column(updatable=false)
	private String gradeId;
	@Column(updatable=false)
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
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private int isDeleted;
	private int eventSource;
	private String campusId;
	@Transient
	private String specialtyId;
	@Transient
	private String specialtyPointId;
	private String teachAreaId;
	private String teachPlaceId;
	@Transient
	private String teachPlaceName;
	@Transient
	private String isPreGraduate;
	@Transient
	private String graduateAcadyear;
	private Integer state;
	@Transient
	private String firstType;
	private String partnership;
	private String partners;
	private String contacts;
	private String remark;
	private String lifeGuideTeacherId;
	private String isDuplexClass;
	@Transient
	private Long sequenceIntId;
	@Transient
	private String classNameDynamic; // 动态班级名称
	@Transient
	private float score;	
	@Transient
	private String punishStuNames;
	@Transient
	private String studentIds;
	@Transient
	private String teacherName;
	
	/** <b>已毕业</b>*/
	public static final Integer GRADUATED = 1;

	/** <b>未毕业</b>*/
	public static final Integer NOT_GRADUATED = 0;
	
	/** <b>新高考衍生出来 解散</b>*/
	public static final Integer GRADUATED_JS = 2;
	
	public static List<Clazz> dt(String data) {
		List<Clazz> ts = SUtils.dt(data, new TypeReference<List<Clazz>>() {
		});
		if (ts == null)
			ts = new ArrayList<Clazz>();
		return ts;

	}

	public static List<Clazz> dt(String data, Pagination page) {
		JSONObject json = JSONObject.parseObject(data);
		List<Clazz> ts = SUtils.dt(json.getString("data"), new TypeReference<List<Clazz>>() {
		});
		if (ts == null)
			ts = new ArrayList<Clazz>();
		if (json.containsKey("count"))
			page.setMaxRowCount(json.getInteger("count"));
		return ts;

	}

	public static Clazz dc(String data) {
		return SUtils.dc(data, Clazz.class);
	}
	
	public String getClassNameDynamic() {
		return classNameDynamic;
	}

	public void setClassNameDynamic(String classNameDynamic) {
		this.classNameDynamic = classNameDynamic;
	}

	@Override
	public String fetchCacheEntitName() {
		return "class";
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

	public String getSpecialtyId() {
		return specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}

	public String getSpecialtyPointId() {
		return specialtyPointId;
	}

	public void setSpecialtyPointId(String specialtyPointId) {
		this.specialtyPointId = specialtyPointId;
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

	public String getIsPreGraduate() {
		return isPreGraduate;
	}

	public void setIsPreGraduate(String isPreGraduate) {
		this.isPreGraduate = isPreGraduate;
	}

	public String getGraduateAcadyear() {
		return graduateAcadyear;
	}

	public void setGraduateAcadyear(String graduateAcadyear) {
		this.graduateAcadyear = graduateAcadyear;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getFirstType() {
		return firstType;
	}

	public void setFirstType(String firstType) {
		this.firstType = firstType;
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

	public Long getSequenceIntId() {
		return sequenceIntId;
	}

	public void setSequenceIntId(Long sequenceIntId) {
		this.sequenceIntId = sequenceIntId;
	}

	public String getTeachPlaceName() {
		return teachPlaceName;
	}

	public void setTeachPlaceName(String teachPlaceName) {
		this.teachPlaceName = teachPlaceName;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public String getStudentIds() {
		return studentIds;
	}

	public void setStudentIds(String studentIds) {
		this.studentIds = studentIds;
	}

	public String getPunishStuNames() {
		return punishStuNames;
	}

	public void setPunishStuNames(String punishStuNames) {
		this.punishStuNames = punishStuNames;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}


}
