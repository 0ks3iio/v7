package net.zdsoft.exammanage.data.dto;

import net.zdsoft.basedata.entity.Student;

public class EmStudentDto {

    private Student student;//用于展现
    private String className;//行政班名称(或者教学班名称)
    private String studentId;//学生id
    private String filter;//是否不排考  0:不排考 1:排考

    private String studentName;//用于提示

    //考场内信息
    private String placeName;//考场
    private String seatNum;//座位号
    private String examNumber;//考号
    private String placeNumber;//考场编号
    private int seatNumInt;//座位号
    private String emPlaceId;

    public String getEmPlaceId() {
        return emPlaceId;
    }

    public void setEmPlaceId(String emPlaceId) {
        this.emPlaceId = emPlaceId;
    }

    public String getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(String placeNumber) {
        this.placeNumber = placeNumber;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getSeatNumInt() {
        return seatNumInt;
    }

    public void setSeatNumInt(int seatNumInt) {
        this.seatNumInt = seatNumInt;
    }

}	
