package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "exammanage_student_apply_count")
public class EmEnrollStuCount extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;

    @ColumnInfo(displayName = "考试Id", nullable = false)
    private String examId;
    @ColumnInfo(displayName = "学校Id", nullable = false)
    private String schoolId;
    @ColumnInfo(displayName = "通过人数", nullable = false)
    private Integer passNum;
    @ColumnInfo(displayName = "未通过人数", nullable = false)
    private Integer notPassNum;
    @ColumnInfo(displayName = "总人数", nullable = false)
    private Integer sumCount;

    @Transient
    private String schoolName;
    @Transient
    private Integer otherNum;


    public Integer getOtherNum() {
        return otherNum;
    }


    public void setOtherNum(Integer otherNum) {
        this.otherNum = otherNum;
    }


    public String getSchoolName() {
        return schoolName;
    }


    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }


    public String getExamId() {
        return examId;
    }


    public void setExamId(String examId) {
        this.examId = examId;
    }


    public String getSchoolId() {
        return schoolId;
    }


    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }


    public Integer getPassNum() {
        return passNum;
    }


    public void setPassNum(Integer passNum) {
        this.passNum = passNum;
    }


    public Integer getNotPassNum() {
        return notPassNum;
    }


    public void setNotPassNum(Integer notPassNum) {
        this.notPassNum = notPassNum;
    }


    public Integer getSumCount() {
        return sumCount;
    }


    public void setSumCount(Integer sumCount) {
        this.sumCount = sumCount;
    }


    @Override
    public String fetchCacheEntitName() {
        return "emEnrollStuCount";
    }

}
