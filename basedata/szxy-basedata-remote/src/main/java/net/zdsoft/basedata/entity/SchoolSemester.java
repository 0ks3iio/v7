package net.zdsoft.basedata.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author shenke
 * @since 2017.07.19
 */
@Entity
@Table(name = "stusys_semester")
public class SchoolSemester extends BaseEntity<String> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String acadyear;
    private Integer semester;
    private Date workBegin;
    private Date workEnd;
    private Date semesterBegin;
    private Date semesterEnd;
    private Integer eduDays;
    private Integer amPeriods;
    private Integer pmPeriods;
    private Integer nightPeriods;
    @Transient
    private Date creationTime;
    @Transient
    private Date modifyTime;
    private Integer isDeleted;
    @Transient
    private Integer eventSource;
    private Integer classHour;
    private Date registerDate;
    private Integer week;
    private Integer mornPeriods;
    private String schoolId;
    private Long updatestamp;
    @Override
    public String fetchCacheEntitName() {
        return "schoolSemester";
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public Semester toSemester() {
        Semester semester = new Semester();
        semester.setAcadyear(acadyear);
        semester.setSemester(this.semester);
        semester.setWorkBegin(workBegin);
        semester.setWorkEnd(workEnd);
        semester.setSemesterBegin(semesterBegin);
        semester.setSemesterEnd(semesterEnd);
        semester.setEduDays(eduDays);
        semester.setAmPeriods(amPeriods);
        semester.setPmPeriods(pmPeriods);
        semester.setNightPeriods(nightPeriods);
        semester.setModifyTime(modifyTime);
        semester.setIsDeleted(isDeleted);
        semester.setEventSource(eventSource);
        semester.setClassHour(classHour);
        semester.setRegisterDate(registerDate);
        semester.setWeek(week);
        semester.setMornPeriods(mornPeriods);
        return semester;
    }
    public static void main(String[] args){
        SchoolSemester schoolSemester = new SchoolSemester();
        schoolSemester.setSchoolId("1234");
        schoolSemester.setClassHour(2);
        schoolSemester.toSemester();
    }

    public String getAcadyear() {
        return acadyear;
    }

    public void setAcadyear(String acadyear) {
        this.acadyear = acadyear;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Date getWorkBegin() {
        return workBegin;
    }

    public void setWorkBegin(Date workBegin) {
        this.workBegin = workBegin;
    }

    public Date getWorkEnd() {
        return workEnd;
    }

    public void setWorkEnd(Date workEnd) {
        this.workEnd = workEnd;
    }

    public Date getSemesterBegin() {
        return semesterBegin;
    }

    public void setSemesterBegin(Date semesterBegin) {
        this.semesterBegin = semesterBegin;
    }

    public Date getSemesterEnd() {
        return semesterEnd;
    }

    public void setSemesterEnd(Date semesterEnd) {
        this.semesterEnd = semesterEnd;
    }

    public Integer getEduDays() {
        return eduDays;
    }

    public void setEduDays(Integer eduDays) {
        this.eduDays = eduDays;
    }

    public Integer getAmPeriods() {
        return amPeriods;
    }

    public void setAmPeriods(Integer amPeriods) {
        this.amPeriods = amPeriods;
    }

    public Integer getPmPeriods() {
        return pmPeriods;
    }

    public void setPmPeriods(Integer pmPeriods) {
        this.pmPeriods = pmPeriods;
    }

    public Integer getNightPeriods() {
        return nightPeriods;
    }

    public void setNightPeriods(Integer nightPeriods) {
        this.nightPeriods = nightPeriods;
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

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getEventSource() {
        return eventSource;
    }

    public void setEventSource(Integer eventSource) {
        this.eventSource = eventSource;
    }

    public Integer getClassHour() {
        return classHour;
    }

    public void setClassHour(Integer classHour) {
        this.classHour = classHour;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getMornPeriods() {
        return mornPeriods;
    }

    public void setMornPeriods(Integer mornPeriods) {
        this.mornPeriods = mornPeriods;
    }

	public Long getUpdatestamp() {
		return updatestamp;
	}

	public void setUpdatestamp(Long updatestamp) {
		this.updatestamp = updatestamp;
	}
}
