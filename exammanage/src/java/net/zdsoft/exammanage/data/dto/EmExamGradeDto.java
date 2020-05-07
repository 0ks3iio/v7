package net.zdsoft.exammanage.data.dto;

import java.util.List;

public class EmExamGradeDto {

    private String classRanks;  //班级名次
    private String gradePlaces; //年级名次
    private String studentName;
    private String studentCode;
    private String identityCard;
    private String examNumber;
    private String total;      //总分
    private String className;
    private String classId;
    private boolean Cheat;
    private boolean Reforming;
    private boolean Missing;
    private List<String> scoresList;

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

    public boolean isCheat() {
        return Cheat;
    }

    public void setCheat(boolean cheat) {
        Cheat = cheat;
    }

    public boolean isReforming() {
        return Reforming;
    }

    public void setReforming(boolean reforming) {
        Reforming = reforming;
    }

    public boolean isMissing() {
        return Missing;
    }

    public void setMissing(boolean missing) {
        Missing = missing;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassRanks() {
        return classRanks;
    }

    public void setClassRanks(String classRanks) {
        this.classRanks = classRanks;
    }

    public String getGradePlaces() {
        return gradePlaces;
    }

    public void setGradePlaces(String gradePlaces) {
        this.gradePlaces = gradePlaces;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<String> getScoresList() {
        return scoresList;
    }

    public void setScoresList(List<String> scoresList) {
        this.scoresList = scoresList;
    }
}
