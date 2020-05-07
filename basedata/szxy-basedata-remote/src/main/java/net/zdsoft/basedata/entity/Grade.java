package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_grade")
public class Grade extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_WEEK_DAYS = 7;  //默认一周上课7天
	
	@ColumnInfo(displayName = "年级名称", nullable = false,maxLength=30)
	private String gradeName;
	@ColumnInfo(displayName = "年级代码", nullable = false,maxLength=3)
	private String gradeCode;
	@ColumnInfo(displayName = "所属学校", nullable = false, hide = true)
	@Column(updatable=false)
	private String schoolId;
	@ColumnInfo(displayName = "学年学期", nullable = false, vsql="select distinct acadyear, acadyear from base_semester where is_deleted = 0 order by 1", vtype=ColumnInfo.VTYPE_SELECT)
	@Column(updatable=false)
	private String openAcadyear;
	@ColumnInfo(displayName = "学制", nullable = false, vtype = ColumnInfo.VTYPE_INT, min="1", maxLength=2)
	private Integer schoolingLength;
	@ColumnInfo(displayName = "学段", nullable = false, vtype = ColumnInfo.VTYPE_RADIO, mcodeId="DM-RKXD")
	private Integer section;
	@ColumnInfo(displayName = "年级组长", vtype = ColumnInfo.VTYPE_SELECT, vsql="select id, teacher_name from base_teacher where is_deleted = 0 and unit_id = {schoolId}")
	private String teacherId;
	@ColumnInfo(displayName = "排序号", vtype = ColumnInfo.VTYPE_INT, min="0", maxLength=10)
	private Integer displayOrder;
	@ColumnInfo(displayName = "上午课时数", nullable = false, vtype = ColumnInfo.VTYPE_INT, min="0", maxLength=1)
	private Integer amLessonCount;
	@ColumnInfo(displayName = "下午课时数", nullable = false, vtype = ColumnInfo.VTYPE_INT, min="0", maxLength=1)
	private Integer pmLessonCount;
	@ColumnInfo(displayName = "晚上课时数", nullable = false, vtype = ColumnInfo.VTYPE_INT, min="0", maxLength=1)
	private Integer nightLessonCount;
	@Temporal(TemporalType.TIMESTAMP)
	@ColumnInfo(displayName = "创建时间", disabled = true)
	private Date creationTime;
	@ColumnInfo(displayName = "修改时间", disabled = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	@ColumnInfo(displayName = "是否删除", hide = true, vtype = ColumnInfo.VTYPE_RADIO, mcodeId = "DM-BOOLEAN")
	private int isDeleted = 0;
	private Integer eventSource;
	@ColumnInfo(displayName = "校区", vtype = ColumnInfo.VTYPE_SELECT, vsql="select id, area_name from base_teach_area where is_deleted = 0 and unit_id = {schoolId}")
	private String subschoolId;
	@ColumnInfo(displayName = "是否毕业", hide=true, vtype = ColumnInfo.VTYPE_INT)
	private int isGraduate;
	private String recess;// 大课间，如22,32，表示上午第2节课后和下午第2节课后是大课间
	@ColumnInfo(displayName = "早上课时数", nullable = false, vtype = ColumnInfo.VTYPE_INT, min="0", maxLength=1)
	private Integer mornPeriods;
	@ColumnInfo(displayName = "每周上课天数", nullable = false, vtype = ColumnInfo.VTYPE_INT, min="5", maxLength=1)
	private Integer weekDays;
	/** <b>已毕业</b>*/
	public static final Integer GRADUATED = 1;

	/** <b>未毕业</b>*/
	public static final Integer NOT_GRADUATED = 0;
	
	@Override
	public String fetchCacheEntitName() {
		return "grade";
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public Integer getAmLessonCount() {
		if(amLessonCount==null) {
			amLessonCount=4;
		}
		return amLessonCount;
	}

	public void setAmLessonCount(Integer amLessonCount) {
		this.amLessonCount = amLessonCount;
	}

	public Integer getPmLessonCount() {
		if(pmLessonCount==null) {
			pmLessonCount=4;
		}
		return pmLessonCount;
	}

	public void setPmLessonCount(Integer pmLessonCount) {
		this.pmLessonCount = pmLessonCount;
	}

	public Integer getNightLessonCount() {
		if(nightLessonCount==null) {
			nightLessonCount=0;
		}
		return nightLessonCount;
	}

	public void setNightLessonCount(Integer nightLessonCount) {
		this.nightLessonCount = nightLessonCount;
	}

	public String getOpenAcadyear() {
		return openAcadyear;
	}

	public void setOpenAcadyear(String openAcadyear) {
		this.openAcadyear = openAcadyear;
	}

	public Integer getSchoolingLength() {
		return schoolingLength;
	}

	public void setSchoolingLength(Integer schoolingLength) {
		this.schoolingLength = schoolingLength;
	}

	public int getIsGraduate() {
		return isGraduate;
	}

	public void setIsGraduate(int isGraduate) {
		this.isGraduate = isGraduate;
	}

	public Integer getSection() {
		return section;
	}

	public void setSection(Integer section) {
		this.section = section;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
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

	public Integer getEventSource() {
		return eventSource;
	}

	public void setEventSource(Integer eventSource) {
		this.eventSource = eventSource;
	}

	public String getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getSubschoolId() {
		return subschoolId;
	}

	public void setSubschoolId(String subschoolId) {
		this.subschoolId = subschoolId;
	}

	public String getRecess() {
		return recess;
	}

	public void setRecess(String recess) {
		this.recess = recess;
	}

	public Integer getMornPeriods() {
		if(mornPeriods==null) {
			mornPeriods=0;
		}
		return mornPeriods;
	}

	public void setMornPeriods(Integer mornPeriods) {
		this.mornPeriods = mornPeriods;
	}

	public Integer getWeekDays() {
		if(weekDays == null){
			weekDays = 7;
		}
		return weekDays;
	}

	public void setWeekDays(Integer weekDays) {
		this.weekDays = weekDays;
	}

}
