package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "exammanage_out_teacher")
public class EmOutTeacher extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;

    @ColumnInfo(displayName = "考试Id", nullable = false)
    private String examId;
    @ColumnInfo(displayName = "学校Id", nullable = false)
    private String schoolId;
    @ColumnInfo(displayName = "老师姓名", nullable = false)
    private String teacherName;
    @ColumnInfo(displayName = "电话号码", nullable = false)
    private String mobilePhone;


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


    public String getTeacherName() {
        return teacherName;
    }


    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }


    public String getMobilePhone() {
        return mobilePhone;
    }


    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }


    @Override
    public String fetchCacheEntitName() {
        return "emOutTeacher";
    }

}
