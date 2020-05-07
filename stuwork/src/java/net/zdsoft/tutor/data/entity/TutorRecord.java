package net.zdsoft.tutor.data.entity;


import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.apache.commons.lang3.StringUtils;
import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2017年9月11日下午7:41:32
 * 导师记录表
 */
@Entity
@Table(name="tutor_record")
public class TutorRecord extends BaseEntity<String>{
	public static final Integer TUTOR_RECORD_IS_STU_SHOW = 1;
	public static final Integer TUTOR_RECORD_ISNOT_STU_SHOW = 0;
	public static final Integer TUTOR_RECORD_IS_FAM_SHOW = 1;
	public static final Integer TUTOR_RECORD_ISNOT_FAM_SHOW = 0;
	private static final long serialVersionUID = 1L;
    private String unitId;
    private String acadyear;
    private String semester;
    private String recordType;
    private String studentId;
    private String teacherId;
    private String recordResult;
    private Integer isStudentShow;  // 1 -- 可见   0 -- 不可见
    private Integer isFamilyShow;   // 1 -- 可见   0 -- 不可见
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
    private String detailedId;  //TutorRecordDetailed的id
    @Transient
    private String teacherName;
	@Override
	public String fetchCacheEntitName() {
		return "tutorRecord";
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
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
	public String getRecordType() {
		return StringUtils.trim(recordType);
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getRecordResult() {
		return recordResult;
	}
	public void setRecordResult(String recordResult) {
		this.recordResult = recordResult;
	}
	public Integer getIsStudentShow() {
		return isStudentShow;
	}
	public void setIsStudentShow(Integer isStudentShow) {
		this.isStudentShow = isStudentShow;
	}
	public Integer getIsFamilyShow() {
		return isFamilyShow;
	}
	public void setIsFamilyShow(Integer isFamilyShow) {
		this.isFamilyShow = isFamilyShow;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Date getModofyTime() {
		return modifyTime;
	}
	public void setModofyTime(Date modofyTime) {
		this.modifyTime = modofyTime;
	}
	public String getDetailedId() {
		return detailedId;
	}
	public void setDetailedId(String detailedId) {
		this.detailedId = detailedId;
	}
}
