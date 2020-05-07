package net.zdsoft.exammanage.data.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "exammanage_aims_student")
public class ExammanageAimsStudent {
    @Id
    private String id;
    private String aimsId;
    private String schoolId;
    private String studentId;
    private String studentName;
    private String studentCode;
    private String identityCard;
    private String examCode;
    private String aimsSchoolId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

}
