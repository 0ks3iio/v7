package net.zdsoft.diathesis.data.dto;

/**
 * @Author: panlf
 * @Date: 2019/8/14 17:08
 */
public class DiathesisSemesterDto {
    private String name;
    private String semester;
    private String gradeCode;

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
