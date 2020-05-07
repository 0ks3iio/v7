package net.zdsoft.scoremanage.data.dto;

/**
 * @author niuchao
 * @date 2019/11/12 15:06
 */
public class StudentFlowDto {
    private String studentId;
    private String studentName;
    private String oldGradeId;
    private String gradeId;
    private String gradeName;
    private String identityCard;
    private String sourceGradeId;
    private String currentGradeId;

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

    public String getOldGradeId() {
        return oldGradeId;
    }

    public void setOldGradeId(String oldGradeId) {
        this.oldGradeId = oldGradeId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getSourceGradeId() {
        return sourceGradeId;
    }

    public void setSourceGradeId(String sourceGradeId) {
        this.sourceGradeId = sourceGradeId;
    }

    public String getCurrentGradeId() {
        return currentGradeId;
    }

    public void setCurrentGradeId(String currentGradeId) {
        this.currentGradeId = currentGradeId;
    }
}
