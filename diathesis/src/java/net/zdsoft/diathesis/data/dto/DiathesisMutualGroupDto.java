package net.zdsoft.diathesis.data.dto;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/5 9:37
 */
public class DiathesisMutualGroupDto {
    private String id;
    private String groupName;
    private Integer studentNum;
    private String leaderId;
    private String leaderName;
    private List<DiathesisIdAndNameDto> studentList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(Integer studentNum) {
        this.studentNum = studentNum;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public List<DiathesisIdAndNameDto> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<DiathesisIdAndNameDto> studentList) {
        this.studentList = studentList;
    }
}
