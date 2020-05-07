package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "exammanage_place_student")
public class EmPlaceStudent extends BaseEntity<String> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String examId;
    private String examPlaceId;
    private String studentId;
    private String seatNum;
    private String examNumber;
    private String groupId;

    @Transient
    private String exPlaceName;
    @Transient
    private String exPlaceCode;
    @Transient
    private String studentName;
    @Transient
    private String studentFilePath;
    @Transient
    private String schoolId;
    @Transient
    private String schName;
    @Transient
    private String student;

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSchName() {
        return schName;
    }

    public void setSchName(String schName) {
        this.schName = schName;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getExPlaceName() {
        return exPlaceName;
    }

    public void setExPlaceName(String exPlaceName) {
        this.exPlaceName = exPlaceName;
    }

    public String getExPlaceCode() {
        return exPlaceCode;
    }

    public void setExPlaceCode(String exPlaceCode) {
        this.exPlaceCode = exPlaceCode;
    }

    public String getExamPlaceId() {
        return examPlaceId;
    }

    public void setExamPlaceId(String examPlaceId) {
        this.examPlaceId = examPlaceId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(String seatNum) {
        this.seatNum = seatNum;
    }

    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
    }

    @Override
    public String fetchCacheEntitName() {
        return "emPlaceStudent";
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentFilePath() {
        return studentFilePath;
    }

    public void setStudentFilePath(String studentFilePath) {
        this.studentFilePath = studentFilePath;
    }

}
