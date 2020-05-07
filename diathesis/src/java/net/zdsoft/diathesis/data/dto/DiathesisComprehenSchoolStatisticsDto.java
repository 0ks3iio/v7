package net.zdsoft.diathesis.data.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/3 13:29
 */
public class DiathesisComprehenSchoolStatisticsDto {
    private String schoolId;
    private String schoolName;
    private Integer studentNum;
    //全优人数
    private Integer excellentNum;
    //不及格人数
    private Integer failNum;

    private List<DiathesisProjectScoreDto> projectScoreList=new ArrayList<>();

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Integer getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(Integer studentNum) {
        this.studentNum = studentNum;
    }

    public Integer getExcellentNum() {
        return excellentNum;
    }

    public void setExcellentNum(Integer excellentNum) {
        this.excellentNum = excellentNum;
    }

    public Integer getFailNum() {
        return failNum;
    }

    public void setFailNum(Integer failNum) {
        this.failNum = failNum;
    }

    public List<DiathesisProjectScoreDto> getProjectScoreList() {
        return projectScoreList;
    }

    public void setProjectScoreList(List<DiathesisProjectScoreDto> projectScoreList) {
        this.projectScoreList = projectScoreList;
    }
}
