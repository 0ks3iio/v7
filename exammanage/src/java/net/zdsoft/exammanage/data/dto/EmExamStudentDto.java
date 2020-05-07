package net.zdsoft.exammanage.data.dto;

public class EmExamStudentDto {

    private String studentId; //学生Id
    private String subjectName; //科目
    private String subjectId; //科目Id
    private String clazzId;
    private String total; //总分
    private String gradeAverage; //年级平均分
    private String classAverage; //班级平均分
    private String classMax; //班级最高分
    private String classMin; //班级最低分
    private String gradeRanking; //年级排名
    private String classRanking; //班级排名

    public String getClazzId() {
        return clazzId;
    }

    public void setClazzId(String clazzId) {
        this.clazzId = clazzId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getGradeAverage() {
        return gradeAverage;
    }

    public void setGradeAverage(String gradeAverage) {
        this.gradeAverage = gradeAverage;
    }

    public String getClassAverage() {
        return classAverage;
    }

    public void setClassAverage(String classAverage) {
        this.classAverage = classAverage;
    }

    public String getClassMax() {
        return classMax;
    }

    public void setClassMax(String classMax) {
        this.classMax = classMax;
    }

    public String getClassMin() {
        return classMin;
    }

    public void setClassMin(String classMin) {
        this.classMin = classMin;
    }

    public String getGradeRanking() {
        return gradeRanking;
    }

    public void setGradeRanking(String gradeRanking) {
        this.gradeRanking = gradeRanking;
    }

    public String getClassRanking() {
        return classRanking;
    }

    public void setClassRanking(String classRanking) {
        this.classRanking = classRanking;
    }
}
