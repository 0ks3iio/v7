package net.zdsoft.exammanage.data.dto;

//准考证
public class EmTicketDto {
    //准考证号
    private String examNumber;
    private String name;
    private String schoolName;
    private String className;
    //学籍号
    private String unitiveCode;
    //考区
    private String examRegion;
    private String examRegionName;
    //考点
    private String examOption;
    private String examOptionName;
    //考场
    private String examPlace;
    private String examPlaceAdd;

    private String seatNum;
    private String studentFilePath;

    public String getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(String seatNum) {
        this.seatNum = seatNum;
    }

    public String getStudentFilePath() {
        return studentFilePath;
    }

    public void setStudentFilePath(String studentFilePath) {
        this.studentFilePath = studentFilePath;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getUnitiveCode() {
        return unitiveCode;
    }

    public void setUnitiveCode(String unitiveCode) {
        this.unitiveCode = unitiveCode;
    }

    public String getExamRegion() {
        return examRegion;
    }

    public void setExamRegion(String examRegion) {
        this.examRegion = examRegion;
    }

    public String getExamRegionName() {
        return examRegionName;
    }

    public void setExamRegionName(String examRegionName) {
        this.examRegionName = examRegionName;
    }

    public String getExamOption() {
        return examOption;
    }

    public void setExamOption(String examOption) {
        this.examOption = examOption;
    }

    public String getExamOptionName() {
        return examOptionName;
    }

    public void setExamOptionName(String examOptionName) {
        this.examOptionName = examOptionName;
    }

    public String getExamPlace() {
        return examPlace;
    }

    public void setExamPlace(String examPlace) {
        this.examPlace = examPlace;
    }

    public String getExamPlaceAdd() {
        return examPlaceAdd;
    }

    public void setExamPlaceAdd(String examPlaceAdd) {
        this.examPlaceAdd = examPlaceAdd;
    }


}
