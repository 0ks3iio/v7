package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name = "exammanage_place_teacher")
public class EmPlaceTeacher extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;

    @ColumnInfo(displayName = "单位Id", nullable = false)
    private String unitId;
    @ColumnInfo(displayName = "考试Id", nullable = false)
    private String examId;
    @ColumnInfo(displayName = "考场Id", nullable = true)
    private String examPlaceId;
    @ColumnInfo(displayName = "编内老师", nullable = true)
    private String teacherIdsIn;
    @ColumnInfo(displayName = "编外老师", nullable = true)
    private String teacherIdsOut;
    @ColumnInfo(displayName = "科目Id", nullable = false)
    private String subjectId;
    @ColumnInfo(displayName = "考试开始时间", nullable = false, vtype = ColumnInfo.VTYPE_DATE, format = "yyyy-MM-dd")
    private Date startTime;
    @ColumnInfo(displayName = "考试结束时间", nullable = false, vtype = ColumnInfo.VTYPE_DATE, format = "yyyy-MM-dd")
    private Date endTime;
    @ColumnInfo(displayName = "类型", nullable = false)
    private String type;

    @Transient
    private String teacherInNames;
    @Transient
    private String teacherOutNames;
    @Transient
    private int minute;

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getExamId() {
        return examId;
    }


    public void setExamId(String examId) {
        this.examId = examId;
    }


    public String getExamPlaceId() {
        return examPlaceId;
    }


    public void setExamPlaceId(String examPlaceId) {
        this.examPlaceId = examPlaceId;
    }


    public String getTeacherIdsIn() {
        return teacherIdsIn;
    }


    public void setTeacherIdsIn(String teacherIdsIn) {
        this.teacherIdsIn = teacherIdsIn;
    }


    public String getTeacherIdsOut() {
        return teacherIdsOut;
    }


    public void setTeacherIdsOut(String teacherIdsOut) {
        this.teacherIdsOut = teacherIdsOut;
    }


    public String getSubjectId() {
        return subjectId;
    }


    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }


    public Date getStartTime() {
        return startTime;
    }


    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


    public Date getEndTime() {
        return endTime;
    }


    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String fetchCacheEntitName() {
        return "emPlaceTeacher";
    }


    public String getTeacherInNames() {
        return teacherInNames;
    }


    public void setTeacherInNames(String teacherInNames) {
        this.teacherInNames = teacherInNames;
    }


    public String getTeacherOutNames() {
        return teacherOutNames;
    }


    public void setTeacherOutNames(String teacherOutNames) {
        this.teacherOutNames = teacherOutNames;
    }


    public String getUnitId() {
        return unitId;
    }


    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

}
