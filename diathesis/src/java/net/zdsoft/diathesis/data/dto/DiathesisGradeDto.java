package net.zdsoft.diathesis.data.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Date: 2019/03/28
 *
 */
public class DiathesisGradeDto {
    private String unitId;
    @NotBlank(message = "gradeId不能为空")
    private String gradeId;
    @NotBlank(message = "gradeCode不能为空")
    private String gradeCode;
    private String gradeName;
    private String year;
    @NotBlank(message = "semester不能为空")
    @Pattern(regexp = "[12]",message = "semester类型错误")
    private String semester;
    private String semesterName;
    @NotBlank(message = "type不能为空")
    @Pattern(regexp = "[02]",message = "type类型错误")
    private String type;
    private List<DiathesisClassDto> classList;
    private List<DiathesisSemesterDto> semesterDtoList=new ArrayList<>();
    @NotEmpty(message = "至少选一门录入科目")
    private List<String> subjectIds;

    private List<DiathesisScoreTypeDto> scoreList;

    public List<String> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(List<String> subjectIds) {
        this.subjectIds = subjectIds;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public List<DiathesisClassDto> getClassList() {
        return classList;
    }

    public void setClassList(List<DiathesisClassDto> classList) {
        this.classList = classList;
    }

    public List<DiathesisSemesterDto> getSemesterDtoList() {
        return semesterDtoList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSemesterDtoList(List<DiathesisSemesterDto> semesterDtoList) {
        this.semesterDtoList = semesterDtoList;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public List<DiathesisScoreTypeDto> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<DiathesisScoreTypeDto> scoreList) {
        this.scoreList = scoreList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiathesisGradeDto that = (DiathesisGradeDto) o;
        return gradeCode.equals(that.gradeCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gradeCode);
    }
}
