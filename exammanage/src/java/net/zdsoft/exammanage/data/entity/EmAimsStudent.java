package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "exammanage_aims_student")
public class EmAimsStudent extends BaseEntity<String> {

    /**
     *
     */
    private static final long serialVersionUID = 8025535580636856625L;
    private String aimsId;
    private String schoolId;
    private String studentId;
    private String studentName;
    private String studentCode;
    private String identityCard;
    private String examCode;
    private String aimsSchoolId;


    @Transient
    private String schoolName;
    @Transient
    private String aimsSchoolName;
    @Transient
    private String sex;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getAimsSchoolName() {
        return aimsSchoolName;
    }

    public void setAimsSchoolName(String aimsSchoolName) {
        this.aimsSchoolName = aimsSchoolName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAimsId() {
        return aimsId;
    }

    public void setAimsId(String aimsId) {
        this.aimsId = aimsId;
    }


    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }


    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }


    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }


    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }


    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }


    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }


    public String getAimsSchoolId() {
        return aimsSchoolId;
    }

    public void setAimsSchoolId(String aimsSchoolId) {
        this.aimsSchoolId = aimsSchoolId;
    }

    @Override
    public String fetchCacheEntitName() {
        return "exammanageAimsStudent";
    }

}
