package net.zdsoft.tutor.data.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2017年11月20日上午9:59:21
 */
@Entity
@Table(name="tutor_record_detailed")
public class TutorRecordDetailed extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
    private String unitId;
    private String acadyear;
    private String semester;
    private String recordType;
    private String classId;
    private String teacherId;
    private String recordResult;
    private Integer isStudentShow;  // 1 -- 可见   0 -- 不可见
    private Integer isFamilyShow;   // 1 -- 可见   0 -- 不可见
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
	@Override
	public String fetchCacheEntitName() {
		return "tutorRecordDetailed";
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
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
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
}
